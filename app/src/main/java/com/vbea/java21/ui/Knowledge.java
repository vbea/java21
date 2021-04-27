package com.vbea.java21.ui;

import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.AdvConfig;

public class Knowledge extends BaseActivity
{
	private ViewGroup bannerLayout;
	private TextView NightView;
	private WebView myweb;

	@Override
	protected void before()
	{
		setContentView(R.layout.apiword);
		setToolbarTitle(getIntent().getExtras().getString("title", ""));
	}

	@Override
	protected void after()
	{
		enableBackButton();
		myweb = bind(R.id.WebViewApi);
		NightView = bind(R.id.api_nightView);
		bannerLayout = bind(R.id.webBanner);
		if (MyThemes.isNightTheme()) NightView.setVisibility(View.VISIBLE);
		myweb.getSettings().setJavaScriptEnabled(true);
		if (Common.isSupportMD())
			myweb.removeJavascriptInterface("searchBoxJavaBridge_");
		String index = getIntent().getExtras().getString("url", "");
		
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
		/*if (!Common.isNoadv())
			initBanner();*/
	}
	
	private String getUrl(String url)
	{
		return "file:///android_asset/" + url;
	}
	
	/*private void initBanner()
	{
		BannerView bannerView = new BannerView(this, ADSize.BANNER, AdvConfig.APPID, AdvConfig.BannerSecond);
		bannerView.setRefresh(30);
		bannerView.setADListener(null);
		bannerLayout.addView(bannerView);
		bannerView.loadAD();
	}*/
}
