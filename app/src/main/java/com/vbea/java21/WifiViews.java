package com.vbea.java21;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.DividerItemDecoration;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.WifiManage;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.list.WifiItem;
import com.vbea.java21.list.WifiAdapter;

public class WifiViews extends AppCompatActivity
{
	private RecyclerView recyclerView;
	private WifiAdapter mAdapter;
	private LinearLayout mLayout;
	private boolean START = true;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifimanage);
		recyclerView = (RecyclerView) findViewById(R.id.wifi_recyclerView);
		mLayout = (LinearLayout) findViewById(R.id.wifi_proLayout);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(tool);
		mAdapter = new WifiAdapter(this);
		//DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
		//decoration.setDrawable(getResources().getDrawable(R.drawable.ic_divider));
		//recyclerView.addItemDecoration(decoration);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		
		mAdapter.setOnItemClickListener(new WifiAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(String ssid, String psd)
			{
				ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				cbm.setPrimaryClip(ClipData.newPlainText("wifi_info", "SSID："+ssid+"\n"+psd));
				Util.toastShortMessage(getApplicationContext(), "已复制到剪贴板");
			}
		});
		
		START = true;
		init();
	}

	private void init()
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					mAdapter.setList(new WifiManage().Read());
					if (START)
						mHandler.sendEmptyMessage(0);
					else
						mHandler.sendEmptyMessage(1);
				}
				catch (Exception e)
				{
					if (START)
					{
						ExceptionHandler.log("wifi_view", e.toString());
						mHandler.sendEmptyMessageDelayed(2, 1000);
					}
				}
				finally
				{
					START = false;
				}
			}
		}.start();
	}

	@Override
	protected void onResume()
	{
		if (!START)
			init();
		super.onResume();
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					mLayout.setVisibility(View.GONE);
					if (mAdapter.getItemCount() <= 0)
						Util.toastShortMessage(WifiViews.this, "查看Wi-Fi密码需要获取ROOT权限");
					else
						mAdapter.notifyDataSetChanged();
					break;
				case 1:
					mAdapter.resumes();
					mAdapter.notifyDataSetChanged();
					break;
				case 2:
					Util.toastShortMessage(WifiViews.this, "加载失败，请重试");
					mLayout.setVisibility(View.GONE);
					break;
			}
			super.handleMessage(msg);
		}
	};
}
