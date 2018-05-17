package com.vbea.java21;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.graphics.Bitmap;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DividerItemDecoration;
import com.vbea.java21.audio.Music;
import com.vbea.java21.list.MusicAdapter;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.audio.*;

public class MusicList extends AppCompatActivity
{
	private RecyclerView recyclerView;
	private MusicAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musiclist);
	
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		recyclerView = (RecyclerView) findViewById(R.id.music_recyclerView);
		setSupportActionBar(tool);
		
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		
		mAdapter = new MusicAdapter(Common.SOUND.getMusicList());
		mAdapter.setIsVip(Common.isVipUser());
		DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
		decoration.setDrawable(getResources().getDrawable(R.drawable.ic_divider));
		recyclerView.addItemDecoration(decoration);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		mAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(int m)
			{
				Common.audioService.play(m);
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onLongClick(String key)
			{
				Util.addClipboard(MusicList.this, "music", key);
				Util.toastShortMessage(getApplicationContext(), "已复制代码到剪贴板");
			}
		});
		Common.audioService.addAudioChangedListener(new OnAudioChangedListener()
		{
			@Override
			public void onAudioChange()
			{
				try
				{
					mAdapter.notifyDataSetChanged();
				}
				catch (Exception e){}
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy()
	{
		Common.audioService.addAudioChangedListener(null);
		super.onDestroy();
	}
}
