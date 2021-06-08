package com.atar.tencentshadow.configs;

import com.atar.tencentshadow.plugin.PluginBean;

import java.util.List;

/**
 * @author：atar
 * @date: 2020/11/19
 * @description:
 */
public class PluginConfigJson {
    private String pluginBaseOnHosetMinVersionName;//插件依赖的最小宿主插件框架版本 ，当插件中需要的插件框架版本高于，需要升级宿主程序，很多预置的插件运行环境是在宿主里面
    private List<PluginBean> pluginList; //插件列表

    public String getPluginBaseOnHosetMinVersionName() {
        return pluginBaseOnHosetMinVersionName;
    }

    public List<PluginBean> getPluginList() {
        return pluginList;
    }
}
