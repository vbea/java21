package com.vbea.java21.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebSettings;

import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbes.util.EasyPreferences;
import com.vbes.util.VbeUtil;

/**
 * Created by Vbe on 2021/3/11.
 */
public class WebConfig {
    private WebSettings webSettings;
    private String UA_Default = "";
    public StringBuilder SH_html = new StringBuilder();

    public String homeUrl = "";
    public String imgSavePath = "";
    private String[] Searches, UserAgents;
    private int searchEngine = 0;
    private int uaIndex = -1;

    
    public WebConfig(Context context, WebSettings settings) {
        webSettings = settings;
        config(context);
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private void config(Context context) {
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        //webSettings.setAppCacheMaxSize(1024*1024*10);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(context.getCacheDir().getPath());
        webSettings.setBlockNetworkImage(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDatabaseEnabled(true);
        //webSettings.setDatabasePath(getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath());
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true); //html5
        webSettings.setGeolocationEnabled(true);
        webSettings.setGeolocationDatabasePath(context.getDir("database", Context.MODE_PRIVATE).getPath());
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setSupportZoom(true);
        webSettings.setSupportMultipleWindows(true);
        if (VbeUtil.isAndroidO()) {
            webSettings.setSafeBrowsingEnabled(false);
        }
        //webSettings.setSaveFormData(true);
        //webSettings.setPluginState(WebSettings.PluginState.ON);
        //webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setUseWideViewPort(true);
        webSettings.setUserAgentString(webSettings.getUserAgentString());

        UA_Default = webSettings.getUserAgentString();
    }

    public void applySetting(Context context) {
        if (Searches == null)
            Searches = context.getResources().getStringArray(R.array.array_search_url);
        if (UserAgents == null) {
            UserAgents = context.getResources().getStringArray(R.array.array_ua);
            UserAgents[0] = UA_Default + " VbeBrowser/1.0";
        }

        homeUrl = EasyPreferences.getString("web_home", "");
        searchEngine = EasyPreferences.getInt("web_search", 0);
        imgSavePath = EasyPreferences.getString("web_savepath", Common.ExterPath + "/DCIM/Java21");
        int ua = EasyPreferences.getInt("web_ua", 0);
        if (uaIndex != ua) {
            uaIndex = ua;
            setUserAgent();
        }
    }
    
    public String getDefaultUA() {
        return UA_Default;
    }

    public String getUserAgent() {
        return UserAgents[uaIndex];
    }

    public String getSearchEngineUrl(String key) {
        return String.format(Searches[searchEngine], key);
    }

    public void setUserAgent() {
        webSettings.setUserAgentString(getUserAgent());
    }

    public boolean isValidHome() {
        if (homeUrl.length() > 0) {
            //ExceptionHandler.log("home", SH_home);
            int begin = homeUrl.indexOf("://");
            if (begin > 0) {
                return homeUrl.substring(begin).length() > 0;
            }
        }
        return false;
    }


}
