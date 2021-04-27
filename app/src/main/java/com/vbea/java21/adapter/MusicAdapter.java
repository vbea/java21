package com.vbea.java21.adapter;

import com.vbea.java21.R;
import com.vbea.java21.audio.Music;
import com.vbea.java21.classes.Common;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

public class MusicAdapter extends BaseListAdapter<Music> {//RecyclerView.Adapter<MusicAdapter.MyViewHolder>

	private boolean isVip;
	
	public MusicAdapter() {
		super(R.layout.music);
	}

	public void setIsVip(boolean vip) {
		this.isVip = vip;
	}

	@Override
	protected void onRender(BaseViewHolder holder, Music music, int p) {
		holder.setText(R.id.music_txtMusicName, music.getName());
		holder.setText(R.id.music_txtMusicVolet, getMusicVelot(music.max, music.min));
		holder.setText(R.id.music_playing, getStatus(p, music.isTop));
		holder.addClick(R.id.musicTableRow);
		holder.addLongClick(R.id.musicTableRow);
	}
	
	private String getMusicVelot(int max, int min) {
		if (isVip)
			return max + "/" + min;
		return (100 - (max / 5)) + "/" + (90 - (min / 5));
	}
	
	private String getStatus(int p, boolean top) {
		String status = "";
		if (top) {
			status = "精选";
			if (Common.audioService.what == p && Common.audioService.isPlay())
				status += ", 正在播放";
		} else if (Common.audioService.what == p && Common.audioService.isPlay())
			status = "正在播放";
		return status;
	}
}
