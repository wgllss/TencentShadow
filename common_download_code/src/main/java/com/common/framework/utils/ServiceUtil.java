package com.common.framework.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class ServiceUtil {
    /**
     * 判断某个服务是否存在
     *
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (serviceList == null || !(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;

            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    public static void startService(Context packageContext, Class<?> cls) {
        try {
            if (!isServiceExisted(packageContext, cls.getName())) {
                Intent intent = new Intent(packageContext, cls);
                packageContext.startService(intent);
            }
        } catch (Exception e) {
        }
    }

    public static void stopService(Context packageContext, Class<?> cls) {
        try {
            if (!isServiceExisted(packageContext, cls.getName())) {
                Intent intent = new Intent(packageContext, cls);
                packageContext.stopService(intent);
            }
        } catch (Exception e) {
        }
    }

}
