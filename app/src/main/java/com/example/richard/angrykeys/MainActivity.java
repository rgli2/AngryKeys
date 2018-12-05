package com.example.richard.angrykeys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CREATION", "onCreate() executed");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv2 = (TextView) findViewById(R.id.textView2);
        SeekBar sb1 = (SeekBar) findViewById(R.id.seekBar);
        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser)
            {
                //---change the font size of the EditText---
                tv2.setText(String.valueOf(progress));
            }
        });
    }
}
