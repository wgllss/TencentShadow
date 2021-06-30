package com.google.samples.apps.sunflower;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.atar.bridge.BridgeInteface;
import com.atar.bridge.HostCallBack;
import com.google.samples.apps.sunflower.databinding.ActivityGarden2Binding;

public class Garden2Activity extends AppCompatActivity {
    private ActivityGarden2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_garden2);
        binding.txtBind.setText("整单打发的发发的发发的啊发");
        binding.txtBind2.setText("8888");
        binding.txtBind2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BridgeInteface bridgeInteface = new BridgeInteface();
                bridgeInteface.startActivity(Garden2Activity.this, new HostCallBack() {
                    @Override
                    public void onDo(Context context, Class cls) {
                        try {
                            Intent intent = new Intent(context, cls);
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
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Garden2Activity.this.finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出
                }
            }.start();
        }
    }
}