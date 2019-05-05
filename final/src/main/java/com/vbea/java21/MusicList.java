package com.vbea.java21;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import com.vbea.java21.list.MusicAdapter;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.audio.*;
import com.vbea.java21.view.MyDividerDecoration;

public class MusicList extends BaseActivity
{
	private RecyclerView recyclerView;
	private MusicAdapter mAdapter;

	@Override
	protected void before()
	{
		setContentView(R.layout.musiclist);
	}

	@Override
	protected void after()
	{
		enableBackButton();
		recyclerView = bind(R.id.music_recyclerView);
		
		mAdapter = new MusicAdapter(Common.SOUND.getMusicList());
		mAdapter.setIsVip(Common.isVipUser());
		recyclerView.addItemDecoration(new MyDividerDecoration(this));
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
