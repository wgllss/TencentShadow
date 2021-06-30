package com.atar.tencentshadow.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.atar.tencentshadow.R;
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

import androidx.annotation.NonNull;

public class LoadingActivity extends Activity implements DownloadFileListener {

    private String TAG = LoadingActivity.class.getSimpleName();

    public static final int FROM_ID_START_ACTIVITY = 1001;
    public static final int FROM_ID_CALL_SERVICE = 1002;
    private Chandler chandler = new Chandler();

    private boolean isLoaded = false;
    private boolean isAutoSatrt = false;

    private class Chandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    isAutoSatrt = true;
                    if (!isLoaded) {
                        MainActivity.startMainActivity(LoadingActivity.this);
                    }
                    break;
                case 2:
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.welcome);

        ServiceUtil.startService(this, DownLoadSevice.class);
        DownloadPluginManager.getInstance()
                .setIsHasNewFileListener(this)
                .checkHasNewFileVersion();

        chandler.sendEmptyMessageDelayed(1, 5000);
    }

    @Override
    public void hasManagerFile(boolean from, boolean isNew, File manager) {
        InitApplication.onApplicationCreate(manager);
        //加载完 下载新的加载器 和插件
        DownLoadSevice.setGetServerConfig(this);
        StringBuilder sb = new StringBuilder();
        sb.append(from ? "本地有 " : "下载了 ");
        sb.append(isNew ? "新的" : "曾在的");
        sb.append(" 加载器 开始加载");
        ShowLog.e(TAG, sb.toString());
    }

    @Override
    public void hasPluginZip(boolean from, boolean isNew, File plugin) {
        StringBuilder sb = new StringBuilder();
        sb.append(from ? "本地有 " : "下载了 ");
        sb.append(isNew ? "新的" : "曾在的");
        sb.append(" 插件文件 ");
        ShowLog.e(TAG, sb.toString());
    }

    @Override
    public void hasNotManagerFile() {
        ShowLog.e(TAG, "本地 没有 加载器 文件");

    }

    @Override
    public void hasNotPluginFile() {
        ShowLog.e(TAG, "本地 没有 插件 文件");
    }

    @Override
    public void multDownLoadSuccess() {
        chandler.sendEmptyMessageDelayed(2, 1000);
        ShowLog.e(TAG, "加载器 插件一起下载完成");
    }

    @Override
    public void multFileLoadLoadSuccess() {
        ShowLog.e(TAG, "加载器 插件一起 准备 完成");
        isLoaded = true;
        if (isAutoSatrt) {
            return;
        }
        PluginManager pluginManager = InitApplication.getPluginManager();
        Bundle bundle = new Bundle();

        String pluginZipPath = Contans.strDownloadDir + Contans.str_u_current_plugin_name;
        ShowLog.e(TAG, pluginZipPath);
        bundle.putString("pluginZipPath", pluginZipPath);
        try {
            pluginManager.enter(LoadingActivity.this, FROM_ID_START_ACTIVITY, bundle, new EnterCallback() {
                @Override
                public void onShowLoadingView(View view) {
//                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                lp.gravity.
//                com.atar.tencentshadow.activity.LoadingActivity.this.addContentView(view, lp);//显示Manager传来的Loading页面
                }

                @Override
                public void onCloseLoadingView() {
//                        com.atar.tencentshadow.activity.MainActivity.this.setContentView(linearLayout);
                }

                @Override
                public void onEnterComplete() {
                    chandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ShowLog.e(TAG, "overridePendingTransition");
                            overridePendingTransition(R.anim.anim_alpha_121, R.anim.anim_alpha_121);
                        }
                    });
                }
            });
        } catch (Exception e) {
            MainActivity.startMainActivity(LoadingActivity.this);
        }
    }
}