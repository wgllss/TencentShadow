package com.atar.bridge;

import android.content.Context;

/**
 * @author：atar
 * @date: 2021/6/27
 * @description:
 */
public interface BridgeEnterInteface {

    void startActivity(Context context, String className,  Class cls);
}
