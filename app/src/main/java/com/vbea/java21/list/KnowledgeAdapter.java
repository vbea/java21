package com.vbea.java21.list;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.vbea.java21.R;

public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeAdapter.MyViewHolder>
{
	private ArrayList<Knowledges> mList;
	private OnItemClickListener onItemClickListener;

	public KnowledgeAdapter(ArrayList<Knowledges> items)
	{
		mList = items;
	}
	
	public void addItem(Knowledges i)
	{
		mList.add(i);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		LayoutInflater inflate = LayoutInflater.from(p1.getContext());
		View v = inflate.inflate(R.layout.java6, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int p)
	{
		final Knowledges kow = mList.get(p);
		holder.title.setText(kow.TITLE);
		holder.sub.setText(kow.SUB);
		holder.layout.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
				{
					onItemClickListener.onItemClick(kow.TITLE, kow.URL);
				}
			}
		});
	}

	@Override
	public int getItemCount()
	{
		return mList.size();
	}

	@Override
	public long getItemId(int p1)
	{
		return 0;
	}
	
	public String getTitle(int p)
	{
		return mList.get(p).TITLE;
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
			title = (TextView) v.findViewById(R.id.know_title);
			sub = (TextView) v.findViewById(R.id.know_subTitle);
			layout = (LinearLayout) v.findViewById(R.id.itemLayout);
		}
	}

	public interface OnItemClickListener
	{
        void onItemClick(String title, String url);
    }
}
