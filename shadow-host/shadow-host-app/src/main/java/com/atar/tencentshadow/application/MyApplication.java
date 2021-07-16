package com.atar.tencentshadow.application;

import android.app.Application;
import android.content.IntentFilter;

import com.atar.bridge.BridgeManager;
import com.atar.tencentshadow.BuildConfig;
import com.atar.tencentshadow.bridgeimp.ImplBridgeInteface;
import com.atar.tencentshadow.receiver.HostReceiver;
import com.common.framework.application.CommonApplication;
import com.tencent.shadow.sample.introduce_shadow_lib.InitApplication;

public class MyApplication extends Application {

    public static Application application = null;
    public static boolean isLog = BuildConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        CommonApplication.initApplication(this);// 初始化全局Context
        InitApplication.onApplicationCreate(this);
        BridgeManager.getInstance().initBridgeInteface(new ImplBridgeInteface(this));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HostReceiver.action_exit);
        HostReceiver hostReceiver = new HostReceiver();
        registerReceiver(hostReceiver, intentFilter);
    }

}
