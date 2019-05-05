package com.vbea.java21;

import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.support.design.widget.BottomSheetDialog;

public class ApiWord extends BaseActivity
{
	private WebView myweb;
	private ProgressBar proGro;
	private TextView NightView;
	private final String api12Url = "http://docs.oracle.com/en/java/javase/12/docs/api/index.html";
	private final String api11Url = "http://docs.oracle.com/en/java/javase/11/docs/api/index.html";
	private final String api10Url = "http://docs.oracle.com/javase/10/docs/api/overview-summary.html";
	private final String api9Url = "http://docs.oracle.com/javase/9/docs/api/overview-summary.html";
	private final String api8Url = "http://docs.oracle.com/javase/8/docs/api/overview-summary.html";
	private final String api7Url = "http://docs.oracle.com/javase/7/docs/api/overview-summary.html";
	private final String api6Url = "http://docs.oracle.com/javase/6/docs/api/overview-summary.html";
	private final String apiUrlCH = "http://www.javaweb.cc/help/JavaAPI1.6/overview-summary.html";
	private final String apiAndroid = "http://androiddoc.qiniudn.com/reference/packages.html";
	private final String sub = "API文档";

	@Override
	protected void before()
	{
		setContentView(R.layout.apiword);
	}
	
	@Override
	protected void after()
	{
		enableBackButton();
		proGro = bind(R.id.apiProgress);
		myweb = bind(R.id.WebViewApi);
		NightView = bind(R.id.api_nightView);
		if (MyThemes.isNightTheme()) NightView.setVisibility(View.VISIBLE);
		WebSettings set = myweb.getSettings();
		set.setJavaScriptEnabled(true);
		set.setLoadWithOverviewMode(true);
		set.setBuiltInZoomControls(true);
		set.setDisplayZoomControls(false);
		set.setSupportZoom(true);
		if (android.os.Build.VERSION.SDK_INT < 19)
			myweb.removeJavascriptInterface("searchBoxJavaBridge_");
		//myweb.loadUrl("http://www.vbes.tk/JavaAPI/index.html");
		new Handler().postDelayed(new Runnable()
		{
			public void run()
			{
				myweb.loadUrl(api6Url);
			}
		}, 500);
		/*myweb.setFocusable(true);
		myweb.setFocusableInTouchMode(true);
		myweb.requestFocus();*/
		myweb.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView v, final String url)
			{
				if (!url.startsWith("http"))
				{
					return true;
				}
				return super.shouldOverrideUrlLoading(v, url);
			}
		});
		
		myweb.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				proGro.setProgress(newProgress);
				proGro.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
			}
		});
		
		/*btn_goto.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				myweb.goForward();
				popwin.dismiss();
			}
		});*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.word_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.item_flush:
				myweb.reload();
				break;
			case R.id.item_apiCN:
				myweb.loadUrl(apiUrlCH);
				break;
			case R.id.item_api6:
				myweb.loadUrl(api6Url);
				break;
			case R.id.item_api7:
				myweb.loadUrl(api7Url);
				break;
			case R.id.item_api8:
				myweb.loadUrl(api8Url);
				break;
			case R.id.item_api9:
				myweb.loadUrl(api9Url);
				break;
			case R.id.item_api10:
				myweb.loadUrl(api10Url);
				break;
			case R.id.item_api11:
				myweb.loadUrl(api11Url);
				break;
			case R.id.item_api12:
				myweb.loadUrl(api12Url);
				break;
			case R.id.item_apiand:
				myweb.loadUrl(apiAndroid);
				break;
		}
		return true;
	}
	
	@Override
    //设置回退 
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法 
    public boolean onKeyDown(int keyCode, KeyEvent event)
	{ 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myweb.canGoBack())
		{ 
            myweb.goBack(); //goBack()表示返回WebView的上一页面 
            return true; 
        } 
        return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

}
