package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v4.widget.SwipeRefreshLayout;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.InboxManager;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.data.Messages;
import com.vbea.java21.list.InboxAdapter;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.exception.BmobException;
import com.vbea.java21.list.*;

public class MyInbox extends BaseActivity
{
	/**
	 * 邠心工作室
	 * 21天学通Java
	 * 消息中心页面
	 * 2017/12/01 五
	 */
	private TextView emptyView;
	private RecyclerView recyclerView;
	private InboxAdapter mAdapter;
	private SwipeRefreshLayout refreshLayout;

	@Override
	protected void before()
	{
		setContentView(R.layout.inbox);
	}

	@Override
	public void after()
	{
		enableBackButton();
		recyclerView = (RecyclerView) findViewById(R.id.inbox_recyclerView);
		emptyView = (TextView) findViewById(R.id.inbox_emptyview);
		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.inbox_refresh);
		refreshLayout.setColorSchemeResources(MyThemes.getColorPrimary(), MyThemes.getColorAccent());
		mAdapter = new InboxAdapter();
		recyclerView.addItemDecoration(new MyDividerDecoration(this));
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				if (Common.isNet(MyInbox.this))
					getData();
				else
					Util.toastShortMessage(getApplicationContext(), "咦，网络去哪儿了？");
			}
		});

		mAdapter.setOnItemClickListener(new InboxAdapter.OnItemClickListener()
		{
			@Override
			public void onLeftMenu(Messages msg)
			{
				Common.getInbox().updateMessage(msg.read ? msg.makeUnread() : msg.makeRead(), new InboxManager.InboxCallback()
				{
					@Override
					public void onSuccess()
					{
						getData();
					}
					@Override
					public void onFailure(){}
				});
				
			}

			@Override
			public void onDelete(Messages msg, int position)
			{
				Common.getInbox().deleteMessage(msg, position);
				init();
			}

			@Override
			public void onClick(Messages msg)
			{
				if (!msg.read)
					Common.getInbox().updateMessage(msg.makeRead(), null);
				if (msg.type == 0)
					Common.startActivityOptions(MyInbox.this, UserCentral.class);
				else if (msg.type < 7)
				{
					Intent intent = new Intent(MyInbox.this, AndroidWeb.class);
					intent.putExtra("id", msg.refId);
					intent.putExtra("url", msg.url);
					intent.putExtra("title", msg.message);
					intent.putExtra("sub", getString(R.string.app_name));
					intent.putExtra("type", msg.type);
					Common.startActivityOptions(MyInbox.this, intent);
				}
				else
					finishAfterTransition();
			}
		});
	}

	private void init()
	{
		if (refreshLayout.isRefreshing())
			refreshLayout.setRefreshing(false);
		if (mAdapter.getItemCount() > 0)
		{
			emptyView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			//Util.toastShortMessage(getApplicationContext(), "count:" + Common.getInbox().getList().size() + ",unread"+Common.getInbox().getCount());
			mAdapter.notifyDataSetChanged();
		}
		else
		{
			emptyView.setVisibility(View.VISIBLE);
			emptyView.setText("空空如也");
			recyclerView.setVisibility(View.GONE);
		}
	}

	private void getData()
	{
		emptyView.setText("请稍候...");
		Common.getInbox().getMessage(new InboxManager.InboxCallback()
		{
			@Override
			public void onSuccess()
			{
				mAdapter.setList(Common.getInbox().getList());
				mHandler.sendEmptyMessage(1);
			}
			@Override
			public void onFailure()
			{
				//mAdapter.clear();
				mHandler.sendEmptyMessage(2);
			}
		});
	}

	@Override
	protected void onResume()
	{
		if (refreshLayout.isRefreshing())
			refreshLayout.setRefreshing(false);
		getData();
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		//Common.getInbox().refreshMessage();
		super.onDestroy();
	}

	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					init();
					break;
				case 2:
					emptyView.setVisibility(View.VISIBLE);
					emptyView.setText("加载失败\n请检查你的网络连接");
					recyclerView.setVisibility(View.GONE);
			}
			super.handleMessage(msg);
		}
	};
}
