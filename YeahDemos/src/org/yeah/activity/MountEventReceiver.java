
package org.yeah.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MountEventReceiver extends BroadcastReceiver {

    private static final String TAG = "harry";

    public void onReceive(Context context, Intent intent) {
        //if (intent == null)return;
        String action = intent.getAction();
        Log.i("harry", "yeahdemos Receive action " + action);
        
        //Intent i = new Intent(Intent.ACTION_MAIN);
         //            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.setComponent(new ComponentName("com.philips.airstudiodemo","com.philips.airstudiodemo.StartDemoActivity"));
        //context.startActivity(i);



    }
}
