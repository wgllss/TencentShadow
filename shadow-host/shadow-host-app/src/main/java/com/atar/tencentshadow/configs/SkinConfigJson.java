package com.atar.tencentshadow.configs;

import java.util.List;

/**
 * @author：atar
 * @date: 2020/11/19
 * @description:
 */
public class SkinConfigJson {
    private boolean isLoadApkSkin;//是否应用加载外部apk
    private String skinVersionName;//皮肤版本
    private String skinReplaceMinVersion;//皮肤需要强制替换的最小版本 (小于该版本下必须强制下载新的，不含等于)
    private List<SkinTypeBean> skinTypes; //皮肤类型
    private List<SkinBean> all_skin;

    public String getSkinVersionName() {
        return skinVersionName;
    }

    public String getSkinReplaceMinVersion() {
        return skinReplaceMinVersion;
    }

    public List<SkinBean> getAll_skin() {
        return all_skin;
    }

    public boolean isLoadApkSkin() {
        return isLoadApkSkin;
    }

    public List<SkinTypeBean> getSkinTypes() {
        return skinTypes;
    }
}
