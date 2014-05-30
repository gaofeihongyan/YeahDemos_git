
package org.yeah.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.yeah.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestActivity extends Activity implements OnClickListener {

    private static final String TAG = "TestActivity";

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Intent intent = new Intent();

    /** Called when the activity is first created. */
    // @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        // setContentView(new DragView(this));

        // ActivityManager am =
        // (ActivityManager)this.getSystemService(this.ACTIVITY_SERVICE);

        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn1.setText("Build.FINGERPRINT(ro.build.fingerprint)");
        // btn1.setVisibility(View.GONE);

        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn2.setText("DISPLAY");
        // btn2.setVisibility(View.GONE);

        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        btn3.setText("access /sdcard");
        // btn3.setVisibility(View.GONE);

        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(this);
        btn4.setText("create Notification");
        // btn4.setVisibility(View.GONE);

        btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(this);
        btn5.setText("cancel Notification");
        // btn5.setVisibility(View.GONE);

        btn6 = (Button) findViewById(R.id.btn6);
        btn6.setOnClickListener(this);
        btn6.setText("Check ecd key if exist");

    }

    // @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn1:
                break;

            case R.id.btn2:
                new AlertDialog.Builder(this).setTitle("msg")
                        .setMessage("DISPLAY： " + Build.DISPLAY)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).create().show();

                break;

            case R.id.btn3:
                // intent.setAction(Intent.ACTION_BATTERY_LOW);
                // this.sendBroadcast(intent);
                File file = new File("/sdcard");
                boolean canRead = file.canRead();
                boolean canWrite = file.canWrite();
                Log.i("harry", "canRead ： " + canRead);
                Log.i("harry", "canWrite ： " + canWrite);
                break;
            case R.id.btn4:

                Intent i = new Intent(this, ECDKeyActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, i,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                createNotification(1, "tickerText", "contentTile", "contentText",
                        android.R.drawable.arrow_down_float, null);
                break;
            case R.id.btn5:
                cancelNotification(1);
                break;

            case R.id.btn6:

                intent.setAction("com.philips.ecd.DOWNLOAD_COMPLETE");
                intent.putExtra("updateZipLocation", "/sdcard/update.zip");
                this.startActivity(intent);

                /*
                 * String key = null; boolean isExsit = new
                 * File("/factory/ecd.bin").exists(); if (isExsit) {
                 * showDialog("Congratulation! You have burn ecd key!"); } else
                 * { showDialog("Unluck ! You have not burn ecd key!"); }
                 */

                break;
        }

    }

    private void showDialog(String msg) {
        new AlertDialog.Builder(this).setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                }).create().show();
        String str;
    }

    /**
     * create notification
     * 
     * @param status
     */
    private void createNotification(int id, String tickerText, String title, String contentText,
            int iconRes, PendingIntent contentIntent) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nm = (NotificationManager) getSystemService(ns);
        // create Notification
        /*
         * final Notification notification = new
         * Notification.Builder(this).setContentTitle(title)
         * .setContentText(contentText
         * ).setTicker(tickerText).setContentIntent(contentIntent)
         * .setSmallIcon(iconRes).setOngoing(true) //
         * .setWhen(System.currentTimeMillis()) .getNotification();
         * nm.notify(id, notification);
         */
        Log.i(TAG, "\n---------------createNotification: id: " + id + "  title: " + title);
    }

    private void cancelNotification(int id) {
        Log.i(TAG, "\n---------------cancelNotification: id: " + id);
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        mNotificationManager.cancel(id);
        // mNotificationManager.cancel(DOWNLOAD_PROGRESS);
        // pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    }

    public String readKey() throws FileNotFoundException {

        FileInputStream fis = new FileInputStream(new File("/factory/ecd.bin"));
        Scanner sc = new Scanner(fis);

        StringBuffer sb = new StringBuffer();

        if (sc.hasNext()) {
            String temp = sc.next();
            for (int j = 0; j < temp.length(); j++) {
                // if (j >= 96 && j < 96 + 9)
                sb.append((char) (temp.charAt(j) + 3));
            }
            // Log.i("harry", "ecd.bin output: " + sb.toString());
            return sb.toString();
        }

        return null;
    }

}
