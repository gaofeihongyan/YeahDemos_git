
package org.yeah.util;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tools {

    private static final String TAG = Tools.class.getSimpleName();

    private void showPopUp(Context context, View v) {
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(Color.GRAY);
        TextView tv = new TextView(context);
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setText("I'm a pop -----------------------------!");
        tv.setTextColor(Color.WHITE);
        layout.addView(tv);

        PopupWindow popupWindow = new PopupWindow(layout, 120, 120);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0],
                location[1] - popupWindow.getHeight());

        // left
        // popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
        // location[0]-popupWindow.getWidth(), location[1]);

        // right
        // popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
        // location[0]+v.getWidth(), location[1]);

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

    /**
     * create notification
     * 
     * @param status
     */
    private void createNotification(Context context, int id, String tickerText, String title,
            String contentText,
            int iconRes, PendingIntent contentIntent) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nm = (NotificationManager) context.getSystemService(ns);
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

    private void cancelNotification(Context context, int id) {
        Log.i(TAG, "\n---------------cancelNotification: id: " + id);
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(ns);
        mNotificationManager.cancel(id);
        // mNotificationManager.cancel(DOWNLOAD_PROGRESS);
        // pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    }

    private void showDialog(Context context, String msg) {
        new AlertDialog.Builder(context).setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                }).create().show();
        String str;
    }
    
}
