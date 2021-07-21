package com.google.samples.apps.sunflower;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.atar.bridge.BridgeExitInteface;
import com.atar.bridge.BridgeManager;
import com.atar.bridge.Test;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.samples.apps.sunflower.databinding.ActivityGarden2Binding;
import com.google.samples.bridgebind.BridgebindserviceUtil;
import com.google.samples.broadcast.TestReceiver;
import com.google.samples.manager.ActivityManager;

public class Garden2Activity extends AppCompatActivity {
    private ActivityGarden2Binding binding;

    private TestReceiver testReceiver = new TestReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().pushActivity(this);

        Test.getInstance().test(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_garden2);
        binding.txtBind.setText("整单打发的发发的发发的啊发");
        binding.txtBind2.setText("点击跳转到宿主");
        binding.txtBind2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Garden2Activity.this, BridgeManager.getInstance().getClassByHost("com.atar.tencentshadow.activity.SettingIPActivity"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.txtBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.txtBind3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), Garden3Activity.class));
            }
        });

        binding.txtBind5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BridgebindserviceUtil.getInstance().processBridge(v.getContext());
            }
        });


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.test.receiver.plugin");
        registerReceiver(testReceiver, intentFilter);
        
        setFrescoImg(binding.tenantlogo, Uri.parse("https://img1.baidu.com/it/u=686675228,2481849275&fm=26&fmt=auto&gp=0.jpg"), true);
//        binding.tenantlogo.setImageURI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (testReceiver != null) {
            unregisterReceiver(testReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private long exitTime = 0;

    public void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            BridgeManager.getInstance().exit(new BridgeExitInteface() {
                @Override
                public void exit() {
                    ActivityManager.getActivityManager().exitApplication();
                }
            });
        }
    }

    private void setFrescoImg(SimpleDraweeView simpleDraweeView, Uri uri, boolean reLoad) {
//        int size = ScreenUtils.dp2px(60);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
//                .setResizeOptions(new ResizeOptions(size, size))
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setImageRequest(request)
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(true)
                .setOldController(simpleDraweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>() {

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        if (reLoad) {
                            ImagePipeline imagePipeline = Fresco.getImagePipeline();
                            imagePipeline.evictFromMemoryCache(uri);
                            imagePipeline.evictFromDiskCache(uri);
                            imagePipeline.evictFromCache(uri);
                            imagePipeline.resume();
                        }
                    }
                })
                .build();
        simpleDraweeView.setController(controller);
    }
}