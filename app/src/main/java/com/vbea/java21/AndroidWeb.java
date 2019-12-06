package com.vbea.java21;

import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.content.Intent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;
import android.graphics.Bitmap;

import android.support.design.widget.BottomSheetDialog;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.SocialShare;
import com.vbea.java21.classes.AdvConfig;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.data.Comments;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.comm.util.AdError;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.exception.BmobException;

public class AndroidWeb extends BaseActivity
{
	private WebView myweb;
	private ProgressBar proGro;
	private String id, title, sub, url;
	private BottomSheetDialog mBSDialog;
	private TextView NightView, CommentView;
	private LinearLayout share_qq, share_qzone, share_wx, share_wxpy;
	private LinearLayout share_sina, share_web, share_link, share_more;
	private BannerView bannerView;
	private ViewGroup bannerLayout;
	private int type = 0;//Android
	//private int CommentCount = 0;
	private final String template = "已有%1s条评论";

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
		CommentView = bind(R.id.art_comment);
		bannerLayout = bind(R.id.webBanner);
		TableRow btnComment = bind(R.id.art_editbtn);
		EditText edtComment = bind(R.id.art_editText);
		if (MyThemes.isNightTheme()) NightView.setVisibility(View.VISIBLE);
		WebSettings set = myweb.getSettings();
		set.setJavaScriptEnabled(true);
		set.setUseWideViewPort(true);
		//myweb.addJavascriptInterface(new JavaScriptShowCode(), "showcode");
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
				if (url.startsWith("img://")) {
					Intent intent = new Intent(AndroidWeb.this, ShowWebImage.class);
					intent.putExtra("url", url.replace("img://", ""));
					Common.startActivityOptions(AndroidWeb.this, intent);
				} else if (url.startsWith("weixin://")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
				} else {
					Intent intent = new Intent(AndroidWeb.this, HtmlViewer.class);
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					Common.startActivityOptions(AndroidWeb.this, intent);
				}
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
				//if (type != 0)
				addImageClickListner();
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
		
		edtComment.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			public void onFocusChange(View v, boolean s)
			{
				if (s) goComment();
			}
		});
		
		btnComment.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				goComment();
			}
		});
		//applyTips();
		if (!Common.isNoadv() || Common.isHuluxiaUser())
			initBanner();
		if (Common.isHuluxiaAd())
			initInterstitial();
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
	
	private void initInterstitial()
	{
		final InterstitialAD interstitial = new InterstitialAD(this, AdvConfig.APPID, AdvConfig.Interstitial);
		interstitial.setADListener(new AbstractInterstitialADListener()
		{
			@Override
			public void onNoAD(AdError e)
			{
				//ExceptionHandler.log("in-ad:"+e.getErrorCode(), e.getErrorMsg());
			}

			@Override
			public void onADReceive()
			{
				interstitial.show();
			}
		});
		interstitial.loadAD();
	}
	
	private Bitmap getShareBitmap()
	{
		View v = myweb;
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		return Bitmap.createScaledBitmap(v.getDrawingCache(), 120, 120, true);
	}
	
	private void goComment()
	{
		Intent intent = new Intent(this, Comment.class);
		intent.putExtra("title", title);
		intent.putExtra("id", id);
		intent.putExtra("type", type);
		intent.putExtra("url", url);
		Common.startActivityOptions(this, intent);
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

	/*@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.item_androidcomment).setTitle("评论" + (CommentCount > 0 ? "(" + CommentCount + ")" : ""));
		return super.onPrepareOptionsMenu(menu);
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		//if (item.getItemId() == R.id.item_androidcomment)
		if (item.getItemId() == R.id.item_flush)
		{
			if (Common.isNet(this))
				myweb.reload();
		}
		if (item.getItemId() == R.id.item_androidshare)
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
					intent.putExtra(Intent.EXTRA_TEXT, title + "：" + url);
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
					Uri uri = Uri.parse(url);
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
					cm.setPrimaryClip(ClipData.newRawUri("url", Uri.parse(url)));
					Util.toastShortMessage(getApplicationContext(), "已复制到剪贴板");
				}
			});
			share_wx.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					SocialShare.shareToWeixin(title, sub, url, getShareBitmap());
				}
			});
			share_wxpy.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					SocialShare.shareToWeixinZone(title, sub, url, getShareBitmap());
				}
			});
			share_sina.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{
					SocialShare.shareToWeixinFavorite(title, sub, url, getShareBitmap());
					/*SocialShare.shareToSina(AndroidWeb.this, title + "\n" + sub + "\n" + url, new WeiboAuthListener()
					{
						@Override
						public void onComplete(Bundle p1)
						{
							Util.toastShortMessage(getApplicationContext(), "分享成功");
						}

						@Override
						public void onWeiboException(WeiboException p1)
						{
							Util.toastShortMessage(getApplicationContext(), "wbexception:"+p1.toString());
						}

						@Override
						public void onCancel()
						{
							
						}
					});*/
				}
			});
		}
		return true;
	}
	
	private void getCommentCount()
	{
		BmobQuery<Comments> sql = new BmobQuery<Comments>();
		sql.addWhereEqualTo("id", id);
		sql.addWhereEqualTo("type", type);
		sql.count(Comments.class, new CountListener()
		{
			@Override
			public void done(Integer p1, BmobException p2)
			{
				if (p2 == null)
					CommentView.setText(String.format(template, p1));
			}
		});
	}
	
	/*private void applyTips()
	{
		SharedPreferences spf = getSharedPreferences("java21", MODE_PRIVATE);
		boolean isRed = spf.getBoolean("androidtip", false);
		if (!isRed)
		{
			Util.toastShortMessage(getApplicationContext(), "点击右上角菜单可打开评论");
			SharedPreferences.Editor editor = spf.edit();
			editor.putBoolean("androidtip", true);
			editor.commit();
		}
	}*/
	
	private void addImageClickListner()
	{
		//这段js函数的功能就是，遍历所有的img节点，并添加onclick函数
		//函数的功能是在图片点击的时候调用本地java接口并传递url过去
		myweb.loadUrl("javascript:(function() {" + 
						"var objs = document.getElementsByTagName(\"img\"); " +
						"for(var i=0;i<objs.length;i++)  " +
						"{" + 
						"    objs[i].onclick=function()  "+
						"    { "  + 
						"        window.location.href = 'img://'+this.src;" +
						"    } " +
						"}" + 
						"})()");
	}
	
	private void openImageActivity(String img)
	{
		try
		{
			Intent intent = new Intent(AndroidWeb.this, ShowWebImage.class);
			intent.putExtra("image", img);
			Common.startActivityOptions(AndroidWeb.this, intent);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("openImageActivity", e.toString());
		}
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
	
	class QQShareListener implements View.OnClickListener
	{
		public void onClick(View v)
		{
			Bundle params = new Bundle();
			params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, sub);
			params.putString(QQShare.SHARE_TO_QQ_APP_NAME,"21天学通Java");
			params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
			SocialShare.shareToQQ(AndroidWeb.this, params, v.getId()==R.id.btn_share_qzone, qqShareListener);
		}
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK && data != null)
		{
			if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE)
				Tencent.handleResultData(data, qqShareListener);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume()
	{
		myweb.requestFocus();
		getCommentCount();
		if (mBSDialog != null)
			mBSDialog.dismiss();
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		if (bannerView != null)
		{
			bannerLayout.removeAllViews();
			bannerView.destroy();
			bannerView = null;
		}
		super.onDestroy();
	}
}
