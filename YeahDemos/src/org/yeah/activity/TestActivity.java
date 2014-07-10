
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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.yeah.R;
import org.yeah.util.InstallApkUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
        btn1.setText("1");
        // btn1.setVisibility(View.GONE);

        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn2.setText("2");
        // btn2.setVisibility(View.GONE);

        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        btn3.setText("3");
        // btn3.setVisibility(View.GONE);

        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(this);
        btn4.setText("4");
        // btn4.setVisibility(View.GONE);

        btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(this);
        btn5.setText("5");
        // btn5.setVisibility(View.GONE);

        btn6 = (Button) findViewById(R.id.btn6);
        btn6.setOnClickListener(this);
        btn6.setText("6");

    }

    List<byte[]> list = new ArrayList<byte[]>();
    byte[] data = new byte[] {
            8
    };

    // @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn1:
                break;

            case R.id.btn2:
                break;
            case R.id.btn3:
                break;
            case R.id.btn4:
                break;
            case R.id.btn5:
                break;
            case R.id.btn6:
                break;
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // menu.add(groupId, itemId, order, title)
        menu.add("aaa");
        menu.add("bbb");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if ("aaa".equals(item.getTitle())) {
            Toast.makeText(this, "Item aaa was chosen", Toast.LENGTH_SHORT).show();
            return true;
        } else if ("bbb".equals(item.getTitle())) {
            Toast.makeText(this, "Item bbb was chosen", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        // TODO Auto-generated method stub
        super.onContextMenuClosed(menu);
    }

}
