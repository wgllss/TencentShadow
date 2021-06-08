/**
 *
 */
package com.common.framework.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.framework.application.CommonApplication;

import java.util.Date;

/**
 * ****************************************************************************************************************************************************************************
 * 公用Toast提示控件
 *
 * @author :Atar
 * @createTime:2014-6-17下午4:41:07
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description: ****************************************************************************************************************************************************************************
 */
public class CommonToast {

    private static int toastBackgroundId = -1;
    private static int toastTextColorId = -1;
    private static Date lastDate = null;
    // 提示时间间隔3秒钟
    private static int toastPeriod = 3;
    private static Toast mToast;
    private static TextView toastText;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static Runnable r = new Runnable() {
        public void run() {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;// toast隐藏后，将其置为null
            }
            if (toastText != null) {
                toastText = null;
            }
        }
    };

    /**
     * 初始化Toast资源颜色
     *
     * @param toastBackgroundId 背景颜色
     * @param toastTextColorId  提示文字颜色
     * @author :Atar
     * @createTime:2014-6-25上午10:26:23
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void initToastResouseId(int toastBackgroundId, int toastTextColorId) {
        CommonToast.toastBackgroundId = toastBackgroundId;
        CommonToast.toastTextColorId = toastTextColorId;
    }

    /**
     * 显示toast
     *
     * @param strContent      显示内容
     * @param widthRate       占屏幕宽度比例 0为 WRAP_CONTENT
     * @param heightRate      占屏幕高度比例 0为 WRAP_CONTENT
     * @param gravity         占屏幕位置
     * @param delayMillis     延迟多少毫秒隐藏tost
     * @param isInToastPeriod isInToastPeriod 在规定的时间间隔里内是否显示
     * @author :Atar
     * @createTime:2016-3-18上午10:19:13
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    @SuppressWarnings("deprecation")
    public static void show(Context context, String strContent, int widthRate, int heightRate, int gravity, int offy, long delayMillis, boolean isInToastPeriod) {
        show(context, strContent, widthRate, heightRate, gravity, offy, delayMillis, isInToastPeriod, 1, 0);
    }

    /**
     * 显示toast
     *
     * @param strContent      显示内容
     * @param widthRate       占屏幕宽度比例 0为 WRAP_CONTENT
     * @param heightRate      占屏幕高度比例 0为 WRAP_CONTENT
     * @param gravity         占屏幕位置
     * @param delayMillis     延迟多少毫秒隐藏tost
     * @param isInToastPeriod isInToastPeriod 在规定的时间间隔里内是否显示
     * @param orientation     屏幕方向
     * @param rotation        旋转角度
     * @author :Atar
     * @createTime:2016-3-18上午10:19:13
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    @SuppressWarnings("deprecation")
    public static void show(Context context, String strContent, int widthRate, int heightRate, int gravity, int offy, long delayMillis, boolean isInToastPeriod, int orientation, int rotation) {
        try {
            if (isInToastPeriod) {
                return;
            }
            mHandler.removeCallbacks(r);
            if (mToast == null || toastText == null) {// 只有mToast==null时才重新创建，否则只需更改提示文字
                mToast = new Toast(context);
                LinearLayout toastLayout = new LinearLayout(context);
                toastLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                toastLayout.setOrientation(LinearLayout.VERTICAL);
                toastLayout.setGravity(Gravity.CENTER);
                toastText = new TextView(context);
                int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
                int width = 0;
                int height = 0;
                if (orientation == 1) { // 1竖屏
                    int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
                    if (widthRate == 0) {
                        width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    } else {
                        width = widthPixels / widthRate;
                    }
                    if (heightRate == 0) {
                        height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    } else {
                        height = widthPixels / heightRate;
                    }
                    toastText.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    toastLayout.setRotation(rotation);
                } else {
                    int widthPixels = context.getResources().getDisplayMetrics().heightPixels;
                    if (widthRate == 0) {
                        width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    } else {
                        width = widthPixels / widthRate;
                    }
                    if (heightRate == 0) {
                        height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    } else {
                        height = widthPixels / heightRate;
                    }
                    toastText.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    toastLayout.setRotation(rotation);
                }
                toastText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                toastText.setPadding(size, size, size, size);
                toastText.setGravity(Gravity.CENTER);
                if (toastBackgroundId != -1) {
                    toastLayout.setBackgroundDrawable(context.getResources().getDrawable(toastBackgroundId));
                } else {
                    setText(toastText);
                }
                if (toastTextColorId != -1) {
                    toastText.setTextColor(context.getResources().getColor(toastTextColorId));
                } else {
                    setText(toastText);
                }

                toastLayout.addView(toastText);
                mToast.setView(toastLayout);
                mToast.setGravity(gravity, 0, offy);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            if (toastText != null) {
                toastText.setText(strContent);
            }
            mHandler.postDelayed(r, delayMillis);// 延迟delayMillis耗秒隐藏toast
            try {
                mToast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 显示Toast
     *
     * @param strContent:提示肉容
     * @author :Atar
     * @createTime:2014-6-25上午10:23:59
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void show(String strContent) {
        show(CommonApplication.getContext(), strContent, 0, 0, Gravity.CENTER, 0, 3000, false);
    }

    public static void show(String strContent, int dealyMills) {
        show(CommonApplication.getContext(), strContent, 0, 0, Gravity.CENTER, 300, dealyMills, false);
    }

    public static void show(String strContent, int gravity, int offy) {
        show(CommonApplication.getContext(), strContent, 0, 0, Gravity.CENTER, offy, 3000, false);
    }

//    public static void show(String strContent, int orientation, int rotation) {
//        show(CommonApplication.getContext(), strContent, 3, 0, Gravity.CENTER, 2000, false, orientation, rotation);
//    }


    /**
     * 显示toast
     *
     * @param strContent  显示内容
     * @param widthRate   占屏幕宽度比例
     * @param heightRate  占屏幕高度比例
     * @param gravity     占屏幕位置
     * @param delayMillis 延迟多少毫秒隐藏tost
     * @author :Atar
     * @createTime:2016-3-18上午10:19:13
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void show(String strContent, int widthRate, int heightRate, int gravity, long delayMillis) {
        show(CommonApplication.getContext(), strContent, widthRate, heightRate, gravity, 300, delayMillis, false);
    }

    /**
     * 在时间间隔内不作提示
     *
     * @param strContent
     * @author :Atar
     * @createTime:2015-9-23下午3:33:03
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void showWhihPeriod(String strContent) {
        show(CommonApplication.getContext(), strContent, 0, 0, Gravity.CENTER, 0, 3000, isInToastPeriod());
    }

    /**
     * 在时间间隔内不作提示
     *
     * @param strContent
     * @author :Atar
     * @createTime:2015-9-23下午3:33:03
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void showWhihPeriod(String strContent, int delayMills) {
        show(CommonApplication.getContext(), strContent, 0, 0, Gravity.CENTER, 0, delayMills, isInToastPeriod());
    }

//    /**
//     * 检测toast权限是否打开
//     *
//     * @param context
//     * @param strContent
//     * @param mOnResultListener
//     * @author :Atar
//     * @createTime:2015-9-23下午3:32:59
//     * @version:1.0.0
//     * @modifyTime:
//     * @modifyAuthor:
//     * @description:
//     */
//    public static void showWhthCheck(Context context, String strContent, OnResultListener mOnResultListener) {
//        int resultOp = CheckPermissionUtils.checkPermiion(context, 11);
//        if (resultOp == 0) {
//            show(context, strContent, 0, 0, Gravity.CENTER, 300, 3000, false);
//        } else {
//            if (mOnResultListener != null) {
//                mOnResultListener.OnResult(resultOp);
//            }
//        }
//    }

    /**
     * @param mTextView
     * @author :Atar
     * @createTime:2015-9-23下午3:33:13
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    @SuppressWarnings("deprecation")
    private static void setText(TextView mTextView) {
        mTextView.setTextColor(Color.WHITE);
        int roundRadius = 8;
        int fillColor = Color.parseColor("#90000000");
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        mTextView.setBackgroundDrawable(gd);
    }

    /**
     * 是否在提示时间间隔内
     *
     * @return
     * @author :Atar
     * @createTime:2014-11-12上午11:26:54
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    @SuppressWarnings("unused")
    public static boolean isInToastPeriod() {
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        if (curDate != null) {
            long lastTime = 0;
            if (lastDate == null) {
                lastTime = 0;
            } else {
                lastTime = lastDate.getTime();
            }
            long timeLong = curDate.getTime() - lastTime;
            if (timeLong < toastPeriod * 1000) {
                return true;
            } else {
                lastDate = curDate;
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 消失对话框
     *
     * @author :Atar
     * @createTime:2017-2-27下午2:06:27
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void dissMissToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;// toast隐藏后，将其置为null
        }
        if (toastText != null) {
            toastText = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
