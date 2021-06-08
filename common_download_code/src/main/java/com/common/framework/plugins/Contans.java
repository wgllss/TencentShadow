package com.common.framework.plugins;

import android.os.Environment;

/**
 * @author：atar
 * @date: 2021/6/5
 * @description:
 */
public class Contans {

    //存放host 地址key
    public static final String HOST_KEY = "HOST_KEY";
    //保存配置文件总版本key
    public static final String SAVE_CONFIG_FILE_VERSION_KEY = "SAVE_CONFIG_FILE_VERSION_KEY";
    //保存配置文件内容key
    public static final String SAVE_CONFIG_FILE_CONTENT_KEY = "SAVE_CONFIG_FILE_CONTENT_KEY";
    //下载文件目录
    public static final String strDownloadDir = Environment.getExternalStorageDirectory() + "/.cache/.download_plugin_apk/";

    //下载到本地加载器 文件名称
    public static final String str_downloadManager_name = "shadow-manager-debug.apk";
    //下载到本地插件 文件名称
    public static final String str_downloadPlugin_name = "plugin-debug.zip";

    //实际使用到 下载到本地加载器 文件名称
    public static final String str_u_downloadManager_name = "shadow-manager-debug0.apk";
    //实际使用到 下载到本地插件 文件名称
    public static final String str_u_downloadPlugin_name = "plugin-debug0.zip";

    //当前使用的插件本地 文件
    public static String str_u_current_plugin_name = str_u_downloadPlugin_name;

    //下载管理器标志
    public static final int DOWNLOAD_MANAGER = 987789;
    //下载插件标志
    public static final int DOWNLOAD_PLUGIN = 987799;


}
