package com.example.mrchen.voice.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 使用Fragment,谷歌专门为安卓设计的技术。
 * FileFragment,setFragment,translateFragment,VoiceFragment均要继承BaseFragment
 */
public abstract class BaseFragment extends Fragment {
    public Context mBaseContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseContext= getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 由子类实现，效果由子类具体决定
     * @return view 子类布局
     */
    public abstract View initView();

    /**
     * 初始化子类数据，非抽象，由子类决定是否实现
     * 当有时，实现
     */
    public void initData(){

    }

}
