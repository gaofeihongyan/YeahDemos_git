
package org.yeah;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

import spider.szc.ToolUtil;

import java.util.LinkedList;
import java.util.List;

/* A way to save a global Context, using in anywhere */

public class MyApplication extends Application {
    // private List mList = new LinkedList();
    private List<Activity> activityList = new LinkedList<Activity>();
    private static MyApplication instance;

    static {
        ToolUtil.loadLib();
    }

    // ����ģʽ�л�ȡΨһ��MyApplicationʵ��
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;

    }

    // ���Activity��������
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // ��������Activity��finish

    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

    static public void msgDialog(Context context, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(text)
                .setCancelable(false)
                .setPositiveButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                            }
                        });
        builder.show();
    }
}
