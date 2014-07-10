
package org.yeah.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.yeah.R;
import org.yeah.view.SmileVolumeSeekBar;
import org.yeah.view.SmileVolumeSeekBar.OnSeekChangeListener;
import org.yeah.view.SmileVolumeSeekBar;

public class SmileVolumeSeekBarTestActivity extends Activity {
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

        setContentView(R.layout.activity_smile_volume_seekbar);
        // final CircularSeekBar cs = new CircularSeekBar(this);
        final SmileVolumeSeekBar cs = (SmileVolumeSeekBar) findViewById(R.id.seekbar);
        final TextView tv = (TextView) findViewById(R.id.tv);

        cs.setMaxProgress(8);

        cs.setOnSeekChangeListener(new OnSeekChangeListener() {

            @Override
            public void onProgressChange(SmileVolumeSeekBar view, float newProgress) {
                // TODO Auto-generated method stub
                // Log.i("harry", "newProgress: " + newProgress);
                // Log.i("harry", "getProgressPercent(): " +
                // cs.getProgressPercent());
                Log.i("harry", "112222222222222222222822");
                tv.setText("ProgressPercent: " + cs.getProgressPercent() + "%--" + "progress: "
                        + cs.getProgress());

            }
        });

        tv.setText("ProgressPercent: " + cs.getProgressPercent() + "%--" + "progress: "
                + cs.getProgress());
        
        cs.setProgress(9, false);
        //cs.setProgressPercent(50,true);
        // cs.setAngle(90);

    }

}
