package com.atar.tencentshadow.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.framework.stack.ActivityManager;


public class MainActivity extends Activity {

    private String TAG = MainActivity.class.getSimpleName();

    public static final int FROM_ID_START_ACTIVITY = 1001;
    public static final int FROM_ID_CALL_SERVICE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().pushActivity(this);
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        textView.setText("宿主App");


        Button settingButton = new Button(this);
        settingButton.setText("设置IP");
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingIPActivity.class));
            }
        });
        linearLayout.addView(settingButton);
        setContentView(linearLayout);

    }


    public static void startMainActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }
}