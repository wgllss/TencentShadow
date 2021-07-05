package com.atar.bridge;

import android.content.Context;
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

    public static BridgeManager getInstance() {
        return bridgeManager;
    }

    public void initBridgeInteface(BridgeExitInteface bridgeInteface) {
        this.bridgeInteface = bridgeInteface;
    }

    public void startActivity(Context context, String className, BridgeEnterInteface bridgeEnterInteface) {
        try {
            Log.e(TAG, "className : " + className);
            Class cls = Class.forName(className);
            if (bridgeEnterInteface != null) {
                bridgeEnterInteface.startActivity(context, className, cls);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

}
