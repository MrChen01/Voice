package com.example.mrchen.voice.set;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.mrchen.voice.R;
import com.example.mrchen.voice.app.BaseFragment;

public class SetFragment extends BaseFragment {
    private Context mContext;
    @Override
    public View initView() {
        mContext=mBaseContext;
        Log.i("TAG","SetFragment"+"View初始化成功");
        return View.inflate(mContext, R.layout.set_fragment_layout,null);
    }
}
