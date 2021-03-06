package com.atar.tencentshadow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.atar.bridge.Test;
import com.atar.tencentshadow.R;
import com.common.framework.plugins.Contans;
import com.common.framework.plugins.DownloadFileListener;
import com.common.framework.plugins.DownloadPluginManager;
import com.common.framework.services.DownLoadSevice;
import com.common.framework.stack.ActivityManager;
import com.common.framework.utils.ServiceUtil;
import com.common.framework.utils.ShowLog;
import com.tencent.shadow.dynamic.host.EnterCallback;
import com.tencent.shadow.dynamic.host.PluginManager;
import com.tencent.shadow.sample.introduce_shadow_lib.InitApplication;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class LoadingActivity extends FragmentActivity implements DownloadFileListener {

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
        if (!this.isTaskRoot()) { // ?????????Activity???????????????????????????Activity????????????????????????????????????????????????????????? //??????????????????launcher Activity???????????????????????????return???
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;// finish()?????????????????????????????????????????????????????????logCat????????????return???????????????exception
            }
        }

        isLoaded = false;
        isAutoSatrt = false;
        ActivityManager.getActivityManager().pushActivity(this);
//        setContentView(R.layout.welcome);

        ServiceUtil.startService(this, DownLoadSevice.class);
        DownloadPluginManager.getInstance()
                .init()
                .setIsHasNewFileListener(this)
                .checkHasNewFileVersion();

        chandler.sendEmptyMessageDelayed(1, 5000);

        Test.getInstance().test(this);
    }

    @Override
    public void hasManagerFile(boolean from, boolean isNew, File manager) {
        InitApplication.onApplicationCreate(manager);
        //????????? ????????????????????? ?????????
        DownLoadSevice.setGetServerConfig(this);
        StringBuilder sb = new StringBuilder();
        sb.append(from ? "????????? " : "????????? ");
        sb.append(isNew ? "??????" : "?????????");
        sb.append(" ????????? ????????????");
        ShowLog.e(TAG, sb.toString());
    }

    @Override
    public void hasPluginZip(boolean from, boolean isNew, File plugin) {
        StringBuilder sb = new StringBuilder();
        sb.append(from ? "????????? " : "????????? ");
        sb.append(isNew ? "??????" : "?????????");
        sb.append(" ???????????? ");
        ShowLog.e(TAG, sb.toString());
    }

    @Override
    public void hasNotManagerFile() {
        ShowLog.e(TAG, "?????? ?????? ????????? ??????");

    }

    @Override
    public void hasNotPluginFile() {
        ShowLog.e(TAG, "?????? ?????? ?????? ??????");
    }

    @Override
    public void multDownLoadSuccess() {
        chandler.sendEmptyMessageDelayed(2, 1000);
        ShowLog.e(TAG, "????????? ????????????????????????");
    }

    @Override
    public void multFileLoadLoadSuccess() {
        ShowLog.e(TAG, "????????? ???????????? ?????? ??????");
        isLoaded = true;
        if (isAutoSatrt) {
            return;
        }
        PluginManager pluginManager = InitApplication.getPluginManager();
        Bundle bundle = new Bundle();

        String pluginZipPath = Contans.strDownloadDir + Contans.str_u_current_plugin_name;
        ShowLog.e(TAG, pluginZipPath);
        bundle.putString("pluginZipPath", pluginZipPath);
        bundle.putString("KEY_ACTIVITY_CLASSNAME", "com.google.samples.apps.sunflower.Garden2Activity");
        try {
            pluginManager.enter(LoadingActivity.this, FROM_ID_START_ACTIVITY, bundle, new EnterCallback() {
                @Override
                public void onShowLoadingView(View view) {
//                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                lp.gravity= Gravity.CENTER;
//                com.atar.tencentshadow.activity.LoadingActivity.this.addContentView(view, lp);//??????Manager?????????Loading??????
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
//                            ActivityManager.getActivityManager().finishActivity(LoadingActivity.class);
                        }
                    });
                }
            });
        } catch (Exception e) {
            MainActivity.startMainActivity(LoadingActivity.this);
        }
    }
}