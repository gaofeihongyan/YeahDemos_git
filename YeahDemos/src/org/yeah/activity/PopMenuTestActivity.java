
package org.yeah.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.yeah.R;
import org.yeah.util.Tools;
import org.yeah.widget.SelectPicPopupWindow;

public class PopMenuTestActivity extends Activity {

		// 自定义的弹出框类
		// SelectPicPopupWindow menuWindow;
		PopupWindow menuWindow;
		private int SHOW = 1;

		private ImageView mHeadImage;

		@Override
		public void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_popmenu);
				TextView tv = (TextView) this.findViewById(R.id.text);
				mHeadImage = (ImageView) this.findViewById(R.id.head_image);

				// 把文字控件添加监听，点击弹出自定义窗口
				tv.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
								if (SHOW == 0) {
										// 实例化SelectPicPopupWindow
										menuWindow = new SelectPicPopupWindow(
														PopMenuTestActivity.this,
														itemsOnClick);
										// 显示窗口
										menuWindow.showAtLocation(findViewById(R.id.root_layout),
														Gravity.BOTTOM, 0, 0); // 设置layout在PopupWindow中显示的位置

								} else if (SHOW == 1) {
										LayoutInflater inflater = LayoutInflater
														.from(PopMenuTestActivity.this);
										View popView = inflater.inflate(R.layout.alert_dialog,
														null, true);
										popView.findViewById(R.id.btn_take_photo)
														.setOnClickListener(
																		itemsOnClick);
										popView.findViewById(R.id.btn_pick_photo)
														.setOnClickListener(
																		itemsOnClick);
										popView.findViewById(R.id.btn_cancel)
										.setOnClickListener(
														itemsOnClick);
										menuWindow = Tools.showPopUp(PopMenuTestActivity.this, v,
														popView, 0);
								}
						}
				});
				
				
				
		}

		// 为弹出窗口实现监听类
		private OnClickListener itemsOnClick = new OnClickListener() {
				public void onClick(View v) {
						menuWindow.dismiss();
						switch (v.getId()) {
								case R.id.btn_take_photo:
										// Tools.log("itemsOnClick --> btn_take_photo");
										Tools.getImageFromCamera(PopMenuTestActivity.this);
										break;
								case R.id.btn_pick_photo:
										// Tools.log("itemsOnClick --> btn_pick_photo");
										Tools.getImageFromAlbum(PopMenuTestActivity.this);
										break;
								default:
										break;
						}
				}
		};

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				if (requestCode == Tools.REQUEST_CODE_PICK_IMAGE) {
						if (data != null) {
								Uri uri = data.getData();
								Tools.Log("onActivityResult --> REQUEST_CODE_PICK_IMAGE uri: "
												+ uri);
								// to do find the path of pic by uri
								if (mHeadImage.getVisibility() == View.GONE)
										mHeadImage.setVisibility(View.VISIBLE);
								mHeadImage.setImageBitmap(Tools.getBitmapFromUri(this, uri));
						}

				} else if (requestCode == Tools.REQUEST_CODE_CAPTURE_CAMEIA) {
						if (data != null) {
								Uri uri = data.getData();
								if (uri == null) {
										// use bundle to get data
										Bundle bundle = data.getExtras();
										if (bundle != null) {
												// get bitmap
												Bitmap photo = (Bitmap) bundle.get("data");
												// spath :生成图片取个名字和路径包含类型
												// Tools.saveImage(photo,
												// spath);
												mHeadImage.setImageBitmap(photo);
										} else {
												Toast.makeText(getApplicationContext(), "err****",
																Toast.LENGTH_LONG).show();
												return;
										}
								} else {
										// to do find the path of pic by uri
										mHeadImage.setImageBitmap(Tools.getBitmapFromUri(this, uri));
								}
								Tools.Log("onActivityResult --> REQUEST_CODE_CAPTURE_CAMEIA uri: "
												+ uri);
						}
				}
		}
}
