
package org.yeah.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import org.yeah.R;
import org.yeah.view.CheckSwitchButton;
import org.yeah.view.SlideSwitchView;
import org.yeah.view.SlideSwitchView.OnSwitchChangedListener;

/**
 * @author RA
 * @blog http://blog.csdn.net/vipzjyno1
 */
public class CompoundButtonTestActivity extends Activity {
    private ToggleButton mTogBtn;
    private CheckSwitchButton mCheckSwithcButton;
    private CheckSwitchButton mEnableCheckSwithcButton;
    private SlideSwitchView mSlideSwitchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compound_button_test);
        initView();
    }

    private void initView() {
        mTogBtn = (ToggleButton) findViewById(R.id.mTogBtn);
        mTogBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                } else {
                }
            }
        });
        mCheckSwithcButton = (CheckSwitchButton) findViewById(R.id.mCheckSwithcButton);
        mEnableCheckSwithcButton = (CheckSwitchButton) findViewById(R.id.mEnableCheckSwithcButton);
        mCheckSwithcButton.setChecked(false);
        mCheckSwithcButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    mEnableCheckSwithcButton.setEnabled(false);
                    mSlideSwitchView.setEnabled(false);
                } else {
                    mEnableCheckSwithcButton.setEnabled(true);
                    mSlideSwitchView.setEnabled(true);
                }
            }
        });

        
        /**
         * Test SlideSwitchView
         */
        mSlideSwitchView = (SlideSwitchView) findViewById(R.id.mSlideSwitchView);
        mSlideSwitchView.setOnChangeListener(new OnSwitchChangedListener() {

            @Override
            public void onSwitchChange(SlideSwitchView switchView, boolean isChecked) {
                // TODO Auto-generated method stub
                    Log.d("harry","isChecked: " + isChecked);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_compound_button, menu);
        return true;
    }

}
