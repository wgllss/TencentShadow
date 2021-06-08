package com.common.framework.plugins;

import com.common.framework.appconfig.AppConfigModel;

/**
 * @author：atar
 * @date: 2021/6/5
 * @description:
 */
public class ServerConfig {
    public static final String Config_IP = AppConfigModel.getInstance().getString(Contans.HOST_KEY, "");
    public static final String config_host = "http://" + Config_IP + ":8080/";
    public static final String config_file_url = config_host + "assets/config/android_plugin_config.txt";
}
