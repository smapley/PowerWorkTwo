package com.smapley.powerwork.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.smapley.powerwork.R;

/**
 * Created by smapley on 2015/5/1.
 */
public class test extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.main_fragment);
    }
}
