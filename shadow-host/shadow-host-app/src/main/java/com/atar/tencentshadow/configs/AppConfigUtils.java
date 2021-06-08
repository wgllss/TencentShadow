/**
 *
 */
package com.atar.tencentshadow.configs;

import android.content.Context;
import android.text.TextUtils;

import com.atar.tencentshadow.BuildConfig;
import com.common.framework.appconfig.AppConfigDownloadManager;
import com.common.framework.appconfig.AppConfigModel;
import com.common.framework.skin.SkinResourcesManager;
import com.common.framework.utils.ShowLog;
import com.google.gson.Gson;


/**
 * ****************************************************************************************************************************************************************************
 *
 * @author :Atar
 * @createTime:2017-5-24上午11:13:53
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description: ****************************************************************************************************************************************************************************
 */
public class AppConfigUtils {
    private static String TAG = AppConfigUtils.class.getSimpleName();

    /**
     * 获取离线assets下webview 所用文件
     *
     * @author :Atar
     * @createTime:2017-6-6上午10:13:20
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
//    public static void getOffineFilePath(final AssetManager am) {
//        ThreadPoolTool.getInstance().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    String[] resJs = am.list("js");
//                    String[] resImg = am.list("img");
//                    String[] resCss = am.list("css");
//                    String[] html = am.list("html");
//                    String strOfflineResources = "";
//                    if (resJs != null && resJs.length > 0) {
//                        for (int i = 0; i < resJs.length; i++) {
//                            strOfflineResources += resJs[i];
//                        }
//                    }
//                    if (resImg != null && resImg.length > 0) {
//                        for (int i = 0; i < resImg.length; i++) {
//                            strOfflineResources += resImg[i];
//                        }
//                    }
//                    if (resCss != null && resCss.length > 0) {
//                        for (int i = 0; i < resCss.length; i++) {
//                            strOfflineResources += resCss[i];
//                        }
//                    }
//                    if (html != null && html.length > 0) {
//                        for (int i = 0; i < html.length; i++) {
//                            strOfflineResources += html[i];
//                        }
//                    }
//                    AppConfigModel.getInstance().putString(HOST_OFFINE_FILE_PATH_KEY,
//                            strOfflineResources, true);
//                    ZzLog.e("加载完成：");
//                } catch (Exception e) {
//                    ShowLog.e(TAG, e);
//                }
//            }
//        });
//    }

    /**
     * 读取服务端文件 如txt中的json
     *
     * @param FileUrl :文件地址
     * @author :Atar
     * @createTime:2017-5-24上午11:15:57
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void getServerTextJson(final Context context, final String FileUrl) {
        AppConfigDownloadManager.getInstance().getServerJson(FileUrl, new AppConfigDownloadManager.HttpCallBackResult() {
            @Override
            public void onResult(String result) {
                if (result != null) {
                    Gson gson = new Gson();
                    AppConfigJson mAppConfigJson = null;
                    try {
                        mAppConfigJson = gson.fromJson(result, AppConfigJson.class);
                    } catch (Exception e) {
                        ShowLog.e(TAG, e);
                    }
                    if (mAppConfigJson == null) {
                        return;
                    }
                    String versionName = mAppConfigJson.getConfigVersionName();
                    String localVersion = BuildConfig.VERSION_NAME;

                    //处理配置文件版本内容
                    if (versionName.compareToIgnoreCase(localVersion) > 0) {
                        //线上配置文件版本比宿主apk版本大
                        String configFileContentVersionName = AppConfigModel.getInstance().getString(Config.SAVE_CONFIG_FILE_VERSION_KEY, BuildConfig.VERSION_NAME);
                        if (versionName.compareToIgnoreCase(configFileContentVersionName) > 0) {
                            //线上配置文件内容版本比本地该版本大
                            AppConfigModel.getInstance().putString(Config.SAVE_CONFIG_FILE_VERSION_KEY, mAppConfigJson.getConfigVersionName(), true);
                            AppConfigModel.getInstance().putString(Config.SAVE_CONFIG_FILE_CONTENT_KEY, result, true);
                        } else {
                            //本地已经存在
                            ShowLog.e(TAG,"本地已经存在 该内容");
                        }
                    } else {
                        AppConfigModel.getInstance().putString(Config.SAVE_CONFIG_FILE_VERSION_KEY, mAppConfigJson.getConfigVersionName(), true);
                        AppConfigModel.getInstance().putString(Config.SAVE_CONFIG_FILE_CONTENT_KEY, result, true);
                    }

                    //处理引导图
                    if (mAppConfigJson.getGuideImageConfigJson() != null && !TextUtils.isEmpty(mAppConfigJson.getGuideImageConfigJson().getGuideImageVersion())) {
                        String localGuideImageVersion = AppConfigModel.getInstance().getString(Config.SAVE_LOAD_IMAGE_VERSION_KEY, BuildConfig.VERSION_NAME);
                        if (mAppConfigJson.getGuideImageConfigJson().getGuideImageVersion().compareToIgnoreCase(localGuideImageVersion) > 0) {
                            AppConfigModel.getInstance().putString(Config.SAVE_LOAD_IMAGE_CONTENT_KEY, gson.toJson(mAppConfigJson.getGuideImageConfigJson().getList()), true);
                            AppConfigModel.getInstance().putString(Config.SAVE_LOAD_IMAGE_VERSION_KEY, mAppConfigJson.getGuideImageConfigJson().getGuideImageVersion(), true);
                            //下载引导图 供下次使用
//                            DownLoadSevice.startDownloadGuideImage(context, gson.toJson(mAppConfigJson.getGuideImageConfigJson().getList()));
                        }
                    }

                    //处理皮肤
                    if (mAppConfigJson.getSkinconfigJson() != null && !TextUtils.isEmpty(mAppConfigJson.getSkinconfigJson().getSkinVersionName())) {
                        //下载皮肤 供下次使用
                        SkinResourcesManager.getInstance(context).downLoadSkin(null, mAppConfigJson.getSkinconfigJson().isLoadApkSkin(), mAppConfigJson.getSkinconfigJson().getSkinVersionName(), mAppConfigJson.getSkinconfigJson().getSkinReplaceMinVersion());
                    }

                    //处理插件
                    if (mAppConfigJson.getPluginConfigJson() != null && mAppConfigJson.getPluginConfigJson().getPluginList() != null && mAppConfigJson.getPluginConfigJson().getPluginList().size() > 0) {
                        String pluginBaseOnHosetMinVersionName = mAppConfigJson.getPluginConfigJson().getPluginBaseOnHosetMinVersionName();
                        if (!TextUtils.isEmpty(pluginBaseOnHosetMinVersionName) && pluginBaseOnHosetMinVersionName.compareToIgnoreCase(BuildConfig.VERSION_NAME) > 0) {
                            ShowLog.e(TAG,"需要升级宿主程序了");
                            return;
                        }
//                        DownLoadSevice.startDownloadPlugin(context, gson.toJson(mAppConfigJson.getPluginConfigJson().getPluginList()));
                    }
                }
            }
        });
    }

//    /**
//     * @author :Atar
//     * @createTime:2017-5-24下午3:17:37
//     * @version:1.0.0
//     * @modifyTime:
//     * @modifyAuthor:
//     * @description:
//     */
//    public static void switchActivity(OnClickInfo onClickInfo, Context context) {
//        try {
//            if (onClickInfo != null) {
//                // if (onClickInfo.isNeedLogin() && JoinInTGBActivity.startJoinInTGBActivity
//                // (context)) {// 需要登陆
//                // return;
//                // }
//
//                String className = onClickInfo.getClassName();
//                if (className != null && className.contains("com.open.weex") && VERSION.SDK_INT <
//                        VERSION_CODES.ICE_CREAM_SANDWICH) {
//                    CommonToast.show("Android系统版本太低,请升级系统");
//                    return;
//                }
//                if (onClickInfo.getSpecialInfo() != null && onClickInfo.getSpecialInfo().length()
//                        > 0) {// 特殊点击事件 跳转 切换tabhost等
//                    switchMainTab(onClickInfo.getSpecialInfo(), className);
//                    return;
//                }
//                if (className == null || className.length() == 0) {
//                    return;
//                }
//                Class<?> cls = Class.forName(className);
//                Intent intent = new Intent(context, cls);
//                if (ActivityManager.getActivityManager().getActivityStack() != null &&
//                        ActivityManager.getActivityManager().getActivityStack().size() > 0) {
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                }
//                intent = getIntentFromOptionJson(context, intent, onClickInfo.getOptionJson(),
//                        onClickInfo.getOnEventInfo());
//                IntentUtil.startOtherActivity(context, intent);
//            }
//        } catch (Exception e) {
//            CrashHandler.getInstance().CrashException(e, CrashHandler.CRASHT_YPE_CATCH);
//            ShowLog.e(TAG, e);
//        }
//    }
//
//    /**
//     * 处理特殊跳转事件
//     *
//     * @param specialInfo
//     * @param subTabClassActivityName
//     * @author :Atar
//     * @createTime:2017-5-24下午3:15:07
//     * @version:1.0.0
//     * @modifyTime:
//     * @modifyAuthor:
//     * @description:
//     */
//    public static void switchMainTab(String specialInfo, String subTabClassActivityName) {
//        try {
//            if (specialInfo != null && specialInfo.length() > 0) {
//                JSONObject specialJson = new JSONObject(specialInfo);
//                int tabHostposition = 0;
//                int viewPagerItem = -1;
//                if (specialJson != null && specialJson.length() > 0) {
//                    if (specialJson.has("tabHostposition")) {
//                        tabHostposition = specialJson.getInt("tabHostposition");
//                    }
//                    if (specialJson.has("viewPagerItem")) {
//                        viewPagerItem = specialJson.getInt("viewPagerItem");
//                    } else {
//                        viewPagerItem = -1;
//                    }
//                }
//                // if (ActivityManager.getActivityManager().getActivity(MainTabActivity.class) !=
//                // null) {
//                // ActivityManager.getActivityManager().getActivity(MainTabActivity.class)
//                // .switchMainTab(tabHostposition, subTabClassActivityName, viewPagerItem);
//                // }
//            }
//        } catch (Exception e) {
//            CrashHandler.getInstance().CrashException(e, CrashHandler.CRASHT_YPE_CATCH);
//            ShowLog.e(TAG, e);
//        }
//    }
//

    /**
     * 从json中获取参数传入到intent中去 适合app中所有跳转
     *
     * @return
     * @author :Atar
     * @createTime:2017-5-24上午11:25:36
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
//    public static void startActivity(Context context, HomeConfigJson homeConfigJson) {
//        try {
//            if (homeConfigJson == null) {
//                return;
//            }
//            switch (homeConfigJson.getType()) {
//                case 0://宿主 跳转宿主
//                    if (!TextUtils.isEmpty(homeConfigJson.getClassName())) {
//                        ZzLog.e("homeConfigJson.getClassName()-->" + homeConfigJson.getClassName());
//                        Class<?> cls = Class.forName(homeConfigJson.getClassName());
//                        Intent intent = new Intent(context, cls);
//                        String optionJson = homeConfigJson.getOptionJson();
//                        intent = IntentUtil.getIntentFromOptionJson(intent, optionJson);
//                        IntentUtil.startOtherActivity(context, intent);
//                    }
//                    break;
//                case 1://宿主跳转到插件
//                    if (!TextUtils.isEmpty(homeConfigJson.getClassName())) {
//                        ZzLog.e("homeConfigJson.getClassName()-->" + homeConfigJson.getClassName());
//                        String optionJson = homeConfigJson.getOptionJson();
//                        if (context instanceof BaseActivity) {
//                            String apk_sdk_path = Config.strDownloadDir + homeConfigJson.getPluginPath();
//                            ZzLog.e("apk_sdk_path:" + apk_sdk_path);
//                            ProxyActivity.startProxyActivity((BaseActivity) context, optionJson, homeConfigJson.getClassName(), apk_sdk_path);
//                        }
//                    }
//                    break;
//                case 2://插件跳转到宿主 代码写在插件里面
//                    if (!TextUtils.isEmpty(homeConfigJson.getClassName())) {
//                        Class<?> cls = Class.forName(homeConfigJson.getClassName());
//                        Intent intent = new Intent(context, cls);
//                        intent.putExtra(PluginManager.HOST_CLASS_NAME, true);
//                        String optionJson = homeConfigJson.getOptionJson();
//                        intent = IntentUtil.getIntentFromOptionJson(intent, optionJson);
//                        IntentUtil.startOtherActivity(context, intent);
//                    }
//                    break;
//                case 3://插件到插件 ，代码写在插件里面
//                    break;
//            }
//
//        } catch (Exception e) {
//            ShowLog.e(TAG, e);
//        }
//    }

//
//    /**
//     * 设置统计事件
//     *
//     * @param onEventInfo
//     * @author :Atar
//     * @createTime:2017-7-24下午4:11:49
//     * @version:1.0.0
//     * @modifyTime:
//     * @modifyAuthor:
//     * @description:
//     */
//    public static void setEventInfo(Context context, String onEventInfo) {
//        try {
//            if (onEventInfo != null && onEventInfo.length() > 0) {// 解析点击统计事件
//                JSONObject onEventJson = new JSONObject(onEventInfo);
//                if (onEventJson != null) {
//                    String eventType = "";
//                    String eventID = "";
//                    String eventName = "";
//                    if (onEventJson.has("eventType")) {
//                        eventType = onEventJson.getString("eventType");
//                    }
//                    if (onEventJson.has("eventID")) {
//                        eventID = onEventJson.getString("eventID");
//                    }
//                    if (onEventJson.has("eventName")) {
//                        eventName = onEventJson.getString("eventName");
//                    }
//                    if ("0".equals(eventType)) {
//                        // 百度统计
//                        // StatService.onEvent(context, eventID, eventName, 1);
//                    } else if ("1".equals(eventType)) {
//                        // 友盟统计
//                        // MobclickAgent.onEvent(context, eventName);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            CrashHandler.getInstance().CrashException(e, CrashHandler.CRASHT_YPE_CATCH);
//            ShowLog.e(TAG, e);
//        }
//    }
//

    /**
     * 得到默认配置数据
     *
     * @return
     * @author :Atar
     * @createTime:2017-5-24下午1:28:35
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static String getDefaultSetting() {
        return "{\n" +
                "    \"configVersionName\":\"1.0.00\",\n" +
                "    \"guideImageConfigJson\":{\n" +
                "        \"guideImageVersion\":\"1.0.00\",\n" +
                "        \"guideImageMinVersionName\":\"1.0.00\",\n" +
                "        \"list\":[\n" +
                "            \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1605787039347&di=dbc20edebafe0283000c2213f1149c44&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F9345d688d43f87941a6570a2d71b0ef41ad53aa7.jpg\",\n" +
                "            \"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3414787736,2428132775&fm=26&gp=0.jpg\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"skinconfigJson\":{\n" +
                "        \"isLoadApkSkin\":true,\n" +
                "        \"skinVersionName\":\"1.0.00\",\n" +
                "        \"skinReplaceMinVersion\":\"1.0.00\",\n" +
                "        \"skinTypes\":[\n" +
                "            {\n" +
                "                \"skinType\":0,\n" +
                "                \"skinName\":\"日间模式\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"skinType\":1,\n" +
                "                \"skinName\":\"夜间模式\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"pluginConfigJson\":{\n" +
                "        \"pluginBaseOnHosetMinVersionName\":\"1.0.00\",\n" +
                "        \"pluginList\":[\n" +
                "            {\n" +
                "                \"pluginUrl\":\"assets/apk/down_plugin/release/app_sub1-release.apk\",\n" +
                "                \"pluginVersion\":\"1.0.00\",\n" +
                "                \"pluginReplaceMinVersion\":\"1.0.00\",\n" +
                "                \"pluginShowName\":\"app_sub1模块\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"homeConfigJson\":{\n" +
                "        \"type\":0,\n" +
                "        \"className\":\"com.atar.host.app.activity.Main2Activity\"\n" +
                "    }\n" +
                "}";
    }
}
