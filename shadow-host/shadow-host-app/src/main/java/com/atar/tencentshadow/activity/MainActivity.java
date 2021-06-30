package com.atar.tencentshadow.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atar.tencentshadow.configs.Config;
import com.common.framework.plugins.Contans;
import com.common.framework.plugins.DownloadFileListener;
import com.common.framework.plugins.DownloadPluginManager;
import com.common.framework.services.DownLoadSevice;
import com.common.framework.utils.ServiceUtil;
import com.common.framework.utils.ShowLog;
import com.tencent.shadow.dynamic.host.EnterCallback;
import com.tencent.shadow.dynamic.host.PluginManager;
import com.tencent.shadow.sample.introduce_shadow_lib.InitApplication;

import java.io.File;

public class MainActivity extends Activity {

    private String TAG = MainActivity.class.getSimpleName();

    public static final int FROM_ID_START_ACTIVITY = 1001;
    public static final int FROM_ID_CALL_SERVICE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        textView.setText("宿主App");


        Button settingButton = new Button(this);
        settingButton.setText("设置IP");
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingIPActivity.class));
            }
        });
        linearLayout.addView(settingButton);

//        Button button = new Button(this);
//        button.setText("启动插件");
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                v.setEnabled(false);//防止点击重入
//
//                PluginManager pluginManager = InitApplication.getPluginManager();
//                Bundle bundle = new Bundle();
//
//                String pluginZipPath = Contans.strDownloadDir + Contans.str_u_current_plugin_name;
//                ShowLog.e(TAG, pluginZipPath);
//                bundle.putString("pluginZipPath", pluginZipPath);
//                pluginManager.enter(com.atar.tencentshadow.activity.MainActivity.this, FROM_ID_START_ACTIVITY, bundle, new EnterCallback() {
//                    @Override
//                    public void onShowLoadingView(View view) {
//                        com.atar.tencentshadow.activity.MainActivity.this.setContentView(view);//显示Manager传来的Loading页面
//                    }
//
//                    @Override
//                    public void onCloseLoadingView() {
//                        com.atar.tencentshadow.activity.MainActivity.this.setContentView(linearLayout);
//                    }
//
//                    @Override
//                    public void onEnterComplete() {
//                        v.setEnabled(true);
//                    }
//                });
//            }
//        });
//
//        linearLayout.addView(textView);
//        linearLayout.addView(button);

        setContentView(linearLayout);
    }

    public static void startMainActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }
}