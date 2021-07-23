package com.atar.bridge;

/**
 * @author：atar
 * @date: 2021/7/23
 * @description:
 */
public interface BridgeEventBusInteface {

    void register(Object subscriber);

    boolean isRegistered(Object subscriber);

    void unregister(Object subscriber);

    void post(Object subscriber);
}
