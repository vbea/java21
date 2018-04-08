package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Environment;
import android.net.Uri;
import android.view.View;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DividerItemDecoration;

import com.vbea.java21.data.WebHelper;
import com.vbea.java21.list.BookAdapter;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.ExceptionHandler;

public class Bookmark extends AppCompatActivity
{
	private RecyclerView recyclerView;
	private BookAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musiclist);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		recyclerView = (RecyclerView) findViewById(R.id.music_recyclerView);
		setSupportActionBar(tool);
		init();
		DividerItemDecoration decoration = new DividerItemDecoration(Bookmark.this, DividerItemDecoration.VERTICAL);
		decoration.setDrawable(getResources().getDrawable(R.drawable.ic_divider));
		recyclerView.addItemDecoration(decoration);
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
		
		mAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(String url)
			{
				Intent intent = new Intent();
				intent.setData(Uri.parse(url));
				setResult(RESULT_OK, intent);
				supportFinishAfterTransition();
			}

			@Override
			public void onItemLongClick(int id)
			{
				
			}
		});
	}
	
	private void init()
	{
		try
		{
			WebHelper query = new WebHelper(this);
			mAdapter = new BookAdapter(query.listBookmark());
			//ExceptionHandler.log("bookmark_query", "count=" + mAdapter.getItemCount());
		}
		catch (Exception e)
		{
			ExceptionHandler.log("bookmark_query", e);
		}
	}
}
