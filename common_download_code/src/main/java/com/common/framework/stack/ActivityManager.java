package com.common.framework.stack;

import android.app.Activity;

import java.util.Stack;

public class ActivityManager {
    private Stack<Activity> activityStack;
    private static ActivityManager instance;

    public static ActivityManager getActivityManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public void setActivityStack(Stack<Activity> activityStack) {
        this.activityStack = activityStack;
    }

    /**
     * 指定activity退出栈
     *
     * @param activity
     * @author :Atar
     * @createTime:2011-9-5下午3:31:00
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 得到栈顶activity
     *
     * @return
     * @author :Atar
     * @createTime:2011-9-5下午3:32:20
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack != null && !activityStack.empty())
            activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 将当前activity压入栈
     *
     * @param activity
     * @author :Atar
     * @createTime:2011-9-5下午3:33:13
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 除指定的activity其余退出栈
     *
     * @param cls
     * @author :Atar
     * @createTime:2011-9-5下午3:30:06
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public <A extends Activity> void popAllActivityExceptOne(Class<A> cls) {
        if (activityStack == null) {
            return;
        }
        try {
            for (int i = activityStack.size() - 1; i >= 0; i--) {
                try {
                    if (activityStack.get(i) != null && activityStack.get(i).getClass() != null && !activityStack.get(i).getClass().equals(cls)) {
                        activityStack.get(i).finish();
                        activityStack.remove(i);
                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 退出所有activity
     *
     * @author :Atar
     * @createTime:2011-9-5下午3:28:39
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void popAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (!activity.isFinishing()) {
                activity.finish();
            }
            popActivity(activity);
        }
    }

    /**
     * 得到前一个activity
     *
     * @return
     * @author :Atar
     * @createTime:2014-9-5下午2:18:44
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public Activity getPreviousActivity() {
        if (activityStack == null || activityStack.size() == 0 || activityStack.size() < 2) {
            return null;
        }
        return activityStack.get(activityStack.size() - 2);
    }

    /**
     * 倒数第几个Activity
     *
     * @param lastPosition
     * @return
     * @author :Atar
     * @createTime:2016-6-16下午7:30:58
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public Activity getActivity(int lastPosition) {
        if (activityStack == null || activityStack.size() == 0 || activityStack.size() < lastPosition) {
            return null;
        }
        return activityStack.get(activityStack.size() - lastPosition);
    }

    /**
     * 得到指定activity
     *
     * @param cls
     * @return
     * @author :Atar
     * @createTime:2014-9-5下午2:21:04
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    @SuppressWarnings("unchecked")
    public <A extends Activity> A getActivity(Class<A> cls) {
        if (activityStack == null) {
            return null;
        }
        A a = null;
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            try {
                if (activityStack.get(i) != null && activityStack.get(i).getClass() != null && activityStack.get(i).getClass().equals(cls)) {
                    a = (A) activityStack.get(i);
                    break;
                }
            } catch (Exception e) {

            }
        }
        return a;
    }

    /**
     * 得到指定activity 倒数第几个 从倒数第0开始算
     *
     * @param cls
     * @return
     * @author :Atar
     * @createTime:2014-9-5下午2:21:04
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    @SuppressWarnings("unchecked")
    public <A extends Activity> A getActivity(Class<A> cls, int lastPosition) {
        if (activityStack == null) {
            return null;
        }
        A a = null;
        int tempLastPosition = 0;
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            try {
                if (activityStack.get(i) != null && activityStack.get(i).getClass() != null && activityStack.get(i).getClass().equals(cls)) {
                    if (tempLastPosition == lastPosition) {
                        a = (A) activityStack.get(i);
                        break;
                    }
                    tempLastPosition++;
                }
            } catch (Exception e) {

            }
        }
        return a;
    }

    /**
     * 关闭指定activity同时也将此activity退出栈
     *
     * @param cls
     * @author :Atar
     * @createTime:2014-9-5下午2:32:12
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description: 从最后算起，若有相同的关闭最后一个 用i--
     */
    public <A extends Activity> void finishActivity(Class<A> cls) {
        if (activityStack == null) {
            return;
        }
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            try {
                if (activityStack.get(i) != null && activityStack.get(i).getClass() != null && activityStack.get(i).getClass().equals(cls)) {
                    activityStack.get(i).finish();
                    activityStack.remove(i);
                    break;
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 关闭指定Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        try {
            if (activity != null) {
                finishActivity(activity.getClass());
            }
        } catch (Exception e) {

        }
    }

    /**
     * 关闭两个Activity
     *
     * @param clsA
     * @param clsB
     * @author :Atar
     * @createTime:2015-11-9下午4:58:15
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public <A extends Activity, B extends Activity> void finishActivity2(Class<A> clsA, Class<B> clsB) {
        if (activityStack == null) {
            return;
        }
        int flag = 0;
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (flag == 2) {
                break;
            }
            try {
                if (activityStack.get(i) != null && activityStack.get(i).getClass() != null && activityStack.get(i).getClass().equals(clsA)) {
                    activityStack.get(i).finish();
                    activityStack.remove(i);
                    flag++;
                } else if (activityStack.get(i) != null && activityStack.get(i).getClass() != null && activityStack.get(i).getClass().equals(clsB)) {
                    activityStack.get(i).finish();
                    activityStack.remove(i);
                    flag++;
                }
            } catch (Exception e) {

            }
        }
    }

    public void finishActivity3(Class... clsA) {
        if (activityStack == null) {
            return;
        }
        for (int k = clsA.length - 1; k >= 0; k--) {
            for (int i = activityStack.size() - 1; i >= 0; i--) {
                try {
                    if (activityStack.size() > i && activityStack.get(i) != null && activityStack.get(i).getClass() != null && activityStack.get(i).getClass().equals(clsA[k])) {
                        activityStack.get(i).finish();
                        activityStack.remove(i);
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * 关闭当前activity同时退出栈
     *
     * @author :Atar
     * @createTime:2014-9-5下午3:27:55
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void finishCurrentActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            activityStack.get(activityStack.size() - 1).finish();
            activityStack.remove(activityStack.size() - 1);
        }
    }


    /**
     * 退出程序
     *
     * @author :Atar
     * @createTime:2016-8-18下午2:30:03
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public void exitApplication() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                popAllActivity();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出
            }
        }.start();
    }
}
