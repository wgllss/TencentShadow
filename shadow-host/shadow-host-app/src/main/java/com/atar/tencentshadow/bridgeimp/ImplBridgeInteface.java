package com.atar.tencentshadow.bridgeimp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.atar.bridge.BridgeAidlInterface;
import com.atar.bridge.BridgeExitInteface;
import com.atar.tencentshadow.receiver.HostReceiver;

/**
 * @author：atar
 * @date: 2021/6/30
 * @description:
 */
public class ImplBridgeInteface implements BridgeExitInteface {
    private String TAG = ImplBridgeInteface.class.getSimpleName();

    private Context context;

    public ImplBridgeInteface(Context context) {
        this.context = context;
    }

    @Override
    public void exit() {
        try {
            context.sendBroadcast(new Intent(HostReceiver.action_exit));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
