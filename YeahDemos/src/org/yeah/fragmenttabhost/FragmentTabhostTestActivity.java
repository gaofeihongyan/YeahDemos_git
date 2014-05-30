
package org.yeah.fragmenttabhost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioGroup;

import org.yeah.R;
import org.yeah.fragmenttabhost.FragmentTabAdapter.OnTabCheckedChangedListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentTabhostTestActivity extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */
    private RadioGroup mRadioGroup;
    public List<Fragment> fragments = new ArrayList<Fragment>();

    public String hello = "hello ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_tabhost_test);

        fragments.add(new TabAFm());
        fragments.add(new TabBFm());
        fragments.add(new TabCFm());
        fragments.add(new TabDFm());
        fragments.add(new TabEFm());

        mRadioGroup = (RadioGroup) findViewById(R.id.tabs_group);

        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content,
                mRadioGroup);
        
        tabAdapter.setOnTabCheckedChangedListener(new OnTabCheckedChangedListener() {
            @Override
            public void OnTabCheckedChanged(RadioGroup radioGroup, int checkedViewId, int tabIndex) {
                android.util.Log.d("harry", "Extra---- " + tabIndex + " checked!!! ");

            }
        });

    }

}
