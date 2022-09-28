package com.vbea.java21.classes;

import android.content.Context;

import com.vbes.util.EasyPreferences;

/**
 * Created by Vbe on 2018/12/10.
 * 阅读记录存储类
 */
public class ReadUtil {
    private static ReadUtil readUtil;
    public static final String READ_Java = "read_java";
    public static final String READ_JavaAdvance = "read_java2";
    public static final String READ_J2EE = "read_javaee";
    public static final String READ_Android = "read_android";
    public static final String READ_AndroidAdvance = "read_android2";
    public static final String READ_AIDE = "read_aide";
    public static final String READ_Database = "read_database";
    private String radJava, radJava2, radJ2ee, radAndroid, radAndroid2, radAide, radDatabase;

    public static ReadUtil getInstance() {
        return readUtil;
    }

    public static void init(Context context) {
        if (readUtil == null)
            readUtil = new ReadUtil(context);
    }

    private ReadUtil(Context context) {
        radJava = EasyPreferences.getString(READ_Java, "");
        radJava2 = EasyPreferences.getString(READ_JavaAdvance, "");
        radJ2ee = EasyPreferences.getString(READ_J2EE, "");
        radAndroid = EasyPreferences.getString(READ_Android, "");
        radAndroid2 = EasyPreferences.getString(READ_AndroidAdvance, "");
        radAide = EasyPreferences.getString(READ_AIDE, "");
        radDatabase = EasyPreferences.getString(READ_Database, "");
    }

    public boolean isReadJava(String num) {
        return radJava.contains(num);
    }

    public boolean isReadJavaAdvance(String num) {
        return radJava2.contains(num);
    }

    public boolean isReadJavaEE(String num) {
        return radJ2ee.contains(num);
    }

    public boolean isReadAndroid(String num) {
        return radAndroid.contains(num);
    }

    public boolean isReadAndroidAdvance(String num) {
        return radAndroid2.contains(num);
    }

    public boolean isReadAide(String num) {
        return radAide.contains(num);
    }

    public boolean isReadDatabase(String num) {
        return radDatabase.contains(num);
    }

    public void addItemJava(String num) {
        if (!isReadJava(num))
            radJava = addReadItem(radJava, num);
    }

    public void addItemJavaAdvance(String num) {
        if (!isReadJavaAdvance(num))
            radJava2 = addReadItem(radJava2, num);
    }

    public void addItemJavaEE(String num) {
        if (!isReadJavaEE(num))
            radJ2ee = addReadItem(radJ2ee, num);
    }

    public void addItemAndroid(String num) {
        if (!isReadAndroid(num))
            radAndroid = addReadItem(radAndroid, num);
    }

    public void addItemAndroidAdvance(String num) {
        if (!isReadAndroidAdvance(num))
            radAndroid2 = addReadItem(radAndroid2, num);
    }

    public void addItemAide(String num) {
        if (!isReadAide(num))
            radAide = addReadItem(radAide, num);
    }

    public void addItemDatabase(String num) {
        if (!isReadDatabase(num))
            radDatabase = addReadItem(radDatabase, num);
    }

    private String addReadItem(String data, String num)
    {
        Common.AUDIO_STUDY_STATE+=1;
        if (Util.isNullOrEmpty(data))
            data = num;
        else
            data += "," + num;
        return data;
    }

    public void saveData() {
        EasyPreferences.putString(READ_Java, radJava);
        EasyPreferences.putString(READ_JavaAdvance, radJava2);
        EasyPreferences.putString(READ_J2EE, radJ2ee);
        EasyPreferences.putString(READ_Android, radAndroid);
        EasyPreferences.putString(READ_AndroidAdvance, radAndroid2);
        EasyPreferences.putString(READ_AIDE, radAide);
        EasyPreferences.putString(READ_Database, radDatabase);
        EasyPreferences.apply();
    }

    public void clearAllData(Context ct) {
        EasyPreferences.putString(READ_Java, "");
        EasyPreferences.putString(READ_JavaAdvance, "");
        EasyPreferences.putString(READ_J2EE, "");
        EasyPreferences.putString(READ_Android, "");
        EasyPreferences.putString(READ_AndroidAdvance, "");
        EasyPreferences.putString(READ_AIDE, "");
        EasyPreferences.putString(READ_Database, "");
        EasyPreferences.apply();
        readUtil = new ReadUtil(ct);
    }
}
