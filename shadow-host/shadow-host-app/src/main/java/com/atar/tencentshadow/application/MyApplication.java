package com.atar.tencentshadow.application;

import android.app.Application;

import com.atar.tencentshadow.BuildConfig;
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
    }
}
