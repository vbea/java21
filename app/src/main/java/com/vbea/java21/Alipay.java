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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.Context;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;
import android.graphics.Bitmap;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.BottomSheetDialog;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.SocialShare;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class Alipay extends AppCompatActivity
{
	//private final String apiUrl = "http://www.yq1021.com/api/overview-summary.html";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apiword);
	
		WebView myweb = (WebView) findViewById(R.id.WebViewApi);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tool);
		WebSettings set = myweb.getSettings();
		set.setJavaScriptEnabled(true);
		myweb.loadUrl("https://qr.alipay.com/c1x079614vvcisqv4cxi19c");
		
		myweb.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView v, String url)
			{
				//if (url.startsWith(
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				v.loadUrl(url);
				return true;
			}
		});
		
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
	}
}
