package com.example.richard.angrykeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class SharedMemory {
    private SharedPreferences mSharedPreferences;
    public SharedMemory(Context context) {
        mSharedPreferences = context.getSharedPreferences("FILTER_PREFERENCES", Context.MODE_PRIVATE);
    }
    private int getValue(String property, int def) {
        return mSharedPreferences.getInt(property, def);
    }
    public int getAlpha() {
        return getValue("Alpha", 0x33);
    }
    public int getRed() {
        return getValue("Red", 0x33);
    }
    public int getBlue() {
        return getValue("Blue", 0x33);
    }
    public int getGreen() {
        return getValue("Green", 0x33);
    }
    private void setValue(String value, int set) {
        mSharedPreferences.edit().putInt(value, set).apply();
    }

    public void setAlpha(int value) {
        setValue("Alpha", value);
    }

    public void setRed(int value) {
        setValue("Red", value);
    }

    public void setGreen(int value) {
        setValue("Green", value);
    }

    public void setBlue(int value) {
        setValue("Blue", value);
    }

    public int getColor() {
        return Color.argb(getAlpha(), getRed(), getGreen(), getBlue());
    }
}
