package com.atar.host_lib;

import android.content.Context;
import android.content.Intent;

/**
 * @author：atar
 * @date: 2021/6/21
 * @description:
 */
public class BridgeInteface {

    public static BridgeInteface newInstance() {
        return new BridgeInteface();
    }

    public void startActivity(Context context) {
        try{
            Class cls = Class.forName("com.atar.tencentshadow.activity.SettingIPActivity");
            Intent intent = new Intent(context, cls);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
