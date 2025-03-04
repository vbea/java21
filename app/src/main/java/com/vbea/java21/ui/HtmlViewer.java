package com.vbea.java21.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.SearchManager;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.annotation.SuppressLint;
import android.support.v7.widget.SearchView;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.data.BookMark;
import com.vbea.java21.data.WebHelper;
import com.vbea.java21.view.BookmarkDialog;
import com.vbea.java21.web.MyWebChromeClient;
import com.vbea.java21.web.MyWebViewClient;
import com.vbea.java21.web.UriScheme;
import com.vbea.java21.web.WebDialog;
import com.vbea.java21.web.WebConfig;
import com.vbea.java21.web.WebShareDialog;
import com.vbea.java21.web.WebUploadUtil;
import com.vbea.java21.web.WebUtil;
import com.vbea.java21.classes.ExceptionHandler;
import com.tencent.connect.common.Constants;
import com.vbes.util.VbeUtil;
import com.vbes.util.view.MyAlertDialog;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HtmlViewer extends BaseActivity {
    private SearchView searchView;
    public MenuItem searchItem;
    private WebView webView;
    private WebView sourceView;
    public ProgressBar webProgress, sourceProgress;
    public OkHttpClient client = new OkHttpClient();

    private int SOURCE_STATUS = -1;
    private boolean IS_SOURCE = false;
    public String currentUrl = "";
    private String lastUrl = "";
    private String lastPageTitle = "";
    private WebHelper webHelper;
    private LinearLayout coordinatorLayout;
    private FrameLayout fullVideoLayout;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private WebConfig webConfig;
    public UriScheme uriScheme;
    private WebDialog authDialog;
    public WebUploadUtil uploadUtil;
    private WebShareDialog shareDialog;

    public static final int BOOKMARK_HISTORY_RESULT_CODE = 520;

    @Override
    protected void before() {
        setContentView(R.layout.browser);
        enableBackButton(R.id.toolbar);
        setToolbarTitle("点此输入网址或搜索");
    }

    @Override
    protected void after() {
        coordinatorLayout = bind(R.id.webCoordinator);
        fullVideoLayout = bind(R.id.webFullVideo);
        webView = bind(R.id.WebViewPage);
        sourceView = bind(R.id.WebViewSource);
        TextView nightView = bind(R.id.api_nightView);
        webProgress = bind(R.id.apiProgress);
        sourceProgress = bind(R.id.sourceProgress);
        if (MyThemes.isNightTheme()) {
            nightView.setVisibility(View.VISIBLE);
        }

        //PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //webView.addJavascriptInterface(new JavaScriptShowCode(), "showcode");
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new MyWebViewClient(this));
        webView.setDownloadListener(new WebUtil.WebDownload(this));

        uriScheme = new UriScheme(this);
        webConfig = new WebConfig(getApplicationContext(), webView.getSettings());
        webConfig.applySetting(this);
        webHelper = new WebHelper(getApplicationContext());
        uploadUtil = new WebUploadUtil(this);
        shareDialog = new WebShareDialog(this);

        startService(new Intent(getApplicationContext(), DownloadService.class));
        toolbar.setVerticalScrollBarEnabled(false);

        //延迟访问网页
        mHandler.sendEmptyMessageDelayed(0, 300);

        sourceView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                sourceProgress.setVisibility(View.GONE);
            }
        });

        enableBackButton(R.id.toolbar, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IS_SOURCE)
                    goPage();
                else
                    supportFinishAfterTransition();
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (searchItem != null) {
                    searchItem.expandActionView();
                    searchView.setQuery(webView.getUrl(), false);
                }
            }
        });

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                Log.i("url", ((WebView) v).getOriginalUrl());
                if (result != null) {
                    Log.i("HitTestResult", result.getType() + "," + result.getExtra());
                    if (result.getType() == WebView.HitTestResult.IMAGE_TYPE || result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                        final String url = result.getExtra();
                        if (url.startsWith("file:"))
                            return false;
                        AlertDialog.Builder builder = new AlertDialog.Builder(HtmlViewer.this);
                        builder.setItems(new String[]{"保存图片"}, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int s) {
                                WebUtil.saveBitmap(HtmlViewer.this, url, webConfig.imgSavePath);
                               // Util.saveBitmap(HtmlViewer.this, webConfig.SH_savePath, "VBE" + System.currentTimeMillis() + ".png", bitmap);
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
                return false;
            }
        });
    }

    private void init() {
        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Intent.ACTION_VIEW:
                    loadUrls(intent.getDataString());
                    break;
                case Intent.ACTION_SEND:
                    String share = intent.getStringExtra(Intent.EXTRA_TEXT);
                    if (!Util.isNullOrEmpty(share)) {
                        if (share.indexOf("http") > 0)
                            loadUrls(share.substring(share.indexOf("http")));
                        else
                            loadUrls(share);
                    }
                    break;
                case Intent.ACTION_WEB_SEARCH:
                    loadUrlsSearch(intent.getStringExtra(SearchManager.QUERY));
                    break;
                case Intent.ACTION_PICK_ACTIVITY:
                    Common.startActivityOptions(this, DownloadFile.class);
                    break;
            }
        } else {
            if (!Util.isNullOrEmpty(webConfig.homeUrl))
                loadUrls(webConfig.homeUrl);
        }
    }

    public void loadUrls(String url) {
        if (Util.isNullOrEmpty(url))
            return;
        if (url.contains("//"))
            setToolbarTitle(url.substring(url.indexOf("//") + 2));
        else
            setToolbarTitle(url);
        //currentUrl = url;
        //if (url.indexOf("http://") == 0 || url.indexOf("https://") == 0 || url.indexOf("ftp://") == 0 || url.indexOf("file://") == 0)
        if (URLUtil.isValidUrl(url))
            webView.loadUrl(url);
        else if (url.indexOf("vbea://") == 0)
            webView.loadUrl(url.replace("vbea:", "http:"));
        else if (url.indexOf(".") > 0) {
            String s = "http://" + url;
            if (URLUtil.isValidUrl(s) && WebUtil.isUrl(s))
                webView.loadUrl(s);
            else
                loadUrlsSearch(url);
        } else
            loadUrlsSearch(url);
    }

    //搜索引擎
    private void loadUrlsSearch(String key) {
        webView.loadUrl(webConfig.getSearchEngineUrl(key));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        searchItem = menu.findItem(R.id.item_urls);
        searchView = (SearchView) searchItem.getActionView();
        if (searchView == null)
            return super.onCreateOptionsMenu(menu);
        searchItem.setActionView(searchView);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View p1, boolean p2) {
                //webView.setCanNestedScroll(!p2);
                if (!p2)
                    searchItem.collapseActionView();
            }
        });
        searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon)
                .setVisibility(View.GONE);
        searchItem.setVisible(false);
        searchView.setQueryHint("搜索或输入网址");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                loadUrls(str);
                if (IS_SOURCE)
                    goPage();
                searchView.clearFocus();
                searchItem.collapseActionView();
                //searchView.setQuery("", false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String p1) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.item_back).setVisible(webView.canGoBack() && !IS_SOURCE);
        menu.findItem(R.id.item_forward).setVisible(webView.canGoForward() && !IS_SOURCE);
        menu.findItem(R.id.item_androidshare).setVisible(!currentUrl.equals(""));
        menu.findItem(R.id.item_book_store).setVisible(!currentUrl.equals(""));
        menu.findItem(R.id.item_code).setVisible(!currentUrl.equals("") && SOURCE_STATUS != 2);
        menu.findItem(R.id.item_home).setVisible(webConfig.isValidHome());
        menu.findItem(R.id.item_code).setTitle(IS_SOURCE ? "返回" : "查看源");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_back)
            webView.goBack();
        else if (item.getItemId() == R.id.item_forward)
            webView.goForward();
        else if (item.getItemId() == R.id.item_home)
            loadUrls(webConfig.homeUrl);
        else if (item.getItemId() == R.id.item_flush) {
            if (Common.isNet(this) && !IS_SOURCE)
                webView.reload();
        } else if (item.getItemId() == R.id.item_book_store) {
            //showBookmark();
            addBookmark();
        } else if (item.getItemId() == R.id.item_addbook) {
            Common.startActivityForResult(HtmlViewer.this, Bookmark.class, BOOKMARK_HISTORY_RESULT_CODE);
        } else if (item.getItemId() == R.id.item_history)
            Common.startActivityForResult(HtmlViewer.this, History.class, BOOKMARK_HISTORY_RESULT_CODE);
        else if (item.getItemId() == R.id.item_download)
            Common.startActivityOptions(HtmlViewer.this, DownloadFile.class);
        else if (item.getItemId() == R.id.item_androidshare) {
            shareDialog.showShare(webView.getTitle(), currentUrl, webView.getFavicon());
        } else if (item.getItemId() == R.id.item_code) {
            if (Util.isNullOrEmpty(currentUrl))
                return true;
            if (IS_SOURCE)
                goPage();
            else {
                if (SOURCE_STATUS == 0) {
                    sourceProgress.setVisibility(View.VISIBLE);
                    getHtmlSourceCode();
                    //new CodeThread().start();
                } else if (SOURCE_STATUS == 1) {
                    mHandler.sendEmptyMessage(2);
                }
            }
        } else if (item.getItemId() == R.id.item_setting) {
            Common.startActivityOptions(HtmlViewer.this, BrowserSet.class);
        }
        return true;
    }

    public void goPage() {
        webView.setVisibility(View.VISIBLE);
        sourceView.setVisibility(View.GONE);
        sourceView.loadData("", "text/plain", null);
        sourceProgress.setVisibility(View.GONE);
        setToolbarTitle(webView.getTitle());
        searchItem.collapseActionView();
        IS_SOURCE = false;
    }

    public void saveImageResult(boolean success) {
        mHandler.sendEmptyMessage(success ? 3 : 4);
    }
	
	/*public void showHtmlCode()
	{
		SH_htmlUrl = currentUrl;
	}*/

    private void showBookmark() {
        if (Util.isNullOrEmpty(currentUrl)) {
            Common.startActivityForResult(HtmlViewer.this, Bookmark.class, BOOKMARK_HISTORY_RESULT_CODE);
            return;
        }
        MyAlertDialog dialog = new MyAlertDialog(this);
        dialog.setTitle(R.string.bookmark);
        dialog.setItems(R.array.array_bookmark, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface p1, int p2) {
                if (p2 == 0)
                    Common.startActivityForResult(HtmlViewer.this, Bookmark.class, BOOKMARK_HISTORY_RESULT_CODE);
                else
                    addBookmark();
                p1.dismiss();
            }
        });
        dialog.show();
    }

    private void addBookmark() {
        if (webHelper != null) {
            BookMark bookMark = new BookMark();
            bookMark.setTitle(webView.getTitle());
            bookMark.setUrl(webView.getUrl());
            BookmarkDialog.showAddDialog(this, bookMark, new BookmarkDialog.CallBack() {
                @Override
                public void onCallback(String id, BookMark target) {
                    if (webHelper.addBookmark(target.getTitle(), target.getUrl()) > 0)
                        toastShortMessage("添加书签成功");
                    else
                        toastShortMessage("添加失败");
                }
            });
        }
    }

    public void addHistory() {
        //添加历史记录 静默操作
        if (webHelper != null && !lastUrl.equals(currentUrl) && !lastPageTitle.equals(webView.getTitle())) {
            SOURCE_STATUS = 0;
            lastUrl = currentUrl;
            lastPageTitle = webView.getTitle();
            webHelper.addHistory(lastPageTitle, currentUrl);
        }
    }

    public void showCustomerView(View view, WebChromeClient.CustomViewCallback callback) {
        customViewCallback = callback;
        coordinatorLayout.setVisibility(View.INVISIBLE);
        fullVideoLayout.addView(view);
        fullVideoLayout.setVisibility(View.VISIBLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LOW_PROFILE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void hideCustomerView() {
        if (customViewCallback != null)
            customViewCallback.onCustomViewHidden();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        coordinatorLayout.setVisibility(View.VISIBLE);
        fullVideoLayout.removeAllViews();
        fullVideoLayout.setVisibility(View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    //设置回退 
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法 
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (IS_SOURCE)
                goPage();
            else if (webView.canGoBack())
                webView.goBack(); //goBack()表示返回WebView的上一页面
            else
                supportFinishAfterTransition();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && Util.hasAllPermissionsGranted(grantResults))
            uploadUtil.startCamera();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (webConfig != null)
            webConfig.applySetting(this);
        if (shareDialog != null)
            shareDialog.hide();
        if (webView != null)
            webView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case WebUploadUtil.CHOOSE_CAMERA:
                    uploadUtil.chooseCamera();
                    break;
                case WebUploadUtil.CHOOSER_FILE:
                    uploadUtil.chooseFile(data);
                    break;
                case Constants.REQUEST_QQ_SHARE:
                case Constants.REQUEST_QZONE_SHARE:
                    shareDialog.result(data);
                    break;
                case BOOKMARK_HISTORY_RESULT_CODE:
                    if (data != null)
                        loadUrls(data.getDataString());
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0)
                init();
            else if (msg.what == 1)
                webView.loadUrl(currentUrl);
            else if (msg.what == 2) {
                if (SOURCE_STATUS == 1) {
                    IS_SOURCE = true;
                    sourceView.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                    try {
                        sourceView.loadData(Uri.encode(webConfig.SH_html.toString()), "text/plain", "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setToolbarTitle("view-source:" + webView.getUrl());
                    searchView.setQuery(webView.getUrl(), false);
                } else
                    sourceProgress.setVisibility(View.GONE);
            } else if (msg.what == 3) {
                VbeUtil.toastShortMessage(getApplicationContext(), "保存成功");
            } else if (msg.what == 4) {
                VbeUtil.toastShortMessage(getApplicationContext(), "保存失败");
            }
            super.handleMessage(msg);
        }
    };

    public void getHtmlSourceCode() {
        Request request = new Request.Builder()
                .get().url(currentUrl)
                .header("User-Agent", webConfig.getUserAgent())
                .addHeader("Content-Type", "text/html; charset=UTF-8")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                SOURCE_STATUS = 2;
                ExceptionHandler.log("getHtmlSourceCode", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    SOURCE_STATUS = 1;
                    webConfig.SH_html.delete(0, webConfig.SH_html.length());
                    webConfig.SH_html.append(response.body().string());
                    Log.i("source", webConfig.SH_html.toString());
                } else {
                    SOURCE_STATUS = 2;
                }
                mHandler.sendEmptyMessage(2);
            }
        });
    }

    /*class CodeThread extends Thread implements Runnable {
        public void run() {
            try {
                byte[] bytes = Util.getHtmlByteArray(currentUrl);
                webConfig.SH_html = new String(bytes);
                SOURCE_LOAD = 1;
            } catch (Exception e) {
                SOURCE_LOAD = 2;
                ExceptionHandler.log("CodeThread", e);
            } finally {
                mHandler.sendEmptyMessage(2);
            }
        }
    }*/
}
