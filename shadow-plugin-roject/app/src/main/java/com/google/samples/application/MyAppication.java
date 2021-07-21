package com.google.samples.application;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * @author：atar
 * @date: 2021/7/19
 * @description:
 */
public class MyAppication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DiskCacheConfig cacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryName(getPackageName())
                .setBaseDirectoryPath(Environment.getExternalStorageDirectory())
                .build(); //设置磁盘缓存的配置,生成配置文件 ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this) .setMainDiskCacheConfig(cacheConfig) .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setDownsampleEnabled(true)                             // 对图片进行自动缩放
                .setResizeAndRotateEnabledForNetwork(true)   // 对网络图片进行resize处理，减
                .setMainDiskCacheConfig(cacheConfig).build();

        Fresco.initialize(this, config);
    }
}
