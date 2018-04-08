package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Environment;
import android.net.Uri;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DividerItemDecoration;

import com.vbea.java21.data.WebHelper;
import com.vbea.java21.data.Histories;
import com.vbea.java21.data.Histime;
import com.vbea.java21.list.HistoryAdapter;
import com.vbea.java21.list.HistoryDecoration;
import com.vbea.java21.list.MyDividerDecoration;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.TimeAgo;
import com.vbea.java21.classes.ExceptionHandler;

public class History extends AppCompatActivity
{
	private RecyclerView recyclerView;
	private HistoryAdapter mAdapter;
	private WebHelper query;
	private List<Histories> mList;
	private List<Histime> dataList;
	private HistoryDecoration hisDecoration;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musiclist);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		recyclerView = (RecyclerView) findViewById(R.id.music_recyclerView);
		setSupportActionBar(tool);
		mAdapter = new HistoryAdapter();
		init();
		MyDividerDecoration decoration = new MyDividerDecoration(History.this);
		recyclerView.addItemDecoration(decoration);
		hisDecoration = new HistoryDecoration(this, dataList, new DecorationLis());
		recyclerView.addItemDecoration(hisDecoration);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				setResult(RESULT_CANCELED);
				supportFinishAfterTransition();
			}
		});

		mAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(String url)
			{
				Intent intent = new Intent();
				intent.setData(Uri.parse(url));
				setResult(RESULT_OK, intent);
				supportFinishAfterTransition();
			}
		});
	}

	private void init()
	{
		try
		{
			if (query == null)
				query = new WebHelper(this);
			Cursor cursor = query.listHistory();
			if (mList == null)
				mList = new ArrayList<Histories>();
			else
				mList.clear();
			if (dataList == null)
				dataList = new ArrayList<Histime>();
			else
				dataList.clear();
			TimeAgo timeAgo = new TimeAgo(new SimpleDateFormat("MM月dd日 E"));
			timeAgo.setIsTime(false);
			while (cursor.moveToNext())
			{
				Histories his = new Histories();
				his.setUrl(cursor.getString(cursor.getColumnIndex(WebHelper.COL_URL)));
				his.setTitle(cursor.getString(cursor.getColumnIndex(WebHelper.COL_TITLE)));
				his.setCreate(Long.parseLong(cursor.getString(cursor.getColumnIndex(WebHelper.COL_CREATEON))));
				Histime hisTime = new Histime();
				hisTime.setDate(timeAgo.getTimeAgo(his.getCreateOn()));
				mList.add(his);
				dataList.add(hisTime);
				mAdapter.setData(mList);
				if (hisDecoration != null)
					hisDecoration.setData(dataList);
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("history_query_init", e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.history_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Util.showConfirmCancelDialog(this, "清空历史记录", "确认操作？", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface p1, int p2)
			{
				try
				{
					query.clearHistory();
					Util.toastShortMessage(History.this, "操作成功");
					init();
					mAdapter.notifyDataSetChanged();
				}
				catch (Exception e)
				{
					Util.toastShortMessage(History.this, "操作失败");
				}
			}
		});
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	class DecorationLis implements HistoryDecoration.DecorationCallback
	{
		@Override
		public String getGroupId(int position)
		{
			if (dataList.get(position).getDate() != null)
				return dataList.get(position).getDate();
			return "-1";
		}

		@Override
		public String getGroupFirstLine(int position)
		{
			if (dataList.get(position).getDate() != null)
				return dataList.get(position).getDate();
			return "";
		}
	}
}
