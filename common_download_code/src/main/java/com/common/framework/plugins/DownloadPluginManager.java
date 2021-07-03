package com.common.framework.plugins;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;

import com.common.framework.Threadpool.ThreadPoolTool;
import com.common.framework.appconfig.AppConfigModel;
import com.common.framework.application.CommonApplication;
import com.common.framework.common.CommonHandler;
import com.common.framework.http.HttpWriteFileQuest;
import com.common.framework.services.DownLoadSevice;
import com.common.framework.utils.FileUtils;
import com.common.framework.utils.ShowLog;
import com.google.gson.Gson;
import com.zz.common.BuildConfig;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author：atar
 * @date: 2021/6/5
 * @description: 下载插件管理器 功能包括控制下载，控制加载本地插件文件的选择
 */
public class DownloadPluginManager {

    private static final String TAG = DownloadPluginManager.class.getSimpleName();

    private DownloadFileListener isHasNewFileListener;

    //多个文件下载成功标识
    private int downLoadPluginNum = 0;
    //加载器和 插件选择 都已经完成 标识
    private int loadFileCount = 0;

    private ExecutorService newSingleThreadExecutor;//= Executors.newSingleThreadExecutor();

    public static DownloadPluginManager instance = new DownloadPluginManager();
    private Gson gson = new Gson();

    public static DownloadPluginManager getInstance() {
        return instance;
    }

    public  DownloadPluginManager init(){
        downLoadPluginNum = 0;
        loadFileCount = 0;
        return instance;
    }

    public DownloadPluginManager setIsHasNewFileListener(DownloadFileListener isHasNewFileListener) {
        this.isHasNewFileListener = isHasNewFileListener;
        return instance;
    }


    //有新的加载器
    private void hasManagerFile(boolean from, boolean isNew, File manager) {
        if (newSingleThreadExecutor == null) {
            newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        newSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isHasNewFileListener == null) {
                        return;
                    }
                    if (!from || !isNew) {
                        isHasNewFileListener.hasManagerFile(from, isNew, manager);
                    } else {
                        File fileDefault = new File(Contans.strDownloadDir + Contans.str_u_downloadManager_name);
                        String destFilePath = fileDefault.getAbsolutePath();
                        if (fileDefault.exists()) {
                            fileDefault.delete();
                        }
                        boolean isCopySuccess = FileUtils.copyFile(Contans.strDownloadDir + Contans.str_downloadManager_name, destFilePath);
                        if (isCopySuccess) {
                            isHasNewFileListener.hasManagerFile(from, isNew, new File(Contans.strDownloadDir + Contans.str_u_downloadManager_name));
                            manager.delete();
                        }
                        CommonApplication.mediaScannerConnectionScanFile(Contans.strDownloadDir);
                    }
                    setSingleFileLoadLoadSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //有新的插件结果
    private void hasPluginZip(boolean from, boolean isNew, File plugin) {
        if (newSingleThreadExecutor == null) {
            newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        newSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!from || !isNew) {
                        if (isHasNewFileListener != null) {
                            isHasNewFileListener.hasPluginZip(from, isNew, plugin);
                        }
                    } else {
                        File fileDefault = new File(Contans.strDownloadDir + Contans.str_u_downloadPlugin_name);
                        String destFilePath = fileDefault.getAbsolutePath();
                        if (fileDefault.exists()) {
                            fileDefault.delete();
                        }
                        boolean isCopySuccess = FileUtils.copyFile(Contans.strDownloadDir + Contans.str_downloadPlugin_name, destFilePath);
                        if (isCopySuccess) {
                            plugin.delete();
                        }
                        CommonApplication.mediaScannerConnectionScanFile(Contans.strDownloadDir);
                    }
                    setSingleFileLoadLoadSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //本地没有插件 管理加载器
    private void hasNotManagerFile() {
        if (newSingleThreadExecutor == null) {
            newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        newSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String configJson = AppConfigModel.getInstance().getString(Contans.SAVE_CONFIG_FILE_CONTENT_KEY, "");
                    if (TextUtils.isEmpty(configJson)) {
                        return;
                    }
                    Gson gson = new Gson();
                    DownloadPluginMoudle downloadPluginMoudle = gson.fromJson(configJson, DownloadPluginMoudle.class);
                    if (downloadPluginMoudle == null) {
                        return;
                    }
                    downloaNewPluginAndManager(CommonApplication.getContext(), downloadPluginMoudle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (isHasNewFileListener != null) {
            isHasNewFileListener.hasNotManagerFile();
        }
    }

    //本地没有插件 插件
    private void hasNotPluginFile() {
        if (isHasNewFileListener != null) {
            isHasNewFileListener.hasNotPluginFile();
        }
    }

    //多个文件需要一起使用，都下载成功时候
    private void multDownLoadSuccess() {
        if (isHasNewFileListener != null) {
            isHasNewFileListener.multDownLoadSuccess();
        }
    }

    //单个文件加载成功
    public void setSingleFileLoadLoadSuccess() {
        loadFileCount++;
        if (loadFileCount == 2) {
            CommonHandler.getInstatnce().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    //加载成功
                    if (isHasNewFileListener != null) {
                        isHasNewFileListener.multFileLoadLoadSuccess();
                    }
                }
            });
        }
    }

    //插件一起下载完成
    public void updateDownload() {
        if (newSingleThreadExecutor == null) {
            newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        newSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    downLoadPluginNum++;
                    String configJson = AppConfigModel.getInstance().getString(Contans.SAVE_CONFIG_FILE_CONTENT_KEY, "");
                    if (TextUtils.isEmpty(configJson)) {
                        return;
                    }
                    Gson gson = new Gson();
                    DownloadPluginMoudle downloadPluginMoudle = gson.fromJson(configJson, DownloadPluginMoudle.class);
                    if (downloadPluginMoudle == null) {
                        return;
                    }
                    CommonApplication.mediaScannerConnectionScanFile(Contans.strDownloadDir);
                    if (downLoadPluginNum == downloadPluginMoudle.getNeedDownLoadFileNum()) {
                        File manager = new File(Contans.strDownloadDir + Contans.str_downloadManager_name);
                        File tempManagerFile = new File(Contans.strDownloadDir + Contans.str_downloadManager_name + ".tmp" + Contans.DOWNLOAD_MANAGER);
                        if (manager.exists() && !tempManagerFile.exists()) {
                            hasManagerFile(false, true, manager);
                        }

                        Contans.str_u_current_plugin_name = Contans.str_downloadPlugin_name;
                        File plugin = new File(Contans.strDownloadDir + Contans.str_downloadPlugin_name);
                        File tempPluginFile = new File(Contans.strDownloadDir + Contans.str_downloadPlugin_name + ".tmp" + Contans.DOWNLOAD_PLUGIN);
                        if (plugin.exists() && !tempPluginFile.exists()) {
                            Contans.str_u_current_plugin_name = Contans.str_downloadPlugin_name;
                            hasPluginZip(false, true, plugin);
                        }
                        //插件信息 下载完成，插件加载器和插件同时下载完成，或者只有其中之一下载完成
                        multDownLoadSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 检查是否有新的 插件 or 加载器
     */
    public void checkHasNewFileVersion() {
        ThreadPoolTool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File manager = new File(Contans.strDownloadDir + Contans.str_downloadManager_name);
                    File tempManagerFile = new File(Contans.strDownloadDir + Contans.str_downloadManager_name + ".tmp" + Contans.DOWNLOAD_MANAGER);
                    if (manager.exists() && !tempManagerFile.exists()) {
                        hasManagerFile(true, true, manager);
                    } else {
                        File fileDefault = new File(Contans.strDownloadDir + Contans.str_u_downloadManager_name);
                        if (fileDefault.exists()) {
                            hasManagerFile(true, false, fileDefault);
                        } else {
                            hasNotManagerFile();
                        }
                    }

                    File plugin = new File(Contans.strDownloadDir + Contans.str_downloadPlugin_name);
                    File tempPluginFile = new File(Contans.strDownloadDir + Contans.str_downloadPlugin_name + ".tmp" + Contans.DOWNLOAD_PLUGIN);
                    if (plugin.exists() && !tempPluginFile.exists()) {
                        hasPluginZip(true, true, plugin);
                    } else {
                        File fileDefault = new File(Contans.strDownloadDir + Contans.str_u_downloadPlugin_name);
                        if (fileDefault.exists()) {
                            hasPluginZip(true, false, fileDefault);
                        } else {
                            hasNotPluginFile();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 下载服务端配置插件信息 包括加载器 和 插件zip
     *
     * @param context
     * @param url
     */
    public void getConfigFile(final Context context, final String url) {
        ThreadPoolTool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = HttpWriteFileQuest.getTxtFileContent(url);
                    if (TextUtils.isEmpty(result)) {
                        return;
                    }
                    DownloadPluginMoudle downloadPluginMoudle = gson.fromJson(result, DownloadPluginMoudle.class);
                    if (downloadPluginMoudle == null) {
                        return;
                    }
                    String versionName = downloadPluginMoudle.getVersionName();
                    String localVersion = BuildConfig.VERSION_NAME;
                    //处理配置文件版本内容
                    if (versionName.compareToIgnoreCase(localVersion) > 0) {
                        //线上配置文件版本比宿主apk版本大
                        String configFileContentVersionName = AppConfigModel.getInstance().getString(Contans.SAVE_CONFIG_FILE_VERSION_KEY, BuildConfig.VERSION_NAME);
                        if (versionName.compareToIgnoreCase(configFileContentVersionName) > 0) {
                            //线上配置文件内容版本比本地该版本大
                            AppConfigModel.getInstance().putString(Contans.SAVE_CONFIG_FILE_VERSION_KEY, versionName, true);
                            AppConfigModel.getInstance().putString(Contans.SAVE_CONFIG_FILE_CONTENT_KEY, result, true);
                            //上面先存起来，下面再下载文件

                            downloaNewPluginAndManager(context, downloadPluginMoudle);
                        } else {
                            //本地已经存在
                            ShowLog.e(TAG, "本地已经存在 该内容");
                        }
                    } else {
                        //本地已经存在
                        ShowLog.e(TAG, "本地版本号和线上一致");
                        //线上配置文件版本比宿主apk版本要小
//                        AppConfigModel.getInstance().putString(ContansKey.SAVE_CONFIG_FILE_VERSION_KEY, versionName, true);
//                        AppConfigModel.getInstance().putString(ContansKey.SAVE_CONFIG_FILE_CONTENT_KEY, result, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //下载新的插件加载器 or 插件zip
    private void downloaNewPluginAndManager(Context context, DownloadPluginMoudle downloadPluginMoudle) {
        downLoadPluginNum = 0;
        //下载管理器
        String managerLoder_url = downloadPluginMoudle.getManagerLoder_url();
        if (!TextUtils.isEmpty(managerLoder_url)) {
            if (!managerLoder_url.contains("http://")) {
                managerLoder_url = ServerConfig.config_host + managerLoder_url;
            }
            DownLoadSevice.startDownload(context, managerLoder_url, Contans.DOWNLOAD_MANAGER);
        }
        //下载插件
        String pluginZip_url = downloadPluginMoudle.getPluginZip_url();
        if (!TextUtils.isEmpty(pluginZip_url)) {
            if (!pluginZip_url.contains("http://")) {
                pluginZip_url = ServerConfig.config_host + pluginZip_url;
            }
            DownLoadSevice.startDownload(context, pluginZip_url, Contans.DOWNLOAD_PLUGIN);
        }
    }

}
