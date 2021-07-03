package com.atar.tencentshadow.bridgeimp;

import com.atar.bridge.BridgeExitInteface;
import com.common.framework.stack.ActivityManager;

/**
 * @author：atar
 * @date: 2021/6/30
 * @description:
 */
public class ImplBridgeIntegace implements BridgeExitInteface {
    private String TAG = ImplBridgeIntegace.class.getSimpleName();

    @Override
    public void exit() {
        ActivityManager.getActivityManager().exitApplication();
    }
}
