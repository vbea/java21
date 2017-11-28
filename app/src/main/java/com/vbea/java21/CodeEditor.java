package com.vbea.java21;

import android.app.AlertDialog;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.vbea.java21.widget.TouchWebView;
import com.vbea.java21.classes.Common;
import com.vbea.java21.data.SQLHelper;
/*import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.sdk.WebChromeClient;*/

public class CodeEditor extends AppCompatActivity
{
	private TouchWebView myweb;
	//private XWalkView myweb;
	Toolbar tool;
	private int lanCode = -1;
	private String[] strCode, strTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.code_editor);

		myweb = (TouchWebView) findViewById(R.id.codeWebView);
		//myweb = (XWalkView) findViewById(R.id.codeWebView);
		tool = (Toolbar) findViewById(R.id.toolbar);
		//if (MyThemes.isNightTheme()) NightView.setVisibility(View.VISIBLE);
		myweb.getSettings().setJavaScriptEnabled(true);
		if (Common.isSupportMD())
			myweb.removeJavascriptInterface("searchBoxJavaBridge_");
		setSupportActionBar(tool);
		strTitle = getResources().getStringArray(R.array.array_codename);
		strCode = getResources().getStringArray(R.array.array_codemode);
		WebSettings set = myweb.getSettings();
		//XWalkSettings set = myweb.getSettings();
		set.setJavaScriptEnabled(true);
		set.setBuiltInZoomControls(true);
		set.setJavaScriptCanOpenWindowsAutomatically(true);
		set.setSupportMultipleWindows(true);
		set.setUseWideViewPort(true);
		set.setLoadsImagesAutomatically(true);
		set.setLoadWithOverviewMode(true);
		set.setSaveFormData(false);
		set.setSavePassword(false);
		set.setSupportZoom(true);
		set.setDomStorageEnabled(true);
		//set.setTextSize(WebSettings.TextSize.LARGER);
		myweb.setWebViewClient(new WebViewClient());
		myweb.setWebChromeClient(new WebChromeClient());
		
		choisLanguage();
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		new SQLHelper(this).insert("Java", "Java", "java");
	}

	private void getUrl(String language, String mode)
	{
		StringBuilder url = new StringBuilder();
		if (MyThemes.isNightTheme())
			url.append("file:///android_asset/web/night.html?");
		else
			url.append("file:///android_asset/web/editor.html?");
		url.append(language + "=" + mode);
		myweb.loadUrl(url.toString());
	}
	
	private void choisLanguage()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(CodeEditor.this);
		dialog.setTitle("选择解析语言");
		dialog.setSingleChoiceItems(strTitle, lanCode,
			new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface d, int s)
				{
					lanCode = s;
					getUrl(strTitle[s], strCode[s]);
					d.dismiss();
				}
			});
		dialog.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.code_item, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		choisLanguage();
		/*if (item.getItemId() == R.id.item_androidcomment)
		{
		}*/
		return true;
	}
}
