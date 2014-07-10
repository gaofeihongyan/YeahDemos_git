
package org.yeah.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.yeah.R;
import org.yeah.util.Tools;
import org.yeah.widget.SelectPicPopupWindow;

public class PopMenuTestActivity extends Activity {

	  // 自定义的弹出框类
	  // SelectPicPopupWindow menuWindow;
	  PopupWindow menuWindow;
	  private int SHOW = 1;

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_popmenu);
			TextView tv = (TextView) this.findViewById(R.id.text);
			// 把文字控件添加监听，点击弹出自定义窗口
			tv.setOnClickListener(new OnClickListener() {
				  public void onClick(View v) {
						if (SHOW == 0) {
							  // 实例化SelectPicPopupWindow
							  menuWindow = new SelectPicPopupWindow(PopMenuTestActivity.this,
										  itemsOnClick);
							  // 显示窗口
							  menuWindow.showAtLocation(findViewById(R.id.root_layout),
										  Gravity.BOTTOM, 0, 0); // 设置layout在PopupWindow中显示的位置

						} else if (SHOW == 1) {
							  LayoutInflater inflater = LayoutInflater
										  .from(PopMenuTestActivity.this);
							  View popView = inflater.inflate(R.layout.alert_dialog, null, true);
							  popView.findViewById(R.id.btn_take_photo).setOnClickListener(
										  itemsOnClick);
							  popView.findViewById(R.id.btn_pick_photo).setOnClickListener(
										  itemsOnClick);
							  menuWindow = Tools.showPopUp(PopMenuTestActivity.this, v, popView, 1);
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
							  Tools.log("itemsOnClick --> btn_take_photo");
							  break;
						case R.id.btn_pick_photo:
							  Tools.log("itemsOnClick --> btn_pick_photo");
							  break;
						default:
							  break;
				  }

			}

	  };

}
