package com.atar.tencentshadow.configs;

/**
 * @author：atar
 * @date: 2020/11/19
 * @description:
 */
public class SkinBean {
    private String skinDownloadUrl;//皮肤下载路劲
    private String skinShowName;//皮肤显示显示名称
    private String skinLoadName;//皮肤显示名称
    private String skinColor;//皮肤颜色
    private String skinDownLoadName;//下载到本地皮肤文件名称
    private int skinType; //比如 0 :白天 1:晚上 2 :红色 3:绿色等等

    public String getSkinDownloadUrl() {
        return skinDownloadUrl;
    }

    public String getSkinShowName() {
        return skinShowName;
    }

    public String getSkinLoadName() {
        return skinLoadName;
    }

    public String getSkinColor() {
        return skinColor;
    }

    public String getSkinDownLoadName() {
        return skinDownLoadName;
    }

    public int getSkinType() {
        return skinType;
    }
}
