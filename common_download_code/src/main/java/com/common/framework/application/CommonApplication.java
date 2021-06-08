package com.common.framework.application;

import android.app.Application;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;

import com.common.framework.plugins.Contans;
import com.common.framework.utils.ShowLog;

/**
 * @author：atar
 * @date: 2019/5/8
 * @description:
 */
public class CommonApplication {
    private static String TAG = CommonApplication.class.getSimpleName();

    protected static final boolean DEVELOPER_MODE = false;
    protected static Application mInstance;

    public static Context getContext() {
        return mInstance;
    }

    public static void initApplication(Application mApplication) {
        CommonApplication.mInstance = mApplication;
    }

    public static void mediaScannerConnectionScanFile(String path) {
        try {
            MediaScannerConnection.scanFile(mInstance, new String[]{path}, new String[]{"application/octet-stream"}, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(final String path, final Uri uri) {
                    //your file has been scanned!
                    ShowLog.e(TAG, "onScanCompleted:" + path);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
