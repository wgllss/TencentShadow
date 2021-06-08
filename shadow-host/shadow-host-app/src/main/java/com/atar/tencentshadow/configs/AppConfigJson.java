package com.atar.tencentshadow.configs;

/**
 * @author：atar
 * @date: 2020/11/19
 * @description:
 */
public class AppConfigJson {
    private String configVersionName;//配置文件版本,理论上配置文件改任何东西，该版本号需要向上升
    private SkinConfigJson skinconfigJson; //皮肤信息
    private PluginConfigJson pluginConfigJson; //插件信息
    private GuideImageConfigJson guideImageConfigJson;//开机引导图信息
    private HomeConfigJson homeConfigJson;

    public String getConfigVersionName() {
        return configVersionName;
    }

    public SkinConfigJson getSkinconfigJson() {
        return skinconfigJson;
    }

    public PluginConfigJson getPluginConfigJson() {
        return pluginConfigJson;
    }

    public GuideImageConfigJson getGuideImageConfigJson() {
        return guideImageConfigJson;
    }

    public HomeConfigJson getHomeConfigJson() {
        return homeConfigJson;
    }
}
