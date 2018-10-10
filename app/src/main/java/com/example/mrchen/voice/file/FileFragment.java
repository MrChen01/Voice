package com.example.mrchen.voice.file;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.mrchen.voice.R;
import com.example.mrchen.voice.app.BaseFragment;

public class FileFragment extends BaseFragment {
    private Context mContext;
    @Override
    public View initView() {
        this.mContext=mBaseContext;
        Log.i("TAG","FileFragment"+"View初始化成功");
        return View.inflate(mContext, R.layout.file_fragment_layout,null);
    }
}
