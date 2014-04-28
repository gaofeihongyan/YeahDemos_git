package org.yeah.activity;

import junit.framework.Assert;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AssertTest extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);

        setContentView(tv);


    }

    class myTest  extends Assert{

    }
}

