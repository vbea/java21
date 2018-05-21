package com.vbea.java21;

import java.net.URLEncoder;
import java.io.File;
import java.io.IOException;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActivityOptions;
import android.app.Instrumentation;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.InputDevice;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.vbea.java21.widget.TouchWebView;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.MyAlertDialog;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.data.SQLHelper;
import com.vbea.java21.data.CodeMode;

import org.apache.commons.io.FileUtils;
/*import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.sdk.WebChromeClient;*/

public class CodeEditor extends AppCompatActivity
{
	private WebView myweb;
	private Dialog mDialog;
	//private XWalkView myweb;
	Toolbar tool;
	private int lanCode = -1;
	private String[] strCode, strTitle;
	private String mPath = "";
	private boolean isReady = false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Common.start(getApplicationContext());
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.code_editor);

		myweb = (WebView) findViewById(R.id.codeWebView);
		//myweb = (XWalkView) findViewById(R.id.codeWebView);
		tool = (Toolbar) findViewById(R.id.toolbar);
		//if (MyThemes.isNightTheme()) NightView.setVisibility(View.VISIBLE);
		myweb.getSettings().setJavaScriptEnabled(true);
		if (Common.isSupportMD())
			myweb.removeJavascriptInterface("searchBoxJavaBridge_");
		setSupportActionBar(tool);
		strTitle = getResources().getStringArray(R.array.array_codename);
		strCode = getResources().getStringArray(R.array.array_codemode);
		//myweb.addActionList("选择复制");
		//myweb.linkJSInterface();
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
		set.setUserAgentString(getString(R.string.pc_ua));
		//set.setTextSize(WebSettings.TextSize.LARGER);
		//myweb.addJavascriptInterface(new EditorInterface(), "EditorManage");
		myweb.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView v, String url)
			{
				v.loadUrl(url);
				return true;
			}
			
			@Override
			public void onPageStarted(WebView v, String url, Bitmap icon)
			{
				super.onPageStarted(v, url, icon);
			}
			
			@Override
			public void onPageFinished(WebView v, String url)
			{
				isReady = true;
				super.onPageFinished(v, url);
			}
		});
		myweb.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onExceededDatabaseQuota(String url, String databasein, long quota, long estime, long totalq, WebStorage.QuotaUpdater update)
			{
				update.updateQuota(5 * 1024 * 1024);
			}
		});
		SharedPreferences spf = getSharedPreferences("java21", MODE_PRIVATE);
		if (spf.getBoolean("needData", true))
			initData(spf.edit());
		/*else
		{
			Intent intent = getIntent();
			if (intent != null && intent.getAction() != null)
			{
				if (intent.getAction().equals(Intent.ACTION_VIEW))
				{
					Uri uri = intent.getData();
					if (uri != null)
						mPath = uri.getPath();
					if (!Util.isNullOrEmpty(mPath))
					{
						openFile(new File(mPath));
					}
				}
			}
		}*/
		//choisLanguage();
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		/*myweb.setActionSelectListener(new TouchWebView.ActionSelectListener()
		{
			@Override
			public void onClick(String title, String text)
			{
				Util.toastShortMessage(getApplicationContext(), "title:" + title + "\ntext:" + text);
			}
		});*/
		/*myweb.setOnTouchListener(new View.OnTouchListener()
		{
			float lastX,lastY;
			long lastDownTime;
			boolean isLongpress;
			@Override
			public boolean onTouch(View p1, MotionEvent e)
			{
				switch (e.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						lastX = e.getRawX();
						lastY = e.getRawY();
						lastDownTime = System.currentTimeMillis();
						Util.toastShortMessage(getApplicationContext(), "x:" + lastX + ",y:"+lastY);
						break;
					case MotionEvent.ACTION_MOVE:
						float x = e.getRawX();
						float y = e.getRawY();
						if (isLongPressed(lastX, lastY, x, y, lastDownTime, System.currentTimeMillis(), 300))
						{
							Util.toastShortMessage(getApplicationContext(), "longPressed");
							new Thread(new TouchEventRunnable(e.getX(), e.getY(), true)).start();
							return true;
						}
						break;
				}
				return false;
			}
		});*/
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
	
	/*private void getUrl(String language, String mode, String files)
	{
		StringBuilder url = new StringBuilder();
		if (MyThemes.isNightTheme())
			url.append("file:///android_asset/web/night.html?");
		else
			url.append("file:///android_asset/web/editor.html?");
		url.append(language + "=" + mode);
		url.append("&"+files);
		myweb.loadUrl(url.toString());
	}*/
	
	private void choisLanguage()
	{
		MyAlertDialog dialog = new MyAlertDialog(CodeEditor.this);
		dialog.setTitle("新建...");
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
	
	public void initData(final SharedPreferences.Editor edit)
	{
		mDialog = ProgressDialog.show(this, "数据加载中", "请稍后……", true, false);
		new Handler().postDelayed(new Runnable()
		{
			public void run()
			{
				new SQLHelper(CodeEditor.this).initJsonData(new SQLHelper.Callback()
				{
					@Override
					public void onSuccess()
					{
						mDialog.dismiss();
						Util.toastShortMessage(getApplicationContext(), "成功");
						edit.putBoolean("needData", false);
						edit.commit();
					}

					@Override
					public void onFailure()
					{
						mDialog.dismiss();
						Util.toastShortMessage(getApplicationContext(), "失败");
					}
				});
			}
		},500);
	}
	
	private void savaFile(String text)
	{
		if (!Util.isNullOrEmpty(mPath))
		{
			try
			{
				FileUtils.writeStringToFile(new File(mPath), text, "utf-8");
				Util.toastShortMessage(getApplicationContext(), "保存成功");
			}
			catch (IOException e)
			{
				Util.toastShortMessage(getApplicationContext(), "保存失败");
			}
		}
	}
	
	/*private void openFile(final File file)
	{
		if (file.exists())
		{
			SQLHelper helper = new SQLHelper(CodeEditor.this);
			final CodeMode mode = helper.findModeByType(Util.getFileTypeName(file.getName()));
			if (mode == null)
				Util.toastShortMessage(getApplicationContext(),"不支持的文件");
			else
			{
				mDialog = ProgressDialog.show(this, "正在打开文件", "请稍后……", true, false);
				new Handler().postDelayed(new Runnable()
				{
					public void run()
					{
						try
						{
							openFile(file.getName(), mode.getMode(), FileUtils.readFileToString(file));
						}
						catch (Exception e)
						{
							Util.toastShortMessage(getApplicationContext(),"文件打开失败");
						}
						finally
						{
							mDialog.dismiss();
						}
					}
				},500);
			}
		}
		else
			Util.toastShortMessage(getApplicationContext(),"文件不存在");
	}*/
	
	/*private void openFile(String name, String mode, String files) throws Exception
    {
		getUrl(name, mode, URLEncoder.encode(files, "UTF-8"));
		//Util.toastShortMessage(getApplicationContext(), URLEncoder.encode(files, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:openFile('");
        sb.append();
        sb.append("');");
        myweb.loadUrl(sb.toString());
        //web.evaluateJavascript(""+files+"')", null);
    }*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.code_item, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.item_save).setVisible(isReady);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.item_add:
				choisLanguage();
				break;
			case R.id.item_language:
				Common.startActivityOptions(CodeEditor.this, Modelist.class);
				break;
			case R.id.item_save:
				if (Util.isNullOrEmpty(mPath))
					startActivityForResult(new Intent(CodeEditor.this, FileSelect.class), 100, ActivityOptions.makeSceneTransitionAnimation(CodeEditor.this).toBundle());
				else
					myweb.loadUrl("javascript:save();");
				break;
			case R.id.item_keyword:
				Util.showResultDialog(CodeEditor.this, "顶部按钮从左至右依次为：删除、全选、撤销、恢复、搜索\n底部按钮从左至右依次为：缩进、光标左移、光标右移、光标上移、光标下移、添加/移除行注释、添加/移除块注释\n注：双击输入框也可全选代码", "按键说明");
				break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK && requestCode == 100)
		{
			final String _path = data.getStringExtra("path");
			if (Util.isNullOrEmpty(_path))
			{
				Util.toastShortMessage(getApplicationContext(), "无效的目录");
				return;
			}
			View view = LayoutInflater.from(CodeEditor.this).inflate(R.layout.webhome, null);
			final EditText edt = (EditText) view.findViewById(R.id.edt_webhome);
			edt.setHint("*.txt");
			MyAlertDialog dialog = new MyAlertDialog(CodeEditor.this);
			dialog.setTitle("文件名");
			dialog.setView(view);
			dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface d, int s)
				{
					String filename = edt.getText().toString();
					if (Util.isNullOrEmpty(filename))
						Util.toastShortMessage(getApplicationContext(), "无效的文件名");
					else
					{
						mPath = _path + File.separator + filename;
						//Util.toastShortMessage(getApplicationContext(), mPath);
						myweb.loadUrl("javascript:save();");
					}
				}
			});
			dialog.setNegativeButton(android.R.string.cancel, null);
			dialog.show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	class EditorInterface
    {
        @JavascriptInterface
        public void save(String text)
		{
			//Util.toastShortMessage(getApplicationContext(),text);
			savaFile(text);
        }
    }
	
	public class TouchEventRunnable implements Runnable
	{
		private float x;
		private float y;
		private boolean isLongPress;
		
		public TouchEventRunnable(float x, float y)
		{
			this.x = x;
			this.y = y;
		}
		
		public TouchEventRunnable(float x, float y, boolean isLongPress)
		{
			this.x = x;
			this.y = y;
			this.isLongPress = isLongPress;
		}
		
		@Override
		public void run()
		{
			if(isLongPress)
				longClickOnScreen(x,y);
			else
				onClick();
		}
	
		private void longClickOnScreen(float x, float y)
		{
			final Instrumentation inst = new Instrumentation();  
			try
			{
				long downTime = SystemClock.uptimeMillis();
				long eventTime = SystemClock.uptimeMillis();
				final MotionEvent eventDown = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
				eventDown.setSource(InputDevice.SOURCE_TOUCHSCREEN);
				final MotionEvent eventMove = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y+1, 0);
				eventMove.setSource(InputDevice.SOURCE_TOUCHSCREEN);
				final MotionEvent eventUp = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
				eventUp.setSource(InputDevice.SOURCE_TOUCHSCREEN);
				inst.sendPointerSync(eventDown);
				inst.sendPointerSync(eventMove);
				try
				{  
					Thread.sleep(300);  
				}
				catch (InterruptedException e)
				{  
					e.printStackTrace();  
				}
				inst.sendPointerSync(eventUp);  
			}
			catch (NullPointerException e)
			{  
				ExceptionHandler.log("code:ontouch-null", e.toString());
			}  
		}

		private void onClick()
		{
			Instrumentation inst = new Instrumentation();  
			inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));  
			inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));  
		}
	}

	private boolean isLongPressed(float lastX, float lastY, float thisX, float thisY, long lastDownTime, long thisEventTime, long longPressTime)
	{
		float offsetX = Math.abs(thisX - lastX);
		float offsetY = Math.abs(thisY - lastY);
		long intervalTime = thisEventTime - lastDownTime;
		if (offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime)
			return true;
		return false;
	}
}
