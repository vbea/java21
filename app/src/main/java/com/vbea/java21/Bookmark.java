package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Environment;
import android.net.Uri;
import android.view.View;
import android.content.Intent;
import android.content.DialogInterface;
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
	private WebHelper query;
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
			public void onItemLongClick(final int id, final String url)
			{
				new AlertDialog.Builder(Bookmark.this)
				.setTitle(R.string.bookmark)
				.setItems(R.array.array_bookaction, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface v, int b)
					{
						v.dismiss();
						switch (b)
						{
							case 0:
								Intent intent = new Intent();
								intent.setData(Uri.parse(url));
								setResult(RESULT_OK, intent);
								supportFinishAfterTransition();
								break;
							/*case 1:
								onUpdateBookmark(id);
								break;*/
							case 1:
								Util.showConfirmCancelDialog(Bookmark.this, "删除书签", "确认操作？", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface p1, int p2)
									{
										if (onDeleteBookmark(String.valueOf(id)))
										{
											Util.toastShortMessage(getApplicationContext(), "删除成功");
											init();
											mAdapter.notifyDataSetChanged();
										}
										else
										{
											Util.toastShortMessage(getApplicationContext(), "删除失败");
										}
									}
								});
								break;
						}
					}
				}).show();
			}
		});
	}
	
	private void init()
	{
		try
		{
			if (query == null)
				query = new WebHelper(this);
			if (mAdapter == null)
				mAdapter = new BookAdapter(query.listBookmark());
			else
				mAdapter.setData(query.listBookmark());
			//ExceptionHandler.log("bookmark_query", "count=" + mAdapter.getItemCount());
		}
		catch (Exception e)
		{
			ExceptionHandler.log("bookmark_query", e);
		}
	}
	
	/*private void onUpdateBookmark(int id)
	{
	}*/
	
	private boolean onDeleteBookmark(String id)
	{
		if (query != null)
		{
			try
			{
				return (query.deleteBookmark(id) > 0);
			}
			catch (Exception e)
			{
				ExceptionHandler.log("deleteBookmark", e);
			}
		}
		return false;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
