package com.example.firstBlood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.ToggleButton;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    Context mContext;
    RadioButton button1;
    ToggleButton button2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        button1 = (RadioButton)findViewById(R.id.testButton1);
        button2 = (ToggleButton)findViewById(R.id.toggleButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, Run.class);
                mContext.startActivity(intent);
            }
        });
        button2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Intent intent = new Intent();
                    intent.setClass(mContext, test2Activity.class);
                    mContext.startActivity(intent);

                }else{

                }
            }
        });
    }
}
