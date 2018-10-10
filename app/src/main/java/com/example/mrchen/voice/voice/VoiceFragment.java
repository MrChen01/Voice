package com.example.mrchen.voice.voice;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mrchen.voice.R;
import com.example.mrchen.voice.app.BaseFragment;
import com.example.mrchen.voice.utils.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

//语音识别
public class VoiceFragment extends BaseFragment {
    private Context mContext;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Button btn_speech;
    private Button btn_speech_recognizer;
    private TextView tv_show_recording;
    private EditText et_show_input_language;
    @Override
    public View initView() {
        mContext=mBaseContext;
        Log.i("TAG","VoiceFragment"+"View初始化成功");
        View view=View.inflate(mContext, R.layout.voice_fragment_layout,null);
        initViewItem(view);
        setListener();
        return view;
    }

    private void setListener() {
        btn_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.创建RecognizerDialog对象
                RecognizerDialog mDialog = new RecognizerDialog(mContext, new InitListener() {
                    @Override
                    public void onInit(int i) {
                        if (i != ErrorCode.SUCCESS) {
                            Log.i("TAG", "创建RecognizerDialog对象失败");
                        }
                    }
                });
                //2.设置accent、language等参数
                mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
                //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
                //结果
                // mDialog.setParameter("asr_sch", "1");
                // mDialog.setParameter("nlp_version", "2.0");

                //3.设置回调接口
                mDialog.setListener(new RecognizerDialogListener() {
                    @Override
                    public void onResult(RecognizerResult recognizerResult, boolean b) {
                        Log.i("TAG", recognizerResult.getResultString());
                        String text = JsonParser.parseIatResult(recognizerResult.getResultString());
//                        Log.i("TAG","recognizerResult 解析结果是："+text.toString());
                        String sn = null;
                        // 读取json结果中的sn字段
                        try {
                            JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
                            sn = resultJson.optString("sn");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mIatResults.put(sn, text);

                        StringBuffer resultBuffer = new StringBuffer();
                        for (String key : mIatResults.keySet()) {
                            resultBuffer.append(mIatResults.get(key));
                        }
                        et_show_input_language.setText(resultBuffer.toString());
                        et_show_input_language.setSelection(resultBuffer.length());
                        Log.i("TAG", "" + resultBuffer.toString());
                    }

                    @Override
                    public void onError(SpeechError speechError) {
                        Log.i("TAG", speechError.getPlainDescription(true));
                    }
                });
                //4.显示dialog，接收语音输入
                mDialog.show();

            }
        });

        //识别通过Recognizer
        btn_speech_recognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.创建SpeechRecognizer对象，第二个参数：本地识别时传InitListener
                SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(mContext, null);
                //2.设置听写参数，详见《MSC Reference Manual》SpeechConstant类
                mIat.setParameter(SpeechConstant.DOMAIN, "iat");
                mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
                //3.开始听写
                //听写监听器
                RecognizerListener mRecoListener = new RecognizerListener() {
                    //听写结果回调接口(返回Json格式结果，用户可参见附录13.1)；
                    //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
                    //关于解析Json的代码可参见Demo中JsonParser类； //isLast等于true时会话结束。
                    public void onResult(RecognizerResult results, boolean isLast) {
                        Log.d("TAG", "result:" + results.getResultString());
                        String text = JsonParser.parseIatResult(results.getResultString());
//                        Log.i("TAG","recognizerResult 解析结果是："+text.toString());
                        String sn = null;
                        // 读取json结果中的sn字段
                        try {
                            JSONObject resultJson = new JSONObject(results.getResultString());
                            sn = resultJson.optString("sn");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mIatResults.put(sn, text);

                        StringBuffer resultBuffer = new StringBuffer();
                        for (String key : mIatResults.keySet()) {
                            resultBuffer.append(mIatResults.get(key));
                        }
                        et_show_input_language.setText(resultBuffer.toString());
                        et_show_input_language.setSelection(resultBuffer.length());
                        Log.i("TAG", "" + resultBuffer.toString());

                    }

                    //会话发生错误回调接口
                    public void onError(SpeechError error) {
                        //打印错误码描
                        Log.d("TAG", "error:" + error.getPlainDescription(true));
                    }

                    //开始录音
                    public void onBeginOfSpeech() {
                        Log.i("TAG", "onBeginOfSpeech");
                    }

                    //volume音量值0~30，data音频数据
                    public void onVolumeChanged(int volume, byte[] data) {
                        tv_show_recording.setVisibility(View.VISIBLE);

                    }

                    //结束录音
                    public void onEndOfSpeech() {
                        Log.i("TAG", "onEndOfSpeech");
                        tv_show_recording.setVisibility(View.GONE);
                    }

                    //扩展用接口
                    public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

                    }
                };
                mIat.startListening(mRecoListener);
            }
        });
    }

    //找出语音合成界面的控件
    private void initViewItem(View view) {
        btn_speech = view.findViewById(R.id.btn_speech);
        btn_speech_recognizer = view.findViewById(R.id.btn_speech_recognizer);
        tv_show_recording = view.findViewById(R.id.tv_show_recording);
        et_show_input_language= view.findViewById(R.id.et_show_input_language);
    }

}
