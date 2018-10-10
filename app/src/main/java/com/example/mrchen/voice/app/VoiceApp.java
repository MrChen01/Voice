package com.example.mrchen.voice.app;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class VoiceApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化SDK
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5ba25989");

    }
}
