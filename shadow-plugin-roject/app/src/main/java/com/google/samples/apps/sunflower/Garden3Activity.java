package com.google.samples.apps.sunflower;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.atar.bridge.BridgeExitInteface;
import com.atar.bridge.BridgeManager;
import com.atar.bridge.MessageEvent;
import com.atar.bridge.Test;
import com.google.samples.apps.sunflower.databinding.ActivityGarden3Binding;
import com.google.samples.manager.ActivityManager;
import com.google.samples.service.TestService;
import com.google.samples.viewmodels.TestViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class Garden3Activity extends AppCompatActivity {
    private ActivityGarden3Binding binding;

    private TestViewModel testViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().pushActivity(this);

        Test.getInstance().test(this);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_garden3);
        binding.txtBind.setText("整单打发的发发的发发的啊发");
        binding.txtBind2.setText("点击跳转到宿主");
        binding.txtBind2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Garden3Activity.this, BridgeManager.getInstance().getClassByHost("com.atar.tencentshadow.activity.SettingIPActivity"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.txtBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        testViewModel = ViewModelProviders.of(this).get(TestViewModel.class);
        binding.setViewModel(testViewModel);
        testViewModel.testString.setValue("dafdsf9090909090f点击大家揭开读卡减肥的司法局");

        testViewModel.testString.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e("Garden3Activity", "onChanged" + s);
            }
        });

        binding.setClickAction(new CLickAction());


//        BridgeManager.getInstance().register(this);

        EventBus.getDefault().register(this);


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

    public class CLickAction {

        public void startServicePlugin(View view) {
            //插件中启动 插件中 service
            startService(new Intent(Garden3Activity.this, TestService.class));
        }

        public void startBroadcastReceiverPlugin(View view) {
            try {
                Intent intent = new Intent("com.test.receiver.plugin");
                sendBroadcast(intent);
            } catch (Exception e) {

            }
        }

        public void postev(View view){
            EventBus.getDefault().post(new MessageEvent());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.e("Garden3Activity", "onMessageEvent");
    }
}