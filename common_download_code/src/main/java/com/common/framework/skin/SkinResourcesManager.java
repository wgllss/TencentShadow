/**
 *
 */
package com.common.framework.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;

import com.common.framework.Threadpool.ThreadPoolTool;
import com.common.framework.appconfig.AppConfigDownloadManager;
import com.common.framework.appconfig.AppConfigModel;
import com.common.framework.download.DownLoadFileBean;
import com.common.framework.interfaces.HandlerListener;
import com.common.framework.utils.FileUtils;
import com.common.framework.utils.MDPassword;
import com.common.framework.utils.ShowLog;
import com.zz.common.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * ****************************************************************************************************************************************************************************
 * 皮肤资源管理器
 *
 * @author :Atar
 * @createTime:2017-9-18上午10:33:21
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description: ****************************************************************************************************************************************************************************
 */
public class SkinResourcesManager {
    private String TAG = SkinResourcesManager.class.getSimpleName();

    //存放服务端返回是否加载外部apk 皮肤资源
    private static final String IS_LOAD_APK_SKIN_KEY = "IS_LOAD_APK_SKIN_KEY";
    //保存下载皮肤版本
    private static final String DOWN_LOAD_SKIN_VERSION_NAME_KEY = "DOWN_LOAD_SKIN_VERSION_NAME_KEY";

    /**
     * 是否加载apk资源 false加载library下资源
     */
    public static boolean isLoadApkSkin = AppConfigModel.getInstance().getBoolean(IS_LOAD_APK_SKIN_KEY, false);
    //当前加载皮肤版本
    private String downLoadVersionName = AppConfigModel.getInstance().getString(DOWN_LOAD_SKIN_VERSION_NAME_KEY, "");

    private String download_skin_Url = "";
    /**
     * 主工程包名
     */
    private String main_project_packname = "";
    /**
     * 皮肤工程包名
     */
    private String skin_project_packname = "";
    /**
     * SD卡目录 下载 资源文件 皮肤资源
     */
    private String SD_PATH = Environment.getExternalStorageDirectory() + "/.android/.cache/.skin/";
    /**
     * sd下默认皮肤资源
     */
    private String DEFAULT_SD_SKIN_NAME = "default_skin";
    /**
     * sd下载皮肤资源
     */
    private String DOWNLOAD_SD_SKIN_NAME = "download_skin";
    private String current_version = "1.0.00";
    private String default_skin_version_name = "1.0.00";
    private String current_confing_content;
    private String default_confing_content;


    private static SkinResourcesManager mInstance;
    private Context mContext;
    private Resources mResources;

    public String getCurrent_confing_content() {
        return current_confing_content;
    }

    public SkinResourcesManager setCurrent_confing_content(String current_confing_content) {
        this.current_confing_content = current_confing_content;
        return this;
    }

    public SkinResourcesManager setDefault_confing_content(String default_confing_content) {
        this.default_confing_content = default_confing_content;
        return this;
    }

    public static SkinResourcesManager getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new SkinResourcesManager();
            mInstance.mContext = mContext;
            mInstance.main_project_packname = mContext.getPackageName();
            String exPath = mInstance.main_project_packname;
            if (!BuildConfig.DEBUG) {
                exPath = MDPassword.getPassword32(mInstance.main_project_packname);
                mInstance.DOWNLOAD_SD_SKIN_NAME = MDPassword.getPassword32(mInstance.DOWNLOAD_SD_SKIN_NAME);
                mInstance.DEFAULT_SD_SKIN_NAME = MDPassword.getPassword32(mInstance.DEFAULT_SD_SKIN_NAME);
            }
            mInstance.SD_PATH = mInstance.SD_PATH + exPath + "/";
        }
        return mInstance;
    }

    /**
     * 初始化皮肤资源
     *
     * @param skin_project_packname 皮肤工程包名
     * @param download_skin_Url     下载皮肤url
     * @author :Atar
     * @createTime:2017-9-18下午5:19:23
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void initSkinResources(String skin_project_packname, String default_skin_version_name, final String download_skin_Url) {
        this.skin_project_packname = skin_project_packname;
        this.default_skin_version_name = default_skin_version_name;
        this.download_skin_Url = download_skin_Url;
        if (isLoadApkSkin) {
            ThreadPoolTool.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        current_version = downLoadVersionName;
                        ShowLog.e(TAG, "current_version:" + current_version);
                        if (TextUtils.isEmpty(current_version)) {
                            //当前版本 本地没有
                            loadDefaultSkin();
                            return;
                        }
                        File file = new File(SD_PATH + current_version + "/");
                        if (!FileUtils.exists(SD_PATH)) {
                            FileUtils.createDir(SD_PATH);
                        }
                        final File downloadFile = new File(file.getAbsolutePath(), DOWNLOAD_SD_SKIN_NAME);
                        if (downloadFile.exists()) {// 存在下载皮肤文件
                            loadSkinResources(downloadFile.getAbsolutePath(), null);
                            return;
                        }
                        loadDefaultSkin();
                    } catch (Exception e) {
                        loadDefaultSkin();
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //加载默认皮肤
    private void loadDefaultSkin() {
        try {
            isLoadApkSkin = false;
            AppConfigModel.getInstance().putInt(SkinMode.SKIN_MODE_KEY, SkinMode.DAY_MODE, true);
            current_confing_content = default_confing_content;
            mResources = mContext.getResources();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步 加载皮肤资源
     *
     * @param skinFilePath
     * @param callback
     * @author :Atar
     * @createTime:2017-9-18上午11:08:05
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    private void loadSkinResources(final String skinFilePath, final loadSkinCallBack callback) {
        try {
            int flags = PackageManager.GET_META_DATA | PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES
                    | PackageManager.GET_PROVIDERS | PackageManager.GET_RECEIVERS;

            final PackageManager packageManager = mContext.getPackageManager();
            final PackageInfo packageInfo = packageManager.getPackageArchiveInfo(skinFilePath, flags);
            final ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            applicationInfo.sourceDir = applicationInfo.publicSourceDir = skinFilePath;
            try {
                mResources = packageManager.getResourcesForApplication(applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
            }
            if (callback != null) {
                callback.loadSkinSuccess(mResources);
            }
            ShowLog.i(TAG, "皮肤加载成功");
        } catch (Exception e) {
            loadDefaultSkin();
            e.printStackTrace();
        }
    }

    /**
     * 异步 加载皮肤资源
     *
     * @param callback
     * @author :Atar
     * @createTime:2017-9-18上午11:08:30
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void loadSkinResources(final loadSkinCallBack callback) {
        if (isLoadApkSkin) {
            ThreadPoolTool.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        current_version = downLoadVersionName;
                        ShowLog.e(TAG,"current_version:" + current_version);
                        if (TextUtils.isEmpty(current_version)) {
                            //当前版本 本地没有
                            loadDefaultSkin();
                            return;
                        }
                        File file = new File(SD_PATH + current_version + "/");
                        if (!FileUtils.exists(SD_PATH)) {
                            FileUtils.createDir(SD_PATH);
                        }
                        final File downloadFile = new File(file.getAbsolutePath(), DOWNLOAD_SD_SKIN_NAME);
                        if (downloadFile.exists()) {// 存在下载皮肤文件
                            loadSkinResources(downloadFile.getAbsolutePath(), null);
                            return;
                        }
                        loadDefaultSkin();
                    } catch (Exception e) {
                        loadDefaultSkin();
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 下载皮肤
     *
     * @param activity
     * @param newVersion        皮肤新版本号
     * @param replaceMinVersion 皮肤在多少版本以上( >= )下载替换
     * @author :Atar
     * @createTime:2017-9-22下午2:42:49
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void downLoadSkin(Activity activity, boolean isLoadApkSkin, final String newVersion, String replaceMinVersion) {
        AppConfigModel.getInstance().putBoolean(IS_LOAD_APK_SKIN_KEY, isLoadApkSkin, true);
        if (isLoadApkSkin) {
            AppConfigDownloadManager.getInstance().downLoadAppConfigFile(activity, new SkinHandlerListener(newVersion, isLoadApkSkin), newVersion, replaceMinVersion, 0, download_skin_Url, 1, true,
                    DOWNLOAD_SD_SKIN_NAME, SD_PATH + newVersion + "/");
        }
    }

    public Resources getResources() {
        if (isLoadApkSkin) {
            return mResources;
        } else {
            return mResources != null ? mResources : mContext.getResources();
        }
    }

    public String getSkinPackName() {
        return isLoadApkSkin ? skin_project_packname : main_project_packname;
    }

    public interface loadSkinCallBack {
        void loadSkinSuccess(Resources mResources);
    }

    //皮肤下再回调
    public class SkinHandlerListener implements HandlerListener {
        //最新版本 皮肤
        private String newVersion;
        private boolean isLoadApkSkin;

        public SkinHandlerListener(String newVersion, boolean isLoadApkSkin) {
            this.newVersion = newVersion;
            this.isLoadApkSkin = isLoadApkSkin;
        }

        @Override
        public void onHandlerData(Message msg) {
            switch (msg.what) {
                case DownLoadFileBean.DOWLOAD_FLAG_FAIL:
                    ShowLog.i(TAG, "皮肤下载失败");
                    break;
                case DownLoadFileBean.DOWLOAD_FLAG_SUCCESS:
                    ShowLog.i(TAG, "皮肤下载成功");
                    AppConfigModel.getInstance().putString(DOWN_LOAD_SKIN_VERSION_NAME_KEY, newVersion, true);
                    break;
                case DownLoadFileBean.DOWLOAD_FLAG_ING:
                    ShowLog.i(TAG, "皮肤正在下载:" + msg.arg2 + "%");
                    break;
            }
        }
    }
}
