package com.common.framework.plugins;

import android.text.TextUtils;

import java.util.Map;

/**
 * @author：atar
 * @date: 2021/6/5
 * @description:
 */
public class DownloadPluginMoudle {
    private String versionName; //版本号名称
    private String updateMinVersionName;//版本更新的最小版本号
    //注：pluginZip_url ，managerLoder_url  只更新其中之一，另一个不填
    private String managerLoder_url;//插件加载下载路劲
    private String pluginZip_url;//插件下载url
    private int needDownLoadFileNum = 0;//需要下载的 文件数量个数 可能 2个，可能 加载器和插件其中一个

    public String getVersionName() {
        return versionName;
    }

    public String getUpdateMinVersionName() {
        return updateMinVersionName;
    }

    public String getManagerLoder_url() {
        return managerLoder_url;
    }

    public String getPluginZip_url() {
        return pluginZip_url;
    }

    public int getNeedDownLoadFileNum() {
        if (!TextUtils.isEmpty(managerLoder_url)) {
            needDownLoadFileNum++;
        }
        if (!TextUtils.isEmpty(pluginZip_url)) {
            needDownLoadFileNum++;
        }
        return needDownLoadFileNum;
    }
}
