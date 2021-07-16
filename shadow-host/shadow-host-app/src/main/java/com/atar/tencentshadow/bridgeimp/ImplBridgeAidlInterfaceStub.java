package com.atar.tencentshadow.bridgeimp;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.atar.bridge.BridgeAidlInterface;
import com.atar.tencentshadow.services.BridgeAidlServeice;
import com.common.framework.stack.ActivityManager;
import com.common.framework.utils.ServiceUtil;

/**
 * @author：atar
 * @date: 2021/7/16
 * @description:
 */
public class ImplBridgeAidlInterfaceStub extends BridgeAidlInterface.Stub {

    private String TAG = ImplBridgeAidlInterfaceStub.class.getSimpleName();

    private Context context;

    public ImplBridgeAidlInterfaceStub(Context context) {
        this.context = context;
    }

    @Override
    public boolean processBridge() throws RemoteException {
        try {
            Log.e(TAG,"BridgeAidlServeice BridgeAidlInterface processBridge-->");
            ActivityManager.getActivityManager().forceStopPackage(context.getPackageName(), context);
//            ActivityManager.getActivityManager().exitApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
