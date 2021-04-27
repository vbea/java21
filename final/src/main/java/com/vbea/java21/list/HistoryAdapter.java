package com.vbea.java21.adapter;

import java.util.List;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.vbea.java21.R;
import com.vbea.java21.data.Histories;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>
{
	private List<Histories> mList;
	private OnItemClickListener onItemClickListener;
	/*public HistoryAdapter(List<Histories> list)
	{
		mList = list;
	}*/

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		LayoutInflater inflate = LayoutInflater.from(p1.getContext());
		View v = inflate.inflate(R.layout.historyitem, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}
	
	public void setData(List<Histories> list)
	{
		this.mList = list;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int p)
	{
		final Histories item = mList.get(p);
		holder.title.setText(item.getTitle());
		holder.sub.setText(item.getUrl());
		holder.layout.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
				{
					onItemClickListener.onItemClick(item.getUrl());
				}
			}
		});
		holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if(onItemClickListener != null)
				{
					onItemClickListener.onItemLongClick(item.getTitle(), item.getUrl());
					return true;
				}
				return false;
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
	
	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
        this.onItemClickListener = onItemClickListener;
    }

	public class MyViewHolder extends ViewHolder
	{
		TextView title;
		TextView sub;
		LinearLayout layout;
		public MyViewHolder(View v)
		{
			super(v);
			title = (TextView) v.findViewById(R.id.item_title);
			sub = (TextView) v.findViewById(R.id.item_sub);
			layout = (LinearLayout) v.findViewById(R.id.itemLayout);
		}
	}

	public interface OnItemClickListener
	{
        void onItemClick(String url);
		void onItemLongClick(String title, String url);
    }
}
