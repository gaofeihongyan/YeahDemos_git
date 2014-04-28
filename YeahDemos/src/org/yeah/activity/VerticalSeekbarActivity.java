/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yeah.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import org.yeah.R;
import org.yeah.view.VerticalSeekBar;

/**
 * Demonstrates how to use a seek bar
 */
public class VerticalSeekbarActivity extends Activity implements
        VerticalSeekBar.OnSeekBarChangeListener {

    VerticalSeekBar mVerticalSeekBar;
    SeekBar progress_seek;
    TextView mProgressText;
    TextView mTrackingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vertical_seekbar);

        //progress_seek = (SeekBar) findViewById(R.id.progress_seek);
        //progress_seek.setOnSeekBarChangeListener(this);
        mVerticalSeekBar = (VerticalSeekBar) findViewById(R.id.volume_seek);
        mVerticalSeekBar.setOnSeekBarChangeListener(this);
        // mProgressText = (TextView)findViewById(R.id.progress);
        // mTrackingText = (TextView)findViewById(R.id.tracking);
    }

    @Override
    public void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress, boolean fromUser) {
        this.setTitle(progress + " " + "fromUser" + "=" + fromUser);
        //Log.d("harry", progress + " " + "fromUser" + "=" + fromUser);

    }

    @Override
    public void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(VerticalSeekBar verticalSeekBar) {
        //this.setTitle(verticalSeekBar.getProgress() + " " + "fromUser" + "=");
        //Log.d("harry", verticalSeekBar.getProgress() + " " + "fromUser" + "=");

    }
}
