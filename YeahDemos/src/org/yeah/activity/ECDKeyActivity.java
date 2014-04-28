package org.yeah.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.prefs.Preferences;

import org.yeah.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ECDKeyActivity extends Activity implements OnClickListener{

	protected static final String TAG = "harry";
    Button btn1;
	Button btn2;
	Button btn3;
	Intent intent = new Intent();
	Preferences pre;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ecd_test);
        //setContentView(new DragView(this));

        //ActivityManager am = (ActivityManager)this.getSystemService(this.ACTIVITY_SERVICE);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

    }

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.btn1:
            //String str = getContentResolver().getType(Uri.parse("content://com.asd.ECDKeyProvider"));
            //Toast.makeText(this, str, Toast.LENGTH_LONG);
		    //Log.i("harry","onClick-->insert");
		    //ContentValues values = new ContentValues(1);
		    //values.put("asd", "asd");
		    //getContentResolver().query(Uri.parse("content://com.asd.ECDKeyProvider"), null, null,null,null);
		    //String str = getContentResolver().getType(Uri.parse("content://com.asd.ECDKeyProvider"));
             String str = readECDKey();
             this.showDialog(str);
		    //Log.i("harry","getContentResolver().getType(): " + str);
		    break;
		case R.id.btn2:
		    TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		    String uid = tManager.getDeviceId();
		    Log.i("harry","uid: " + uid);
			break;
		case R.id.btn3:

			break;
		}

	}


	   public  String readECDKey(){
	        // pattern
	        // [length of compID:2][length of CTN:2][eui64:16][private key:16]
	        // [system key:16][set12NC:12][compID:v][resgistration ID:16][CTN:v]
	       String path = "/factory/ecd.bin";
	        try {
	            FileInputStream fis = new FileInputStream(new File(path));
	            Scanner sc = new Scanner(fis);

	            StringBuffer sb = new StringBuffer();

	            if (sc.hasNext()) {
	                String temp = sc.next();
	                for (int j = 0; j < temp.length(); j++) {
	                   // if (j >= 96 && j < 96 + 9)
	                        sb.append((char) (temp.charAt(j) + 3));
	                   }
	                Log.i("harry", "******readECDKey-->" + path + "-->" + sb.toString());
	                return sb.toString();
	            }

	        } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        return null;
	    }

	    private void showDialog(String msg){
	        new AlertDialog.Builder(this).setMessage(msg)
	        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.cancel();
	            }
	        }).create().show();
	    }
}

