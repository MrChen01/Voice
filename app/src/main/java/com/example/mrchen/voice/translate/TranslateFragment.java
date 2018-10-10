package com.example.mrchen.voice.translate;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.mrchen.voice.R;
import com.example.mrchen.voice.app.BaseFragment;
import com.example.mrchen.voice.app.MainActivity;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class TranslateFragment extends BaseFragment {
    private Context mContext;
    private EditText et_show_input_language;
    private Button btn_read;
    private RadioGroup rg_select_language;
    private SpeechSynthesizer mTts;

    @Override
    public View initView() {
        mContext=mBaseContext;
        Log.i("TAG","TranslateFragment"+"View初始化成功");
        View view=View.inflate(mContext, R.layout.translate_fragment_layout,null);
        initViewItem(view);
        initListener();
        return view;
    }

    private void initListener() {
        rg_select_language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
                mTts= SpeechSynthesizer.createSynthesizer(mContext, null);
                switch (checkedId){
                    //语言中文
                    case R.id.rb_zh_cn:
                        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//                设置发音人
                        mTts.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
                        break;
                    //语言美音
                    case  R.id.rb_en_us:
                        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");// 设置发音人
                        break;
                    //语言粤语
                    case R.id.rb_cantonese_yueyu:
                        mTts.setParameter(SpeechConstant.VOICE_NAME,"xiaomei");
                        mTts.setParameter(SpeechConstant.ACCENT,"cantonese");
                        break;
                }
            }
        });
        rg_select_language.check(R.id.rb_zh_cn);
        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
                mTts= SpeechSynthesizer.createSynthesizer(mContext, null);
                //2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
                //设置发音人（更多在线发音人，用户可参见 附录13.2
//                mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//                设置发音人
                mTts.setParameter(SpeechConstant.SPEED, "50");//                 设置语速
                mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围 0~100
                mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
//                String language=getSpeakerLanguage();
//                String accent=getSpeakerAccent();
                //设置语言
//                mTts.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
                //设置发言，口音
//                mTts.setParameter(SpeechConstant.VOICE_NAME,"xiaomei");
//                mTts.setParameter(SpeechConstant.ACCENT,"cantonese");
                //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm” //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
                //仅支持保存为 pcm 和 wav 格式，如果不需要保存合成音频，注释该行代码
                Log.i("TAG",mContext.getFilesDir().getAbsolutePath()+"/Test.pcm");
                mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,mContext.getFilesDir().getAbsolutePath()+"Test.pcm");
//                mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
                //合成监听器
                SynthesizerListener mSynListener = new SynthesizerListener(){
                    //会话结束回调接口，没有错误时，error为null
                    public void onCompleted(SpeechError error) {}
                    //缓冲进度回调
                    //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
                    public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
                        Log.i("TAG","onBufferProgress percent"+percent);
                        Log.i("TAG","onBufferProgress beginPos"+beginPos);
                        Log.i("TAG","onBufferProgress endPos"+endPos);
                        Log.i("TAG","onBufferProgress info"+info);

                    }
                    //开始播放
                    public void onSpeakBegin() {
                        Log.i("TAG","onSpeakBegin");
                    }
                    //暂停播放
                    public void onSpeakPaused() {
                        Log.i("TAG","onSpeakPaused");
                    }
                    //播放进度回调
                    //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
                    public void onSpeakProgress(int percent, int beginPos, int endPos) {
                        Log.i("TAG","onSpeakProgress percent"+percent);
                        Log.i("TAG","onSpeakProgress beginPos"+beginPos);
                        Log.i("TAG","onSpeakProgress endPos"+endPos);
                    }
                    //恢复播放回调接口
                    public void onSpeakResumed() {}
                    //会话事件回调接口
                    public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

                    }
                };
                //3.开始合成
                mTts.startSpeaking(et_show_input_language.getText().toString(), mSynListener);

            }
        });
    }

    private void initViewItem(View view) {
        et_show_input_language= (EditText)view.findViewById(R.id.et_show_input_language);
        btn_read= (Button)view.findViewById(R.id.btn_read);
        rg_select_language=(RadioGroup)view.findViewById(R.id.rg_select_language);
    }
}
