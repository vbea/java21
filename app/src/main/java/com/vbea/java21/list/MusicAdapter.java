package com.vbea.java21.list;

import java.util.List;

import android.widget.TextView;
import android.widget.TableRow;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.vbea.java21.R;
import com.vbea.java21.audio.Music;
import com.vbea.java21.classes.Common;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>
{
	private OnItemClickListener onItemClickListener;
	private List<Music> mList;
	private boolean isVip;
	
	public MusicAdapter(List<Music> list)
	{
		mList = list;
	}

	public void setIsVip(boolean vip)
	{
		this.isVip = vip;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		LayoutInflater inflate = LayoutInflater.from(p1.getContext());
		View v = inflate.inflate(R.layout.music, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, final int p)
	{
		final Music m = mList.get(p);
		holder.name.setText(m.getName());
		holder.velo.setText(getMusicVelot(m.max, m.min));
		holder.sub.setText(getStatus(p, m.isTop));
		holder.row.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
					onItemClickListener.onItemClick(p);
			}
		});
		holder.row.setOnLongClickListener(new View.OnLongClickListener()
		{
			public boolean onLongClick(View v)
			{
				if(onItemClickListener != null)
					onItemClickListener.onLongClick(m.key);
				return true;
			}
		});
	}

	@Override
	public int getItemCount()
	{
		if (mList != null)
			return mList.size();
		return 0;
	}

	public void setList(List<Music> list)
	{
		mList = list;
	}
	
	private String getMusicVelot(int max, int min)
	{
		if (isVip)
			return max + "/" + min;
		return (100 - (max / 5)) + "/" + (90 - (min / 5));
	}
	
	private String getStatus(int p, boolean top)
	{
		String status = "";
		if (top)
		{
			status = "精选";
			if (Common.audioService.what == p)
				status += ", 正在播放";
		}
		else if (Common.audioService.what == p)
			status = "正在播放";
		return status;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
        this.onItemClickListener = onItemClickListener;
    }

	public class MyViewHolder extends ViewHolder
	{
		TableRow row;
		TextView name, velo, sub;
		
		public MyViewHolder(View v)
		{
			super(v);
			row = (TableRow) v.findViewById(R.id.musicTableRow);
			name = (TextView) v.findViewById(R.id.music_txtMusicName);
			velo = (TextView) v.findViewById(R.id.music_txtMusicVolet);
			sub = (TextView) v.findViewById(R.id.music_playing);
		}
	}

	public interface OnItemClickListener
	{
        void onItemClick(int m);
		void onLongClick(String key);
    }
}
