package com.vbea.java21;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.view.ViewGroup;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.Context;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.AdvConfig;
import com.qq.e.comm.util.AdError;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.banner.AbstractBannerADListener;

public class Knowledge extends BaseActivity
{
	private ViewGroup bannerLayout;
	private TextView NightView;
	private WebView myweb;

	@Override
	protected void before()
	{
		setContentView(R.layout.apiword);
	}

	@Override
	protected void after()
	{
		enableBackButton();
		myweb = (WebView) findViewById(R.id.WebViewApi);
		NightView = (TextView) findViewById(R.id.api_nightView);
		bannerLayout = (ViewGroup) findViewById(R.id.webBanner);
		if (MyThemes.isNightTheme()) NightView.setVisibility(View.VISIBLE);
		myweb.getSettings().setJavaScriptEnabled(true);
		if (Common.isSupportMD())
			myweb.removeJavascriptInterface("searchBoxJavaBridge_");
		String index = getIntent().getExtras().getString("url", "");
		toolbar.setTitle(getIntent().getExtras().getString("title", ""));
		myweb.loadUrl(getUrl(index));
		WebSettings set = myweb.getSettings();
		switch (Common.JAVA_TEXT_SIZE)
		{
			case 0:
				set.setTextSize(WebSettings.TextSize.SMALLEST);
				break;
			case 1:
				set.setTextSize(WebSettings.TextSize.SMALLER);
				break;
			case 2:
				set.setTextSize(WebSettings.TextSize.NORMAL);
				break;
			case 3:
				set.setTextSize(WebSettings.TextSize.LARGER);
				break;
			case 4:
				set.setTextSize(WebSettings.TextSize.LARGEST);
				break;
		}
		myweb.setWebViewClient(new WebViewClient());
		if (!Common.isNoadv())
			initBanner();
	}
	
	private String getUrl(String url)
	{
		return "file:///android_asset/" + url;
	}
	
	private void initBanner()
	{
		BannerView bannerView = new BannerView(this, ADSize.BANNER, AdvConfig.APPID, AdvConfig.BannerSecond);
		bannerView.setRefresh(30);
		bannerView.setADListener(null);
		bannerLayout.addView(bannerView);
		bannerView.loadAD();
	}
}
