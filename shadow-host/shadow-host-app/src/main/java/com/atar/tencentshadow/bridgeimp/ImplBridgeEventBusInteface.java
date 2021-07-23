package com.atar.tencentshadow.bridgeimp;

import com.atar.bridge.BridgeEventBusInteface;

//import org.greenrobot.eventbus.EventBus;

/**
 * @author：atar
 * @date: 2021/7/23
 * @description:
 */
public class ImplBridgeEventBusInteface implements BridgeEventBusInteface {


    @Override
    public void register(Object subscriber) {
        if(!isRegistered(subscriber)){
//            EventBus.getDefault().register(subscriber);
        }

    }

    @Override
    public boolean isRegistered(Object subscriber) {
//       return EventBus.getDefault().isRegistered(subscriber);
        return false;
    }

    @Override
    public void unregister(Object subscriber) {
//        EventBus.getDefault().unregister(subscriber);
    }

    @Override
    public void post(Object subscriber) {
//        EventBus.getDefault().post(subscriber);
    }
}
