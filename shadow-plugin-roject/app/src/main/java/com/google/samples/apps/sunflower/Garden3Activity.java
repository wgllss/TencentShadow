package com.google.samples.apps.sunflower;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.atar.bridge.BridgeEnterInteface;
import com.atar.bridge.BridgeExitInteface;
import com.atar.bridge.BridgeManager;
import com.atar.bridge.Test;
import com.google.samples.apps.sunflower.databinding.ActivityGarden3Binding;
import com.google.samples.manager.ActivityManager;
import com.google.samples.service.TestService;
import com.google.samples.viewmodels.TestViewModel;

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

                BridgeManager.getInstance().startActivity(Garden3Activity.this, "com.atar.tencentshadow.activity.SettingIPActivity", new BridgeEnterInteface() {
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

    }
}