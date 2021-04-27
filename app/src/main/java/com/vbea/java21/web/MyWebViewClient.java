package com.vbea.java21.web;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.ui.HtmlViewer;
import com.vbes.util.VbeUtil;

/**
 * Created by Vbe on 2021/3/11.
 */
public class MyWebViewClient extends WebViewClient {
    HtmlViewer activity;

    public MyWebViewClient(HtmlViewer act) {
        activity = act;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView v, final WebResourceRequest request) {
        String url = request.getUrl().toString();
        if (!URLUtil.isValidUrl(url)) {
            if (url.toLowerCase().startsWith("vbea://")) {
                activity.currentUrl = url.replace("vbea://", "http://");
                activity.loadUrls(url);
            } else {
                if (Common.isAdminUser())
                    Util.addClipboard(activity, url);
                Log.i("url", url);
                String msg = activity.uriScheme.getAppNameByUrl(url);
                VbeUtil.showConfirmCancelDialog(activity, "提示", msg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int s) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            Util.toastShortMessage(activity, "未安装该应用");
                            //ExceptionHandler.log("HtmlDialog", e.toString());a
                        }
                    }
                });
            }
            return true;
        }
        /*else// if (!currentUrl.equals(url))
        {
            //Util.toastShortMessage(getApplicationContext(), currentUrl + "\n" + url);
            currentUrl = url;
            mHandler.sendEmptyMessageDelayed(1, 300);
            //return true;
        }*/
        return super.shouldOverrideUrlLoading(v, request);
    }

    @Override
    public void onPageStarted(WebView v, String url, Bitmap icon) {
        activity.currentUrl = url;
        if (activity.searchItem != null)
            activity.searchItem.collapseActionView();
        super.onPageStarted(v, url, icon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        activity.currentUrl = url;
        activity.addHistory();
        //view.loadUrl("javascript:window.showcode.show(document.getElementsByTagName('html')[0].outerHTML);");
        activity.setToolbarTitle(view.getTitle());
        //view.loadUrl("javascript:close_adtip();");
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedSslError(WebView v, SslErrorHandler h, SslError e) {
        WebDialog.showSecurityDialog(activity, h, e);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        //handler.proceed("admin", "admin");
        WebDialog.showWebAuthDialog(activity, host, handler);
    }
}
