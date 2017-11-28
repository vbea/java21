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

public class Knowledge extends AppCompatActivity
{
	private ViewGroup bannerLayout;
	private TextView NightView;
	private WebView myweb;
	Toolbar tool;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apiword);

		myweb = (WebView) findViewById(R.id.WebViewApi);
		tool = (Toolbar) findViewById(R.id.toolbar);
		NightView = (TextView) findViewById(R.id.api_nightView);
		bannerLayout = (ViewGroup) findViewById(R.id.webBanner);
		if (MyThemes.isNightTheme()) NightView.setVisibility(View.VISIBLE);
		myweb.getSettings().setJavaScriptEnabled(true);
		if (Common.isSupportMD())
			myweb.removeJavascriptInterface("searchBoxJavaBridge_");
		
		int index = getIntent().getExtras().getInt("id", 0);
		tool.setTitle(getIntent().getExtras().getString("title", ""));
		setSupportActionBar(tool);
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
		
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		if (!Common.isNoadv())
			initBanner();
	}
	
	private String getUrl(int index)
	{
		switch (index)
		{
			case 0:
				return "file:///android_asset/java/debugging.html";
			case 1:
				return "file:///android_asset/java/programme.html";
			case 2:
				return "file:///android_asset/java/training1.html";
			case 3:
				return "file:///android_asset/java/audition1.html";
			case 4:
				return "file:///android_asset/java/audition2.html";
			case 5:
				return "file:///android_asset/java/audition3.html";
			case 6:
				return "file:///android_asset/java/audition4.html";
			default:
				return "";
		}
	}
	
	private void initBanner()
	{
		BannerView bannerView = new BannerView(this, ADSize.BANNER, AdvConfig.APPID, AdvConfig.Banner2);
		bannerView.setRefresh(30);
		bannerView.setADListener(null);
		bannerLayout.addView(bannerView);
		bannerView.loadAD();
	}
}
