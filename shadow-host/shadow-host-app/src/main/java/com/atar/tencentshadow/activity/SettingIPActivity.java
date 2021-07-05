package com.atar.tencentshadow.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.atar.tencentshadow.R;
import com.atar.tencentshadow.configs.Config;
import com.common.framework.appconfig.AppConfigModel;
import com.common.framework.download.DownLoadFileBean;
import com.common.framework.download.DownLoadFileManager;
import com.common.framework.interfaces.HandlerListener;
import com.common.framework.plugins.Contans;
import com.common.framework.plugins.ServerConfig;
import com.common.framework.services.DownLoadSevice;
import com.common.framework.stack.ActivityManager;
import com.common.framework.utils.ServiceUtil;
import com.common.framework.utils.ShowLog;
import com.common.framework.widget.CommonToast;
import com.tencent.shadow.dynamic.host.EnterCallback;
import com.tencent.shadow.dynamic.host.PluginManager;
import com.tencent.shadow.sample.introduce_shadow_lib.InitApplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SettingIPActivity extends AppCompatActivity implements View.OnClickListener, HandlerListener {

    private static String TAG = SettingIPActivity.class.getSimpleName();

    private final int INSTALL_PACKAGES_REQUESTCODE = 12334;
    private final int GET_UNKNOWN_APP_SOURCES = 12338;

    public static final int FROM_ID_START_ACTIVITY = 1001;

    private Handler chandler = new Handler();

    private EditText edt_text;
    private TextView txt_save;
    private TextView txt_download;
    private TextView txt_download_manager;
    private TextView txt_to_plugin;
    private String ip = "";
    private String url;
    private String manager_url;
    private String strDownloadFileName;
    private String strDownloadManagerFileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().pushActivity(this);
        ServiceUtil.startService(this, DownLoadSevice.class);
        DownLoadSevice.setGetServerConfig(this);
        setContentView(R.layout.activity_setting_i_p);

        edt_text = findViewById(R.id.edt_text);
        txt_save = findViewById(R.id.txt_save);
        txt_download = findViewById(R.id.txt_download);
        txt_download_manager = findViewById(R.id.txt_download_manager);
        txt_to_plugin = findViewById(R.id.txt_to_plugin);

        txt_save.setOnClickListener(this);
        txt_download.setOnClickListener(this);
        txt_download_manager.setOnClickListener(this);
        txt_to_plugin.setOnClickListener(this);
        setIP();
        ip = ServerConfig.Config_IP;
        edt_text.setHint(url);

        //请求安装未知应用来源的权限
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, INSTALL_PACKAGES_REQUESTCODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_save:
                String str = edt_text.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    CommonToast.show("输入为空");
                }
                AppConfigModel.getInstance().putString(Contans.HOST_KEY, str, true);
                break;
            case R.id.txt_download:
                setIP();
                DownLoadFileManager.getInstance().downLoad(this, this, 1, this.url, 2, true, strDownloadFileName, Config.strDownloadDir);
                break;
            case R.id.txt_download_manager:
                setIP();
                DownLoadFileManager.getInstance().downLoad(this, this, 2, this.manager_url, 1, true, strDownloadManagerFileName, Config.strDownloadDir);
                break;
            case R.id.txt_to_plugin:
                PluginManager pluginManager = InitApplication.getPluginManager();
                Bundle bundle = new Bundle();

                String pluginZipPath = Contans.strDownloadDir + Contans.str_u_current_plugin_name;
                ShowLog.e(TAG, pluginZipPath);
                bundle.putString("pluginZipPath", pluginZipPath);
                try {
                    pluginManager.enter(this, FROM_ID_START_ACTIVITY, bundle, new EnterCallback() {
                        @Override
                        public void onShowLoadingView(View view) {
//                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                lp.gravity.
//                com.atar.tencentshadow.activity.LoadingActivity.this.addContentView(view, lp);//显示Manager传来的Loading页面
                        }

                        @Override
                        public void onCloseLoadingView() {
//                        com.atar.tencentshadow.activity.MainActivity.this.setContentView(linearLayout);
                        }

                        @Override
                        public void onEnterComplete() {
                            chandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ShowLog.e(TAG, "overridePendingTransition");
                                    overridePendingTransition(R.anim.anim_alpha_121, R.anim.anim_alpha_121);
//                            ActivityManager.getActivityManager().finishActivity(LoadingActivity.class);
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    MainActivity.startMainActivity(this);
                }
                break;
        }
    }

    @Override
    public void onHandlerData(Message msg) {
        switch (msg.what) {
            case DownLoadFileBean.DOWLOAD_FLAG_FAIL:
                ShowLog.i(TAG, msg.arg2 + ":下载失败");
                break;
            case DownLoadFileBean.DOWLOAD_FLAG_SUCCESS:
                if (msg.arg2 == 1) {
                    txt_download.setText("下载插件(" + 100 + "%)");
                } else if (msg.arg2 == 2) {
                    txt_download_manager.setText("下载插件(" + 100 + "%)");
                }

                ShowLog.i(TAG, msg.arg2 + ":下载成功");
                break;
            case DownLoadFileBean.DOWLOAD_FLAG_ING:
                int progress = (Integer) msg.obj;

                if (msg.arg2 == 1) {
                    txt_download.setText("下载插件(" + progress + "%)");
                } else if (msg.arg2 == 2) {
                    txt_download_manager.setText("下载插件(" + progress + "%)");
                }
                ShowLog.i(TAG, msg.arg2 + ":下载进度" + progress);
                break;
        }
    }

    public void setIP() {
        ip = AppConfigModel.getInstance().getString(Contans.HOST_KEY, ip);
        url = ip + ":8080/assets/apk/plugins/plugin-debug.zip";
        manager_url = ip + ":8080/assets/apk/plugins/shadow-manager-debug.apk";
        if (!url.contains("http://")) {
            url = "http://" + url;
        }
        if (!manager_url.contains("http://")) {
            manager_url = "http://" + manager_url;
        }
        strDownloadFileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        strDownloadManagerFileName = manager_url.substring(manager_url.lastIndexOf("/") + 1, manager_url.length());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//            clickFinish(null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean b = getPackageManager().canRequestPackageInstalls();
                if (!b) {
                    //将用户引导至安装未知应用界面。
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);
                    return;
                }
            }
        }
    }
}