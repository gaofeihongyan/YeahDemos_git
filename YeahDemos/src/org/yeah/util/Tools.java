
package org.yeah.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.yeah.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Tools {

	private static final String TAG = Tools.class.getSimpleName();

	public static final int REQUEST_CODE_PICK_IMAGE = 0x100;
	public static final int REQUEST_CODE_CAPTURE_CAMEIA = 0x101;

	public static void getImageFromAlbum(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");// 相片类型
		activity.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
	}

	public static void getImageFromCamera(Activity activity) {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
			activity.startActivityForResult(getImageByCamera,
					REQUEST_CODE_CAPTURE_CAMEIA);
		}
		else {
			Toast.makeText(activity, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
		}
	}

	public static boolean saveImage(Bitmap photo, String spath) {
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(spath, false));
			photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Bitmap getBitmapFromUri(Activity activity, Uri uri) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					activity.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			Log(e.getMessage());
			Log("目录为：" + uri);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param context
	 * @param root
	 * @param popView
	 * @param location 0:up, 1:down, 2: left, 3:right
	 */
	public static PopupWindow showPopUp(Context context, View root, View popView,
			int popLocation) {

		PopupWindow popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		//popupWindow.setAnimationStyle(R.style.popFromBottom);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// popupWindow.update();

		int[] location = new int[2];
		root.getLocationOnScreen(location);
		Log("location[0]: " + location[0]);
		Log("location[1]: " + location[1]);
		Log("popupWindow.getWidth(): " + popupWindow.getWidth());
		Log("root.getWidth(): " + root.getWidth());

		if (popLocation == 0) {
			popupWindow.showAtLocation(root, Gravity.NO_GRAVITY,
					location[0],
					location[1] - popupWindow.getHeight());
		} else if (popLocation == 1) {
			// down
			// popupWindow.showAsDropDown(root);
			// center down
			// popupWindow.showAtLocation(root, Gravity.NO_GRAVITY,
			// location[0] - popupWindow.getWidth(), location[1]);
			// popupWindow.showAsDropDown(root, - ,0);
			popupWindow.showAtLocation(root, Gravity.NO_GRAVITY,
					location[0] - popupWindow.getWidth() / 2,
					location[1] + root.getHeight());
		} else if (popLocation == 2) {
			// left
			popupWindow.showAtLocation(root, Gravity.NO_GRAVITY,
					location[0] - popupWindow.getWidth(), location[1]);
		} else if (popLocation == 3) {
			// right
			popupWindow.showAtLocation(root, Gravity.CENTER,
					0, 0);

		}

		return popupWindow;
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

	public static int dp2px(Context context, float dipValue) {
		float m = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * m + 0.5f);
	}

	public static int px2dp(Context context, float pxValue) {
		float m = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / m + 0.5f);
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
		new AlertDialog.Builder(context)
				.setMessage(msg)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();

							}
						}).create().show();
		String str;
	}

	public static void Log(String tag, String msg) {
		Log.d(tag, "harry --> " + msg);
	}

	public static void Log(String msg) {
		Log.d("harry", "harry --> " + msg);
	}
}
