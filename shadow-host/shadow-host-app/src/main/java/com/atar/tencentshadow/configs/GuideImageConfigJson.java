package com.atar.tencentshadow.configs;

import java.util.List;

/**
 * @author：atar
 * @date: 2020/11/19
 * @description:
 */
public class GuideImageConfigJson {
    private String guideImageVersion; //开机引导图版本
    private String guideImageMinVersionName;//如果有内容需要强制替换，：小于多少版本需要强制全部替换
    private List<String> list;

    public String getGuideImageVersion() {
        return guideImageVersion;
    }

    public String getGuideImageMinVersionName() {
        return guideImageMinVersionName;
    }

    public List<String> getList() {
        return list;
    }
}
