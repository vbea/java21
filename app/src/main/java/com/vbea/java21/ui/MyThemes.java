package com.vbea.java21.ui;

import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.app.AppCompatDelegate;

import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;

public class MyThemes {
    public static boolean ISCHANGED = false;
    public static int homeTextColor = 0;
    public static int homeTextShadow = 0;
    static final int[] colorPrimary = {
            R.color.title1, R.color.title2,
            R.color.title3, R.color.title3,
            R.color.title4, R.color.title4,
            R.color.title5, R.color.title6,
            R.color.title7, R.color.title7,
            R.color.title8, R.color.title8,
            R.color.title9, R.color.title10,
            R.color.title11, R.color.title12,
            R.color.title13, R.color.title14,
            R.color.title15, R.color.title16,
            R.color.title17, R.color.title17,
            R.color.title18, R.color.title18,
            R.color.title19, R.color.title20,
            R.color.title4, R.color.title20
    };

    static final int[] colorAccent = {
            R.color.title1_3, R.color.title2_3,
            R.color.title3_3, R.color.title3_a,
            R.color.title4_3, R.color.title4_a,
            R.color.title5_3, R.color.title6_3,
            R.color.title7_3, R.color.title6_a,
            R.color.title8_3, R.color.title7_a,
            R.color.title9_3, R.color.title10_3,
            R.color.title11_3, R.color.title12_3,
            R.color.title13_3, R.color.title14_3,
            R.color.title15_3, R.color.title16_3,
            R.color.title17_3, R.color.title16_a,
            R.color.title18_3, R.color.title17_a,
            R.color.title19_3, R.color.title20_3,
            R.color.title19, R.color.title19
    };
    static final int[] style = {
            R.style.AppTheme1, R.style.AppTheme2,
            R.style.AppTheme3, R.style.AppTheme4,
            R.style.AppTheme5, R.style.AppTheme6,
            R.style.AppTheme7, R.style.AppTheme8,
            R.style.AppTheme9, R.style.AppTheme10,
            R.style.AppTheme11, R.style.AppTheme12,
            R.style.AppTheme13, R.style.AppTheme14,
            R.style.AppTheme15, R.style.AppTheme16,
            R.style.AppTheme17, R.style.AppTheme18,
            R.style.AppTheme19, R.style.AppTheme20,
            R.style.AppTheme21, R.style.AppTheme22,
            R.style.AppTheme23, R.style.AppTheme24,
            R.style.AppTheme25, R.style.AppTheme26,
            R.style.AppTheme27, R.style.AppTheme28
    };

    /*static final int[] styleHome = {
        R.style.AppThemeHome1,R.style.AppThemeHome2,
        R.style.AppThemeHome3,R.style.AppThemeHome4,
        R.style.AppThemeHome5,R.style.AppThemeHome6,
        R.style.AppThemeHome7,R.style.AppThemeHome8,
        R.style.AppThemeHome9,R.style.AppThemeHome10,
        R.style.AppThemeHome11,R.style.AppThemeHome12,
        R.style.AppThemeHome13,R.style.AppThemeHome14,
        R.style.AppThemeHome15,R.style.AppThemeHome16,
        R.style.AppThemeHome17,R.style.AppThemeHome18,
        R.style.AppThemeHome19,R.style.AppThemeHome20,
        R.style.AppThemeHome21,R.style.AppThemeHome22,
        R.style.AppThemeHome23,R.style.AppThemeHome24,
        R.style.AppThemeHome25,R.style.AppThemeHome26
    };*/
    public static int[] drawerImages = {
            R.drawable.background00,
            R.drawable.background01,
            R.drawable.background02,
            R.drawable.background03,
            R.drawable.background04,
            R.drawable.background05,
            R.drawable.background06,
            R.drawable.background07,
            R.drawable.background08,
            R.drawable.background09,
            R.drawable.background10
    };
	/*static final int[] resources = {
		R.drawable.ui_item_13,
		R.drawable.ui_item_14,
		R.drawable.ui_item_15,
		R.drawable.ui_item_16,
		R.drawable.ui_item_17,
		R.drawable.ui_item_18,
		R.drawable.ui_item_19,
		R.drawable.ui_item_20,
		R.drawable.ui_item_21,
		R.drawable.ui_item_22,
		R.drawable.ui_item_23,
		R.drawable.ui_item_24,
		R.drawable.ui_item_25,
		R.drawable.ui_item_26
	};*/

    public static void initBackColor(Context context) {
        ISCHANGED = true;
        if (Common.APP_BACK_ID != 100) {
            homeTextColor = ContextCompat.getColor(context, R.color.white);
            homeTextShadow = 0;
        } else {
            try {
                Bitmap back = Common.getDrawerBack(context);
                if (back == null) {
                    Common.APP_BACK_ID = 0;
                    return;
                }
                //changed on 20170616
                homeTextShadow = ContextCompat.getColor(context, R.color.black);
                Palette p1 = new Palette.Builder(back).maximumColorCount(8).generate();
                Palette.Swatch sw = p1.getLightVibrantSwatch();
                if (sw != null) {
                    homeTextColor = sw.getRgb();
                    //homeTextShadow = context.getResources().getColor(R.color.white);
                } else
                    homeTextColor = ContextCompat.getColor(context, R.color.white);
            } catch (Exception e) {
                ExceptionHandler.log("Mytheme_init", e.toString());
            }
        }
    }

    public static void initBackColor(Context context, Bitmap bitmap) {
        ISCHANGED = true;
        if (bitmap != null) {
            try {
                //changed on 20180201
                homeTextShadow = ContextCompat.getColor(context, R.color.black);
                Palette p1 = new Palette.Builder(bitmap).maximumColorCount(8).generate();
                //.generate(bitmap, 8);
                Palette.Swatch sw = p1.getLightVibrantSwatch();
                if (sw != null) {
                    homeTextColor = sw.getRgb();
                } else
                    homeTextColor = ContextCompat.getColor(context, R.color.white);
            } catch (Exception e) {
                ExceptionHandler.log("Mytheme_init2", e.toString());
            }
        } else
            Common.APP_BACK_ID = 0;
    }

    public static int getTheme() {
        checkThemeId();
        return style[Common.APP_THEME_ID];
    }
	
	/*public static int getHomeTheme()
	{
		checkThemeId();
		return styleHome[Common.APP_THEME_ID];
	}*/

    public static int getDrawerBack() {
        if (Common.APP_BACK_ID >= drawerImages.length)
            Common.APP_BACK_ID = 0;
        return drawerImages[Common.APP_BACK_ID];
    }

    public static int getDrawerBack(int id) {
        if (id >= drawerImages.length)
            id = 0;
        return drawerImages[id];
    }

    public static void setViewBackPrimary(View v) {
        v.setBackgroundResource(getColorPrimary());
    }

    public static void setViewBackAccent(View v) {
        v.setBackgroundResource(getColorAccent());
    }

    public static void setViewBackPrimary(View v, int id) {
        v.setBackgroundResource(getColorPrimary(id));
    }

    public static void setViewBackAccent(View v, int id) {
        v.setBackgroundResource(getColorAccent(id));
    }

    public static void setViewBackPrimaryAndAccent(View p, View a, int id) {
        p.setBackgroundResource(getColorPrimary(id));
        a.setBackgroundResource(getColorAccent(id));
    }

    public static int getColorPrimary() {
        return getColorPrimary(Common.APP_THEME_ID);
    }

    public static int getColorAccent() {
        return getColorAccent(Common.APP_THEME_ID);
    }

    public static int getColorAccent(Context context) {
        if (Common.APP_THEME_ID < colorAccent.length)
            return ContextCompat.getColor(context, colorAccent[Common.APP_THEME_ID]);
        else
            return colorAccent[0];
    }

    public static int getColorPrimary(Context context) {
        if (Common.APP_THEME_ID < colorAccent.length)
            return ContextCompat.getColor(context, colorPrimary[Common.APP_THEME_ID]);
        else
            return colorPrimary[0];
    }

    public static int getColorPrimary(int id) {
        if (id < colorPrimary.length)
            return colorPrimary[id];
        else
            return colorPrimary[0];
    }

    public static int getColorAccent(int id) {
        if (id < colorAccent.length)
            return colorAccent[id];
        else
            return colorAccent[0];
    }

    private static void checkThemeId() {
        if (Common.APP_THEME_ID == -1 || Common.APP_THEME_ID >= style.length)
            Common.APP_THEME_ID = 0;
        setNightMode(isNightTheme());
    }

    public static boolean isNightTheme() {
        return Common.APP_THEME_ID > 23;
    }

    public static void setNightMode(boolean night) {
        AppCompatDelegate.setDefaultNightMode(night ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }
}
