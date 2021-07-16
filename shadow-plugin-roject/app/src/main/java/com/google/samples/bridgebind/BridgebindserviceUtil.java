package com.google.samples.bridgebind;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.atar.bridge.BridgeAidlInterface;

/**
 * @author：atar
 * @date: 2021/7/16
 * @description:
 */
public class BridgebindserviceUtil implements ServiceConnection {
    private String TAG = BridgebindserviceUtil.class.getSimpleName();

    private boolean isonServiceConnected;
    private BridgeAidlInterface bridgeAidlInterface;

    private static BridgebindserviceUtil instance = new BridgebindserviceUtil();

    public static BridgebindserviceUtil getInstance() {
        return instance;
    }

    public void onCreate(Context context){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.atar.tencentshadow", "com.atar.tencentshadow.services.BridgeAidlServeice"));
        context.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }


    public void processBridge(Context context) {
        try {
            if (isonServiceConnected) {
                if (bridgeAidlInterface != null) {
                    bridgeAidlInterface.processBridge();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.e(TAG, "onServiceConnected  ");
        try {
            bridgeAidlInterface = BridgeAidlInterface.Stub.asInterface(service);
            if (!isonServiceConnected && bridgeAidlInterface != null) {
                bridgeAidlInterface.processBridge();
            }
            isonServiceConnected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.e(TAG, "onServiceDisconnected  ");
        isonServiceConnected = false;
    }
}
