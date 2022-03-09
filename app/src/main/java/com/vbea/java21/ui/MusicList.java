package com.vbea.java21.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.adapter.MusicAdapter;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.audio.*;
import com.vbea.java21.view.MyDividerDecoration;
import com.vbes.util.list.BaseListAdapter;

public class MusicList extends BaseActivity {
    private RecyclerView recyclerView;
    private MusicAdapter mAdapter;

    @Override
    protected void before() {
        setContentView(R.layout.musiclist);
    }

    @Override
    protected void after() {
        enableBackButton(R.id.toolbar);
        recyclerView = bind(R.id.music_recyclerView);

        mAdapter = new MusicAdapter();
        mAdapter.setIsVip(Common.isVipUser());
        recyclerView.addItemDecoration(new MyDividerDecoration(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.addData(Common.SOUND.getMusicList());

        mAdapter.setOnItemClickListener(new BaseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i, View view) {
                final int old = Common.audioService.what;
                Common.audioService.play(i);
                mAdapter.notifyItemChanged(old);
                mAdapter.notifyItemChanged(i);
            }
        });

        mAdapter.setOnItemLongClickListener(new BaseListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int i, View view) {
                Music music = mAdapter.getItemData(i);
                Util.addClipboard(MusicList.this, "music", music.key);
                Util.toastShortMessage(getApplicationContext(), "已复制代码到剪贴板");
            }
        });

        Common.audioService.addAudioChangedListener(new OnAudioChangedListener() {
            @Override
            public void onAudioChange() {
                try {
                    mAdapter.notifyDataSetChanged();
                } catch (Exception ignored) {
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        Common.audioService.addAudioChangedListener(null);
        super.onDestroy();
    }
}
