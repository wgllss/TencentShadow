package com.atar.tencentshadow.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.common.framework.Threadpool.ThreadPoolTool;
import com.common.framework.stack.ActivityManager;

/**
 * @author：atar
 * @date: 2021/7/15
 * @description:
 */
public class HostReceiver extends BroadcastReceiver {

    public static String action_exit = "com.atar.exit.host.action";

    private String TAG = HostReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        ThreadPoolTool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String action = intent.getAction();
                    Log.e(TAG, "--action--->" + action);
                    if (action_exit.equals(action)) {
                        ActivityManager.getActivityManager().forceStopPackage(context.getPackageName(), context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
