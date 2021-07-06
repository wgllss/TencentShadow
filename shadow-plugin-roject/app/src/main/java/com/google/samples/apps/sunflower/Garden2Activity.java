package com.google.samples.apps.sunflower;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.atar.bridge.BridgeEnterInteface;
import com.atar.bridge.BridgeExitInteface;
import com.atar.bridge.BridgeManager;
import com.atar.bridge.Test;
import com.google.samples.apps.sunflower.databinding.ActivityGarden2Binding;
import com.google.samples.broadcast.TestReceiver;
import com.google.samples.manager.ActivityManager;

public class Garden2Activity extends AppCompatActivity {
    private ActivityGarden2Binding binding;

    private TestReceiver testReceiver = new TestReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().pushActivity(this);

        Test.getInstance().test(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_garden2);
        binding.txtBind.setText("整单打发的发发的发发的啊发");
        binding.txtBind2.setText("点击跳转到宿主");
        binding.txtBind2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BridgeManager.getInstance().startActivity(Garden2Activity.this, "com.atar.tencentshadow.activity.SettingIPActivity", new BridgeEnterInteface() {
                    @Override
                    public void startActivity(Context context, String s, Class aClass) {
                        try {
                            Intent intent = new Intent(context, aClass);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//                    onBackPressed();
            }
        });

        binding.txtBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.txtBind3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),Garden3Activity.class));
            }
        });



        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.test.receiver.plugin");
        registerReceiver(testReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(testReceiver!=null){
            unregisterReceiver(testReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private long exitTime = 0;

    public void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            BridgeManager.getInstance().exit(new BridgeExitInteface() {
                @Override
                public void exit() {
                    ActivityManager.getActivityManager().exitApplication();
                }
            });
        }
    }
}