package com.vbea.java21;

import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.view.View;
import android.view.Menu;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Bitmap;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;

public class AndroidWeb extends BaseActivity
{
	private WebView myweb;
	private ProgressBar proGro;
	private String id, title, sub, url;
	private TextView NightView;
	private int type = 0;//Android
	//private int CommentCount = 0;
	@Override
	protected void before()
	{
		setContentView(R.layout.article);
		url = getIntent().getExtras().getString("url", "");
		title = getIntent().getExtras().getString("title", "");
		sub = getIntent().getExtras().getString("sub", "");
		id = getIntent().getExtras().getString("id", "");
		type = getIntent().getIntExtra("type", -1);
		setToolbarTitle(title);
	}

	@Override
	protected void after()
	{
		enableBackButton();
		proGro = bind(R.id.artProgress);
		myweb = bind(R.id.artWebView);
		NightView = bind(R.id.art_nightView);
		TableRow btnComment = bind(R.id.art_editbtn);
		EditText edtComment = bind(R.id.art_editText);
		if (MyThemes.isNightTheme()) NightView.setVisibility(View.VISIBLE);
		WebSettings set = myweb.getSettings();
		set.setJavaScriptEnabled(true);
		set.setUseWideViewPort(true);
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
		if (Common.isSupportMD())
			myweb.removeJavascriptInterface("searchBoxJavaBridge_");
		new Handler().postDelayed(new Runnable()
		{
			public void run()
			{
				if (Common.isNet(AndroidWeb.this))
					myweb.loadUrl(url);
			}
		}, 500);
		myweb.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView v, String url)
			{
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onReceivedSslError(WebView v, SslErrorHandler h, SslError e)
			{
				h.proceed();
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
	}
	
	private Bitmap getShareBitmap()
	{
		View v = myweb;
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		return Bitmap.createScaledBitmap(v.getDrawingCache(), 120, 120, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (type != 0 && type != 5)
		{
			getMenuInflater().inflate(R.menu.android_item, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	/*final class JavaScriptShowCode
	{
		@JavascriptInterface
		public void openImage(String img)
		{
			openImageActivity(img);
		}
	}*/

	@Override
	protected void onResume()
	{
		myweb.requestFocus();
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
