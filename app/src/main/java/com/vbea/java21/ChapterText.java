package com.vbea.java21;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.AdvConfig;
import com.qq.e.comm.util.AdError;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.banner.AbstractBannerADListener;

public class ChapterText extends AppCompatActivity
{
	//章节内容
	private int chapter = 0;
	private int[] layouts = //引用View
	new int[]{R.layout.chapter01,R.layout.chapter02,R.layout.chapter03,R.layout.chapter04,R.layout.chapter05,R.layout.chapter06,R.layout.chapter07,R.layout.chapter08,R.layout.chapter09,R.layout.chapter10,
			R.layout.chapter11,	R.layout.chapter12,	R.layout.chapter13,	R.layout.chapter14,	R.layout.chapter15,	R.layout.chapter16,	R.layout.chapter17,	R.layout.chapter18,	R.layout.chapter19,	R.layout.chapter20,R.layout.chapter21};
	private String[] titles, shorts;//标题
	private LayoutInflater inflate;
	private LinearLayout contentLayout;
	private TextView textTitle;
	private CollapsingToolbarLayout appbar;
	private NestedScrollView scrollView;
	private FloatingActionButton btnNext;
	private ViewGroup bannerLayout;
	
	private long systemTimeMillis = 0;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		applyStyle();
		super.onCreate(savedInstanceState);
		chapter = getIntent().getExtras().getInt("chapter", 0);
		setContentView(R.layout.chapter_h);
		titles = getResources().getStringArray(R.array.array_chapter);
		shorts = getResources().getStringArray(R.array.array_shortChapter);
		textTitle = (TextView) findViewById(R.id.chapter_title);
		contentLayout = (LinearLayout) findViewById(R.id.content_layout);
		appbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		scrollView = (NestedScrollView) findViewById(R.id.chapter_scrollView);
		btnNext = (FloatingActionButton) findViewById(R.id.fbtn_next);
		inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		bannerLayout = (ViewGroup) findViewById(R.id.articalBanner);
		setView();
		applyTips();
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tool);
		//appbar.setExpandedTitleTextAppearance(R.attr.textAppearanceListItemSmall);
		if (MyThemes.isNightTheme())
			appbar.setExpandedTitleColor(getResources().getColor(R.color.snow));
		else
        	appbar.setExpandedTitleColor(getResources().getColor(R.color.black));
        appbar.setCollapsedTitleTextColor(getResources().getColor(R.color.grey3));
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				supportFinishAfterTransition();
			}
		});
		/*tool.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View p1, MotionEvent p2)
			{
				Util.toastShortMessage(getApplicationContext(), "触摸");
				return false;
			}
		});*/
		tool.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				//模拟双击
				if (System.currentTimeMillis() - systemTimeMillis <= 1000)
				{
					if (scrollView.getScrollY() >= 200)
						scrollView.smoothScrollTo(0, 0);
				}
				systemTimeMillis = System.currentTimeMillis();
			}
		});
		btnNext.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				//Intent intent1 = new Intent();
				//intent1.putExtra("chapter", chapter+1);
				//startActivity(intent1);
				//finish();
				Common.AUDIO_STUDY_STATE+=2;
				chapter++;
				contentLayout.removeViewAt(1);
				setView();
			}
		});
	}
	
	private void setView()
	{
		if (chapter >= titles.length)
			chapter = 0;
		if (chapter == titles.length - 1)
			btnNext.setVisibility(View.GONE);
		appbar.setTitle(titles[chapter]);
		textTitle.setText(shorts[chapter]);
		contentLayout.addView(inflate.inflate(layouts[chapter], null), 1);
	}
	
	public void onImage(View v)
	{
		try
		{
			ImageView img = (ImageView) v;
			Intent intent = new Intent(this, ImagePreview.class);
			v.setDrawingCacheEnabled(true);
			v.buildDrawingCache(true);
			Bitmap bitmap = Bitmap.createBitmap(img.getDrawingCache());
			v.setDrawingCacheEnabled(false);
			//intent.putExtra("bitmap", BitmapFactory.decodeStream(bais));
			ImagePreview.bitmap = bitmap;
			Common.startActivityOptions(this, intent);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("onImage", e.toString());
		}
	}
	
	public void applyStyle()
	{
		switch (Common.JAVA_TEXT_SIZE)
		{
			case 0:
				getTheme().applyStyle(R.style.Theme_TextSize_Small, true);
				break;
			case 1:
				getTheme().applyStyle(R.style.Theme_TextSize_nSmall, true);
				break;
			case 3:
				getTheme().applyStyle(R.style.Theme_TextSize_nLarge, true);
				break;
			case 4:
				getTheme().applyStyle(R.style.Theme_TextSize_Large, true);
				break;
		}
	}
	
	private void applyTips()
	{
		if (!Common.isNoadv())
			initBanner();
		SharedPreferences spf = getSharedPreferences("java21", MODE_PRIVATE);
		boolean isRed = spf.getBoolean("chartip", false);
		if (!isRed)
		{
			Util.toastShortMessage(getApplicationContext(), "双击标题栏可快速回到顶部");
			SharedPreferences.Editor editor = spf.edit();
			editor.putBoolean("chartip", true);
			editor.commit();
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
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
