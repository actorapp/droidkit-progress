package com.droidkit.progress.sample;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.droidkit.progress.CircularView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CircularView circularView = (CircularView) findViewById(R.id.circularView);
        circularView.setColor(Color.RED);
        circularView.post(new Runnable() {
            @Override
            public void run() {
                int val = circularView.getValue();
                if (val == 0) {
                    circularView.setValue(33);
                } else if (val == 33) {
                    circularView.setValue(66);
                } else if (val == 66) {
                    circularView.setValue(100);
                } else if (val == 100) {
                    circularView.setValue(0);
                }
                circularView.postDelayed(this, 1000);
            }
        });
    }
}
