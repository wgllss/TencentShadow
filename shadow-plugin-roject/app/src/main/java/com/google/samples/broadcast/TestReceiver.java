package com.google.samples.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author：atar
 * @date: 2021/7/6
 * @description:
 */
public class TestReceiver extends BroadcastReceiver {
    private String TAG = TestReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            Log.e(TAG,"--action--->" + action);
        } catch (Exception e) {

        }
    }
}
