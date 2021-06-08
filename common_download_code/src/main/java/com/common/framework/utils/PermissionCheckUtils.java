package com.common.framework.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * ****************************************************************************************************************************************************************************
 * 权限检测工具
 *
 * @author:Atar
 * @createTime: 2018/8/10 下午1:12
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description :
 * **************************************************************************************************************************************************************************
 */
public class PermissionCheckUtils {

    public static boolean hasPermission(Activity activity, int permissionRequestCode, String... manifestPermission) {
        int flag = 0;
        try {
            if (activity != null && manifestPermission != null && manifestPermission.length > 0) {
                for (int i = 0; i < manifestPermission.length; i++) {
                    if (ContextCompat.checkSelfPermission(activity, manifestPermission[i]) != PackageManager.PERMISSION_GRANTED) {
                        flag++;
                    }
                }
            }
            if (flag > 0) {
                ActivityCompat.requestPermissions(activity, manifestPermission, permissionRequestCode);
            }
        } catch (Exception e) {

        }
        return flag == 0;
    }
}
