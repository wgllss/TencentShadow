package com.atar.bridge;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author：atar
 * @date: 2021/6/27
 * @description:
 */
public class BridgeInteface {

    public void startActivity(Context context, HostCallBack hostCallBack) {
        try {
            Log.e("TAG", "ioipoipi");
            Class cls = Class.forName("com.atar.tencentshadow.activity.SettingIPActivity");
            if (hostCallBack != null) {
                hostCallBack.onDo(context,cls);
            }
//            Intent intent = new Intent(context, cls);
////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
