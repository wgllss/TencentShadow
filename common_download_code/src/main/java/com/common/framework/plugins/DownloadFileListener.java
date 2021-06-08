package com.common.framework.plugins;

import java.io.File;

/**
 * @author：atar
 * @date: 2021/6/5
 * @description: 每次启动加载插件前检查是否有新的
 */
public interface DownloadFileListener {

    /**
     * 有新的加载器
     *
     * @param from    true:从本地检查出来，false:刚下载完
     * @param manager
     */
    void hasManagerFile(boolean from, boolean isNew, File manager);


    /**
     * 有新的插件
     *
     * @param from   true:从本地检查出来，false:刚下载完
     * @param plugin
     */
    void hasPluginZip(boolean from, boolean isNew, File plugin);

    void hasNotManagerFile();

    void hasNotPluginFile();

    //多个文件需要一起使用，都下载成功时候
    void multDownLoadSuccess();

    void multFileLoadLoadSuccess();
}
