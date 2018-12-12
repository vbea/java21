package com.vbea.java21.classes;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vbe on 2018/9/18.
 */

public class EasyPreferences
{
    private SharedPreferences spf;
    private SharedPreferences.Editor edt;

    public EasyPreferences(Context context) {
        spf = context.getSharedPreferences("java21", Context.MODE_PRIVATE);
    }
    public EasyPreferences(Context context, String name) {
        spf = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public String getString(String var1, String var2) {
        return spf.getString(var1, var2);
    }

    public String getString(String var1) {
        return getString(var1, "");
    }

    public int getInt(String var1, int var2) {
        return spf.getInt(var1, var2);
    }

    public long getLong(String var1, long var2) {
        return spf.getLong(var1, var2);
    }

    public float getFloat(String var1, float var2) {
        return spf.getFloat(var1, var2);
    }

    public boolean getBoolean(String var1, boolean var2) {
        return spf.getBoolean(var1, var2);
    }

    public boolean contains(String var1) {
        return spf.contains(var1);
    }

    public void putString(String var1, String var2) {
        getEditor().putString(var1, var2);
    }

    public SharedPreferences.Editor putInt(String var1, int var2) {
        return getEditor().putInt(var1, var2);
    }

    public SharedPreferences.Editor putLong(String var1, long var2) {
        return getEditor().putLong(var1, var2);
    }

    public SharedPreferences.Editor putFloat(String var1, float var2) {
        return getEditor().putFloat(var1, var2);
    }

    public SharedPreferences.Editor putBoolean(String var1, boolean var2) {
        return getEditor().putBoolean(var1, var2);
    }

    public boolean commit() {
        return getEditor().commit();
    }

    public void apply() {
        getEditor().apply();
    }

    private SharedPreferences.Editor getEditor() {
        if (edt == null)
            edt = spf.edit();
        return edt;
    }
}
