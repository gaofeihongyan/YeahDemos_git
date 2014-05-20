
package org.yeah.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.yeah.R;
import org.yeah.view.ArcSeekBar;
import org.yeah.view.ArcSeekBar.OnSeekChangeListener;
import org.yeah.view.ArcSeekBar.OnSwitchChangeListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ArcSeekBarTestActivity extends Activity {
    /** Called when the activity is first created. */
    private static final String TAG = "KZEQActivity";
    private TextView mStatusTextView;
    private MediaPlayer mMediaPlayer;
    private Equalizer mEqualizer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // final ArcSeekbar arcSeekbar = new ArcSeekbar(this);
        // arcSeekbar.setProgress(100);

        setContentView(R.layout.activity_arc_seekbar);
        // final CircularSeekBar cs = new CircularSeekBar(this);
        final ArcSeekBar cs = (ArcSeekBar) findViewById(R.id.seekbar);
        final TextView tv = (TextView) findViewById(R.id.tv);

        cs.setMaxProgress(255);



        tv.setText(cs.getProgressPercent() + "%");

        cs.setOnSeekChangeListener(new OnSeekChangeListener() {

            @Override
            public void onProgressChange(ArcSeekBar view, float newProgress) {
                // TODO Auto-generated method stub
                //Log.i("harry", "newProgress: " + newProgress);
                //Log.i("harry", "getProgressPercent(): " + cs.getProgressPercent());
                Log.i("harry", "112222222222222222222822");
                tv.setText(cs.getProgressPercent() + "%");

            }
        });
        
        cs.setProgress(100,false);
        // cs.setAngle(90);
        
        cs.setOnSwitchChangeListener(new OnSwitchChangeListener() {
            
            @Override
            public void onSwitchChange(ArcSeekBar view, boolean SwitchStatus) {
                // TODO Auto-generated method stub
                Log.i("harry", "SwitchStatus: " + SwitchStatus);
            }
        });

    }

}
