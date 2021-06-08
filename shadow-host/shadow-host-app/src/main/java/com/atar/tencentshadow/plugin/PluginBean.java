package com.atar.tencentshadow.plugin;

/**
 * @author：atar
 * @date: 2020/11/19
 * @description:
 */
public class PluginBean {
    private String pluginPackgeName; //插件模块包名
    private String pluginUrl; //插件模块路劲
    private String pluginVersion;//插件版本
    private String pluginReplaceMinVersion;
    private String pluginShowName;//插件模块显示名字

    public String getPluginPackgeName() {
        return pluginPackgeName;
    }

    public String getPluginUrl() {
        return pluginUrl;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public String getPluginShowName() {
        return pluginShowName;
    }

    public String getPluginReplaceMinVersion() {
        return pluginReplaceMinVersion;
    }
}
