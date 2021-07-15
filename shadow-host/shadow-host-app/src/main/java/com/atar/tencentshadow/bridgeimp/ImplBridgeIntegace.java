package com.atar.tencentshadow.bridgeimp;

import android.content.Context;
import android.content.Intent;

import com.atar.bridge.BridgeExitInteface;
import com.atar.tencentshadow.receiver.HostReceiver;
import com.common.framework.stack.ActivityManager;

/**
 * @author：atar
 * @date: 2021/6/30
 * @description:
 */
public class ImplBridgeIntegace implements BridgeExitInteface {
    private String TAG = ImplBridgeIntegace.class.getSimpleName();

    private Context context;

    public ImplBridgeIntegace(Context context) {
        this.context = context;
    }

    @Override
    public void exit() {
        context.sendBroadcast(new Intent(HostReceiver.action_exit));
    }
}
