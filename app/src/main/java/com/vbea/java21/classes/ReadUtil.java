package com.vbea.java21.classes;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.http.bean.Init;

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
    private EasyPreferences spf;

    public static ReadUtil getInstance() {
        return readUtil;
    }

    public static void init(Context context) {
        if (readUtil == null)
            readUtil = new ReadUtil(context);
    }

    private ReadUtil(Context context) {
        spf = new EasyPreferences(context);
        radJava = spf.getString(READ_Java, "");
        radJava2 = spf.getString(READ_JavaAdvance, "");
        radJ2ee = spf.getString(READ_J2EE, "");
        radAndroid = spf.getString(READ_Android, "");
        radAndroid2 = spf.getString(READ_AndroidAdvance, "");
        radAide = spf.getString(READ_AIDE, "");
        radDatabase = spf.getString(READ_Database, "");
    }

    public boolean isReadJava(String num) {
        return radJava.indexOf(num) > -1;
    }

    public boolean isReadJavaAdvance(String num) {
        return radJava2.indexOf(num) > -1;
    }

    public boolean isReadJavaEE(String num) {
        return radJ2ee.indexOf(num) > -1;
    }

    public boolean isReadAndroid(String num) {
        return radAndroid.indexOf(num) > -1;
    }

    public boolean isReadAndroidAdvance(String num) {
        return radAndroid2.indexOf(num) > -1;
    }

    public boolean isReadAide(String num) {
        return radAide.indexOf(num) > -1;
    }

    public boolean isReadDatabase(String num) {
        return radDatabase.indexOf(num) > -1;
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
        spf.putString(READ_Java, radJava);
        spf.putString(READ_JavaAdvance, radJava2);
        spf.putString(READ_J2EE, radJ2ee);
        spf.putString(READ_Android, radAndroid);
        spf.putString(READ_AndroidAdvance, radAndroid2);
        spf.putString(READ_AIDE, radAide);
        spf.putString(READ_Database, radDatabase);
        spf.apply();
    }

    public void clearAllData(Context ct) {
        spf.putString(READ_Java, "");
        spf.putString(READ_JavaAdvance, "");
        spf.putString(READ_J2EE, "");
        spf.putString(READ_Android, "");
        spf.putString(READ_AndroidAdvance, "");
        spf.putString(READ_AIDE, "");
        spf.putString(READ_Database, "");
        spf.commit();
        readUtil = new ReadUtil(ct);
    }
}
