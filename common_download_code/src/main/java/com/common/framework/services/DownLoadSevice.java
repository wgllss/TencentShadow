package com.common.framework.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;

import com.common.framework.Threadpool.ThreadPoolTool;
import com.common.framework.download.DownLoadFileBean;
import com.common.framework.download.DownLoadFileManager;
import com.common.framework.interfaces.HandlerListener;
import com.common.framework.plugins.Contans;
import com.common.framework.plugins.DownloadPluginManager;
import com.common.framework.plugins.ServerConfig;
import com.common.framework.utils.ShowLog;

public class DownLoadSevice extends Service implements HandlerListener {

    private String TAG = DownLoadSevice.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        try {
            if (intent != null) {
                ThreadPoolTool.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        int type = intent.getIntExtra(STR_DOWN_LOAD_TYPE, 0);
                        switch (type) {
                            case DOWN_LOAD_TYPE:
                                int whichUrl = intent.getIntExtra(DOWN_LOAD_WHICH_URL_KEY, 0);
                                String downloadUrl = intent.getStringExtra(DOWN_LOAD_URL_KEY);
                                String strDownloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1, downloadUrl.length());
                                DownLoadFileManager.getInstance().downLoad(null, DownLoadSevice.this, whichUrl, downloadUrl, 1, true, strDownloadFileName, Contans.strDownloadDir);
                                break;
                            case GET_SERVER_CONFIG://获取服务端配置
                                DownloadPluginManager.getInstance().getConfigFile(DownLoadSevice.this, ServerConfig.config_file_url);
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onHandlerData(Message msg) {
        try {
            String strMsg = msg.arg2 == Contans.DOWNLOAD_MANAGER ? "加载器：" : msg.arg2 == Contans.DOWNLOAD_PLUGIN ? "插件" : "";
            switch (msg.what) {
                case DownLoadFileBean.DOWLOAD_FLAG_FAIL:
                    ShowLog.i(TAG, msg.arg2 + "---" + strMsg + "---" + "下载失败");
                    break;
                case DownLoadFileBean.DOWLOAD_FLAG_SUCCESS:
                    ShowLog.i(TAG, msg.arg2 + "---" + strMsg + "---" + "下载成功");
                    if (msg.arg2 == Contans.DOWNLOAD_MANAGER || msg.arg2 == Contans.DOWNLOAD_PLUGIN) {
                        DownloadPluginManager.getInstance().updateDownload();
                    }
                    break;
                case DownLoadFileBean.DOWLOAD_FLAG_ING:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static final String STR_DOWN_LOAD_TYPE = "DOWN_LOAD_TYPE";


    //下载引导图
    private static final int DOWN_LOAD_GUIDE_IMAGE_TYPE = 1001;
    private static final String DOWN_LOAD_GUIDE_IMAGE_CONTENT_KEY = "DOWN_LOAD_GUIDE_IMAGE_CONTENT_KEY";

    //下载
    private static final String DOWN_LOAD_URL_KEY = "DOWN_LOAD_URL_KEY";
    private static final String DOWN_LOAD_WHICH_URL_KEY = "DOWN_LOAD_WHICH_URL_KEY";
    private static final int DOWN_LOAD_TYPE = 1000;

    //下载插件
    private static final int DOWN_LOAD_PLUGIN_TYPE = 1002;
    private static final String DOWN_LOAD_PLUGIN_CONTENT_KEY = "DOWN_LOAD_PLUGIN_CONTENT_KEY";

    //获取服务端配置
    private static final int GET_SERVER_CONFIG = 1003;

    //下载文件
    public static void setGetServerConfig(Context context) {
        try {
            Intent intent = new Intent(context, DownLoadSevice.class);
            intent.putExtra(STR_DOWN_LOAD_TYPE, GET_SERVER_CONFIG);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下载文件
    public static void startDownload(Context context, String downloadUrl, int whichUrl) {
        try {
            Intent intent = new Intent(context, DownLoadSevice.class);
            intent.putExtra(STR_DOWN_LOAD_TYPE, DOWN_LOAD_TYPE);
            intent.putExtra(DOWN_LOAD_URL_KEY, downloadUrl);
            intent.putExtra(DOWN_LOAD_WHICH_URL_KEY, whichUrl);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下载引导图
    public static void startDownloadGuideImage(Context context, String content) {
        try {
            Intent intent = new Intent(context, DownLoadSevice.class);
            intent.putExtra(STR_DOWN_LOAD_TYPE, DOWN_LOAD_GUIDE_IMAGE_TYPE);
            intent.putExtra(DOWN_LOAD_GUIDE_IMAGE_CONTENT_KEY, content);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下载引导图
    public static void startDownloadPlugin(Context context, String content) {
        try {
            Intent intent = new Intent(context, DownLoadSevice.class);
            intent.putExtra(STR_DOWN_LOAD_TYPE, DOWN_LOAD_PLUGIN_TYPE);
            intent.putExtra(DOWN_LOAD_PLUGIN_CONTENT_KEY, content);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
