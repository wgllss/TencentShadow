package com.atar.bridge;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

/**
 * @author：atar
 * @date: 2021/7/5
 * @description:
 */
public class Test {
    private String TAG = Test.class.getSimpleName();
    private static Test intance = new Test();

    public static Test getInstance() {
        return intance;
    }

    public void test(FragmentActivity activity) {
        Log.e(TAG, "Test idfaf kl; l ");
    }
}
