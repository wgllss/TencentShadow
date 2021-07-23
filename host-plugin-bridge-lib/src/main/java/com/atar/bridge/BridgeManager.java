package com.atar.bridge;

import android.util.Log;

/**
 * @author：atar
 * @date: 2021/6/30
 * @description:
 */
public class BridgeManager {

    private String TAG = BridgeManager.class.getSimpleName();

    private static BridgeManager bridgeManager = new BridgeManager();
    private BridgeExitInteface bridgeInteface;
    private BridgeEventBusInteface bridgeEventBusInteface;

    public static BridgeManager getInstance() {
        return bridgeManager;
    }

    public BridgeManager initBridgeInteface(BridgeExitInteface bridgeInteface) {
        this.bridgeInteface = bridgeInteface;
        return this;
    }

    public BridgeManager addBridgeEventBusInteface(BridgeEventBusInteface bridgeEventBusInteface) {
        this.bridgeEventBusInteface = bridgeEventBusInteface;
        return this;
    }

    //获取宿主里面的类
    public Class getClassByHost(String className) {
        try {
            Log.e(TAG, "className : " + className);
            Class cls = Class.forName(className);
            return cls;
        } catch (Exception e) {
            return null;
        }
    }

    public void exit(BridgeExitInteface bridgeInteface) {
        try {
            if (this.bridgeInteface != null) {
                this.bridgeInteface.exit();
            }
            if (bridgeInteface != null) {
                bridgeInteface.exit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(Object subscriber) {
        if (bridgeEventBusInteface != null) {
            bridgeEventBusInteface.register(subscriber);
        }
    }

    public void isRegistered(Object subscriber) {
        if (bridgeEventBusInteface != null) {
            bridgeEventBusInteface.isRegistered(subscriber);
        }
    }

    public void unregister(Object subscriber) {
        if (bridgeEventBusInteface != null) {
            bridgeEventBusInteface.unregister(subscriber);
        }
    }

    public void post(Object subscriber) {
        if (bridgeEventBusInteface != null) {
            bridgeEventBusInteface.post(subscriber);
        }
    }

}
