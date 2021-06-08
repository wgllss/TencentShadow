package com.atar.tencentshadow.configs;

/**
 * @author：atar
 * @date: 2020/11/24
 * @description:
 */
public class HomeConfigJson {
    private int type;//跳转类型 0 :宿主 跳转宿主 1:宿主跳转到插件 2：插件跳转到宿主 3:插件到插件
    private String className;//跳转页面类名，全路劲
    private String optionJson;
    private String pluginPath;//如果是跳转到插件,需要设置插件本地路劲

    public int getType() {
        return type;
    }

    public String getClassName() {
        return className;
    }

    public String getOptionJson() {
        return optionJson;
    }

    public String getPluginPath() {
        return pluginPath;
    }
}
