package com.example.firstBlood;

/**
 * Created by root on 15-9-9.
 */
import android.app.Activity;
import android.os.Bundle;

public class test2Activity extends Activity {
    MySurfaceView sv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sv=new MySurfaceView(this);
        sv.requestFocus();
        sv.setFocusableInTouchMode(true);

        setContentView(sv);
    }
}
