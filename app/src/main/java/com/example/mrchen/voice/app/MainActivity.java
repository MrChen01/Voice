package com.example.mrchen.voice.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mrchen.voice.R;
import com.example.mrchen.voice.file.FileFragment;
import com.example.mrchen.voice.set.SetFragment;
import com.example.mrchen.voice.translate.TranslateFragment;
import com.example.mrchen.voice.voice.VoiceFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    /**
     * 将四个Fragment加入到List中，通过事务进行切换
     */
    private List<BaseFragment> listFragment;
    private RadioGroup radioGroup;
    private int positionFragment=0;
    /**
     * 临时存储当前界面（Fragment）
     */
    private BaseFragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup= (RadioGroup)findViewById(R.id.radiogroup);
        applypermission();
        initData();
        initListener();
    }

    private void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_voice:
                        positionFragment=0;
                        break;
                    case R.id.rb_translate:
                        positionFragment=1;
                        break;
                    case R.id.rb_file:
                        positionFragment=2;
                        break;
                    case R.id.rb_set:
                        positionFragment=3;
                        break;
                    default:
                        positionFragment=0;
                        break;
                }
                BaseFragment nextFragment=getFragment(positionFragment);
                switchFragment(tempFragment, nextFragment);
            }
        });
        radioGroup.check(R.id.rb_voice);
    }

    private void switchFragment(BaseFragment fromFragment, BaseFragment nextFragment) {
//        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        if (!nextFragment.isAdded()) {    // 先判断是否被add过
//            fragmentTransaction.hide(fromFragment).add(R.id.framelayout, nextFragment).commit();
//        } else {
//            fragmentTransaction.hide(fromFragment).show(nextFragment).commit();
//        }

            if(nextFragment != fromFragment){
                tempFragment=nextFragment;

                if(nextFragment!=null){
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    //判断nextFragment是否添加
                    if(!nextFragment.isAdded()){
                        //隐藏当前Fragment
                        if(fromFragment!=null){
                            fragmentTransaction.hide(fromFragment);
                        }
                        fragmentTransaction.add(R.id.framelayout,nextFragment).commit();
//                        fragmentTransaction.show(nextFragment);
                    }else {
                        //隐藏当前Fragment
                        if(fromFragment!=null){
                            fragmentTransaction.hide(fromFragment);
                        }
                        fragmentTransaction.show(nextFragment).commit();
                    }
                }
            }
    }

    private BaseFragment getFragment(int positionFragment) {
        if(listFragment!=null&&listFragment.size()>0){
            return listFragment.get(positionFragment);
        }
        return null;
    }

    private void initData() {
        listFragment=new ArrayList<>();
        listFragment.add(new VoiceFragment());
        listFragment.add(new TranslateFragment());
        listFragment.add(new FileFragment());
        listFragment.add(new SetFragment());
    }

    public void applypermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //检查是否已经给了权限
            int checkpermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkpermission != PackageManager.PERMISSION_GRANTED) {//没有给权限
                Log.e("permission", "动态申请");
                //参数分别是当前活动，权限字符串数组，requestcode
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},
                        1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "录音已授权", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "录音拒绝授权", Toast.LENGTH_SHORT).show();
        }

    }
}
