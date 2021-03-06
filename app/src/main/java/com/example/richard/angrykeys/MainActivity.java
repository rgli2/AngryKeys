package com.example.richard.angrykeys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private SharedMemory mSharedMemory;
    private Switch mSwitch;
    private static boolean filterOn = false;
    private static int maxRed = 0;

    public static int getMaxRed() {
        return maxRed;
    }

    /**
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Bundle extras = intent.getExtras();
            if (extras != null) {
                {
                    String rec_data = extras.getString("send_data");
                    Log.d("Received Msg : ", rec_data);
                }
            }
        };
    };
     */

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Log.d("CREATION", "onCreate() executed");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            final TextView tv2 = (TextView) findViewById(R.id.textView2);
            SeekBar sb1 = (SeekBar) findViewById(R.id.seekBar);
            tv2.setText("0");
            sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                Intent i = new Intent(MainActivity.this, ScreenFilterService.class);
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    stopService(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    //---change the font size of the EditText---
                    tv2.setText(String.valueOf(progress));
                    if (mSwitch.isChecked()) {
                        //stopService(i);
                        mSharedMemory.setAlpha(progress * 255 / 100);
                        startService(i);
                        maxRed = progress * 255 / 100;
                    }
                    mSharedMemory.setAlpha(progress * 255 / 100);
                    maxRed = progress * 255 / 100;
                }
            });
            mSwitch = findViewById(R.id.switch1);
            mSharedMemory = new SharedMemory(this);

            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Intent i = new Intent(MainActivity.this, ScreenFilterService.class);
                    boolean state = !mSwitch.isChecked();
                    if (state) {
                        filterOn = false;
                        stopService(i);
                    } else {
                        /**
                        mSharedMemory.setRed(255);
                        mSharedMemory.setGreen(0);
                        mSharedMemory.setBlue(0);
                         */
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (!Settings.canDrawOverlays(MainActivity.this)) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, 1234);
                            } else {
                                filterOn = true;
                                //startService(i);
                            }
                        } else {
                            filterOn = true;
                            //startService(i);
                        }
                    }
                }
            });
        }
        public static boolean filterOn() {
            return filterOn;
        }

    }
/**
 http://api.datumbox.com/1.0/SentimentAnalysis.json?api_key=ce30c2175b219dc10f252483d6b0b632&text=eat my ass
 */