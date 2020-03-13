package com.vbea.java21;

import android.os.Bundle;
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
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;
import android.graphics.Bitmap;

import android.support.design.widget.BottomSheetDialog;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.AdvConfig;
import com.vbea.java21.classes.SocialShare;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.comm.util.AdError;

public class ApiWord extends BaseActivity
{
	private WebView myweb;
	private ProgressBar proGro;
	private TextView NightView;
	private final String api11Url = "http://docs.oracle.com/en/java/javase/11/docs/api/index.html";
	private final String api10Url = "http://docs.oracle.com/javase/10/docs/api/overview-summary.html";
	private final String api9Url = "http://docs.oracle.com/javase/9/docs/api/overview-summary.html";
	private final String api8Url = "http://docs.oracle.com/javase/8/docs/api/overview-summary.html";
	private final String api7Url = "http://docs.oracle.com/javase/7/docs/api/overview-summary.html";
	private final String api6Url = "http://docs.oracle.com/javase/6/docs/api/overview-summary.html";
	private final String apiUrlCH = "http://www.javaweb.cc/help/JavaAPI1.6/overview-summary.html";
	private final String apiAndroid = "http://androiddoc.qiniudn.com/reference/packages.html";
	private final String sub = "API文档";
	private BottomSheetDialog mBSDialog;
	private LinearLayout share_qq, share_qzone, share_wx, share_wxpy;
	private LinearLayout share_sina, share_web, share_link, share_more;
	private ViewGroup bannerLayout;
	private BannerView bannerView;

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
		bannerLayout = bind(R.id.webBanner);
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
		/*if (!Common.isNoadv())
			initBanner();*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.word_menu, menu);
		/*getLayoutInflater().setFactory(new LayoutInflater.Factory()
		{
			public View onCreateView(String name, Context context, AttributeSet attrs)
			{
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView"))
				{
					try
					{
						LayoutInflater inflate = getLayoutInflater();
						final View view = inflate.createView(name, null, attrs);
						new Handler().post(new Runnable()
						{
							public void run()
							{
								view.setBackgroundColor(getResources().getColor(R.color.white));
							}
						});
						return view;
					}
					catch (Exception e)
					{
						ExceptionHandler.log("setMenuBackground:"+e.toString());
					}
				}
				return null;
			}
			
		});*/
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
			case R.id.item_share:
				doShare();
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
			case R.id.item_apiand:
				myweb.loadUrl(apiAndroid);
				break;
		}
		return true;
	}
	
	public void doShare()
	{
		mBSDialog = new BottomSheetDialog(this);
		View view = getLayoutInflater().inflate(R.layout.sharelayout, null);
		share_qq = (LinearLayout) view.findViewById(R.id.btn_share_qq);
		share_qzone = (LinearLayout) view.findViewById(R.id.btn_share_qzone);
		share_wx = (LinearLayout) view.findViewById(R.id.btn_share_wx);
		share_wxpy = (LinearLayout) view.findViewById(R.id.btn_share_wxline);
		share_sina = (LinearLayout) view.findViewById(R.id.btn_share_sina);
		share_web = (LinearLayout) view.findViewById(R.id.btn_share_browser);
		share_link = (LinearLayout) view.findViewById(R.id.btn_share_copylink);
		share_more = (LinearLayout) view.findViewById(R.id.btn_share_more);
		mBSDialog.setContentView(view);
		mBSDialog.show();
		share_qq.setOnClickListener(new QQShareListener());
		share_qzone.setOnClickListener(new QQShareListener());
		share_more.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.putExtra(Intent.EXTRA_TEXT, myweb.getUrl());
				intent.setType("text/plain");
				startActivity(intent);
				mBSDialog.dismiss();
			}
		});
		share_web.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.parse(myweb.getUrl());
				intent.setData(uri);
				startActivity(intent);
			}
		});
		share_link.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mBSDialog.dismiss();
				ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				cm.setPrimaryClip(ClipData.newRawUri("url", Uri.parse(myweb.getUrl())));
				Util.toastShortMessage(getApplicationContext(), "已复制到剪贴板");
			}
		});
		share_wx.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				SocialShare.shareToWeixin(myweb.getTitle(), sub, myweb.getUrl(), getShareBitmap());
			}
		});
		share_wxpy.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				SocialShare.shareToWeixinZone(myweb.getTitle(), sub, myweb.getUrl(), getShareBitmap());
			}
		});
		share_sina.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				SocialShare.shareToWeixinFavorite(myweb.getTitle(), sub, myweb.getUrl(), getShareBitmap());
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
	
	IUiListener qqShareListener = new IUiListener()
	{
		@Override
        public void onCancel()
		{
			Util.toastShortMessage(getApplicationContext(), "分享取消");
        }
        @Override
        public void onComplete(Object response)
		{
			Util.toastShortMessage(getApplicationContext(), "分享成功");
		}
        @Override
        public void onError(UiError e)
		{
			//Util.toastShortMessage(getApplicationContext(), "onError: " + e.errorMessage + "e");
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK && data != null)
		{
			if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE)
				Tencent.handleResultData(data, qqShareListener);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	class QQShareListener implements View.OnClickListener
	{
		public void onClick(View v)
		{
			Bundle params = new Bundle();
			params.putString(QQShare.SHARE_TO_QQ_TITLE, myweb.getTitle());
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, myweb.getUrl());
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, sub);
			params.putString(QQShare.SHARE_TO_QQ_APP_NAME,"21天学通Java");
			params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
			SocialShare.shareToQQ(ApiWord.this, params, v.getId()==R.id.btn_share_qzone, qqShareListener);
		}
	}

	@Override
	protected void onResume()
	{
		if (mBSDialog != null)
			mBSDialog.dismiss();
		super.onResume();
	}
	
	private void initBanner()
	{
		bannerView = new BannerView(this, ADSize.BANNER, AdvConfig.APPID, AdvConfig.BannerSecond);
		bannerView.setRefresh(30);
		bannerView.setADListener(new AbstractBannerADListener()
		{
			@Override
			public void onNoAD(AdError e)
			{
				//ExceptionHandler.log("ad:"+e.getErrorCode(), e.getErrorMsg());
			}

			@Override
			public void onADReceiv()
			{

			}
		});
		bannerLayout.addView(bannerView);
		bannerView.loadAD();
	}
}
