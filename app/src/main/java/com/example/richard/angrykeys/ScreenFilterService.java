package com.example.richard.angrykeys;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class ScreenFilterService extends Service implements KeyEvent.Callback{


    private SharedMemory mSharedMemory;
    private View mView;

    public ScreenFilterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSharedMemory = new SharedMemory(this);
        mView = new LinearLayout(this);
        mView.setBackgroundColor(mSharedMemory.getColor());

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                280,
                PixelFormat.TRANSLUCENT
        );
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        assert windowManager != null;
        windowManager.addView(mView, layoutParams);
        Log.d("TAG", "A");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        assert windowManager != null;
        windowManager.removeView(mView);
        Log.d("TAG", "B");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mView.setBackgroundColor(mSharedMemory.getColor());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        Log.d("asdf","SOMETHING HAPPENED");
        return true;
    }

    @Override
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_D :
                Log.d("KeyPress", "D");
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyMultiple(int i, int i1, KeyEvent keyEvent) {
        return false;
    }
}
