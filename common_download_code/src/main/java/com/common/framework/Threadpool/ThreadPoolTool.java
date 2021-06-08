/**
 *
 */
package com.common.framework.Threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * *****************************************************************************************
 *
 * @className:多线程异步http请求类
 * @author: Atar
 * @createTime:2014-5-18下午11:34:10
 * @modifyTime:
 * @version: 1.0.0
 * @description:利用多线程进行异步操作网络请求，此类使用单利模式 *****************************************************************************************
 */
public class ThreadPoolTool {

    private static final String TAG = ThreadPoolTool.class.getSimpleName();
    private static ThreadPoolTool mInstance;// 异步请求单例模式

    public static ThreadPoolTool getInstance() {
        if (mInstance == null) {
            mInstance = new ThreadPoolTool();
        }
        return mInstance;
    }

    // 线程池
    private final ExecutorService exec = Executors.newCachedThreadPool();
//    private Gson gson = new Gson();

    /**
     * 执行异步任务
     *
     * @param mRunnable
     * @author :Atar
     * @createTime:2015-9-22下午4:24:05
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void execute(Runnable mRunnable) {
        if (exec != null) {
            exec.execute(mRunnable);
        }
    }
}
