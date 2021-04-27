package com.vbea.java21.web;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vbea.java21.ui.HtmlViewer;
import com.vbes.util.VbeUtil;
import com.vbes.util.lis.DialogResult;

/**
 * Created by Vbe on 2021/3/11.
 */
public class MyWebChromeClient extends WebChromeClient {
    HtmlViewer activity;

    public MyWebChromeClient(HtmlViewer act) {
        activity = act;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        activity.setToolbarTitle(title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        activity.webProgress.setProgress(newProgress);
        activity.webProgress.setVisibility(newProgress >= 100 ? View.INVISIBLE : View.VISIBLE);
    }

    public void onGeolocationPermissionsShowPrompt(final String origin, final android.webkit.GeolocationPermissions.Callback callback) {
        VbeUtil.showConfirmCancelDialog(activity, "提示", "来自" + origin + "的网页正在请求定位，是否允许？", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int s) {
                callback.invoke(origin, true, false);
            }
        });
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(getNewWindow(view));
        resultMsg.sendToTarget();
        return true;
    }

    /*public void onExceededDatabaseQuota(String url, String databasein, long quota, long estime, long totalq, WebStorage.QuotaUpdater update)
    {
        update.updateQuota(5 * 1024 * 1024);
    }

    // For Android 3.0-
    public void openFileChooser(ValueCallback<Uri> uploadMsg)
    {
        openFileChooser(uploadMsg, "", "");
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType)
    {
        openFileChooser(uploadMsg, acceptType, "");
    }*/

    // For Android 4.1
    /*public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
    {
        if (mUploadMessage != null)
            mUploadMessage.onReceiveValue(null);
        mUploadMessage = uploadMsg;
        String type = Util.isNullOrEmpty(acceptType) ? "*//*" : acceptType;
        if (type.equals("image/*"))
            selectImage();
        else
        {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType(type);
            startActivityForResult(Intent.createChooser(i, "上传文件"), FILECHOOSER_RESULTCODE);
        }
    }*/

    //Android 5.0+
    @Override
    @SuppressLint("NewApi")
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        activity.uploadUtil.onFileChoose(filePathCallback, fileChooserParams);
        return true;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        activity.showCustomerView(view, callback);
    }

    @Override
    public void onHideCustomView() {
        activity.hideCustomerView();
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return super.onConsoleMessage(consoleMessage);
    }

    private WebView getNewWindow(WebView view) {
        WebView newWebView = new WebView(view.getContext());
        newWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                VbeUtil.showConfirmCancelDialog(activity, "拦截提醒", "网页弹出一个新窗口已被拦截，URL为" + url, "访问", "取消", new DialogResult() {
                    @Override
                    public void onConfirm() {
                        activity.loadUrls(url);
                    }

                    @Override
                    public void onCancel() {
                        /*Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setClass(HtmlViewer.this, HtmlViewer.class);
						intent.setData(Uri.parse(view.getUrl()));
						Common.startActivityOptions(HtmlViewer.this, intent);*/
                    }
                });
                return true;
            }
        });
        return newWebView;
    }
}
