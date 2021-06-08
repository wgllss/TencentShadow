package com.atar.tencentshadow.configs;

import android.os.Environment;

import com.common.framework.appconfig.AppConfigModel;

/**
 * @author：atar
 * @date: 2020/11/19
 * @description:
 */
public class Config {
    //存储ip KEy
    public static final String SAVE_HOST_IP_KEY = "SAVE_HOST_IP_KEY";


    //保存配置文件总版本key
    public static final String SAVE_CONFIG_FILE_VERSION_KEY = "SAVE_CONFIG_FILE_VERSION_KEY";
    //保存配置文件内容key
    public static final String SAVE_CONFIG_FILE_CONTENT_KEY = "SAVE_CONFIG_FILE_CONTENT_KEY";
    //保存引导图版本key
    public static final String SAVE_LOAD_IMAGE_VERSION_KEY = "SAVE_LOAD_IMAGE_VERSION_KEY";
    //保存引导图内容
    public static final String SAVE_LOAD_IMAGE_CONTENT_KEY = "SAVE_LOAD_IMAGE_CONTENT_KEY";
    //报损插件版本KEY
    public static final String SAVAE_PLUGIN_VERSION_NAME_KEY = "SAVAE_PLUGIN_VERSION_NAME_KEY";

    private static String ip = AppConfigModel.getInstance().getString(Config.SAVE_HOST_IP_KEY, "192.168.96.84");

    public static String host = "http://" + ip + ":8080/";
    //下载配置文件 地址
    public static String config_file_url = host + "assets/config/android_app_config_json.txt";
    //下载皮肤地址
    public static final String download_skin_url = host + "assets/apk/down_plugin/release/app_skin_blue-release.apk";

    public static final String strDownloadDir = Environment.getExternalStorageDirectory() + "/.cache/.download_plugin_apk/";
    public static final String strDownloadManagerDir = Environment.getExternalStorageDirectory() + "/.cache/.download_plugin_apk/shadow-manager-debug.apk";

}
