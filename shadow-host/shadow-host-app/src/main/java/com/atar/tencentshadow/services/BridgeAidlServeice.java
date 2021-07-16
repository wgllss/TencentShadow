package com.atar.tencentshadow.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.atar.bridge.BridgeAidlInterface;
import com.atar.tencentshadow.bridgeimp.ImplBridgeAidlInterfaceStub;

import androidx.annotation.Nullable;

/**
 * @author：atar
 * @date: 2021/7/16
 * @description:
 */
public class BridgeAidlServeice extends Service {
    private String TAG = BridgeAidlServeice.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "BridgeAidlServeice onCreate ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "BridgeAidlServeice onDestroy ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ImplBridgeAidlInterfaceStub(this);
    }
}
