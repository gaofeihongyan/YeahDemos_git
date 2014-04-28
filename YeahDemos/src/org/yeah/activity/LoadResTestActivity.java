package org.yeah.activity;

import org.yeah.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoadResTestActivity extends Activity implements OnClickListener{
	
	Button btn1;
	Button btn2;
	Button btn3;
	Intent intent = new Intent();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        //setContentView(new DragView(this));
        
        //ActivityManager am = (ActivityManager)this.getSystemService(this.ACTIVITY_SERVICE);
        
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn3.setText("LoadResTest");
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    
    }

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.btn1:
			intent.setAction(Intent.ACTION_HEADSET_PLUG);
			this.sendBroadcast(intent);
			break;
		case R.id.btn2:
			intent.setAction(Intent.ACTION_HEADSET_PLUG);
			this.sendBroadcast(intent);
			break;
		case R.id.btn3:
			//intent.setAction(Intent.ACTION_BATTERY_LOW);
			//this.sendBroadcast(intent);btn1
		    Configuration config = getResources().getConfiguration();
            new AlertDialog.Builder(this)
            .setTitle("msg")
            .setMessage(config.toString())
            .setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    })
            .create().show();
                    
		    Log.i("harry","config: " + config.toString());
			break;
		}
		
	}
}

