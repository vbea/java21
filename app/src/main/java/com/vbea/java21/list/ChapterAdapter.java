package com.vbea.java21.list;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.vbea.java21.R;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder>
{
	private OnItemClickListener onItemClickListener;
	private ArrayList<Chapter> mList;

	public ChapterAdapter(ArrayList<Chapter> items)
	{
		mList = items;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		LayoutInflater inflate = LayoutInflater.from(p1.getContext());
		View v = inflate.inflate(R.layout.adapter, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int id)
	{
		holder.text.setText(mList.get(id).title);
		holder.review.setText(mList.get(id).review);
		holder.card.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
				{
					onItemClickListener.onItemClick(v, id);
				}
			}
		});
	}

	@Override
	public int getItemCount()
	{
		// TODO: Implement this method
		return mList.size();
	}

	@Override
	public long getItemId(int p1)
	{
		return 0;
	}

	public class MyViewHolder extends ViewHolder
	{
		TextView text;
		TextView review;
		CardView card;
		public MyViewHolder(View v)
		{
			super(v);
			text = (TextView) v.findViewById(R.id.cap_title);
			review = (TextView) v.findViewById(R.id.cap_review);
			card = (CardView) v.findViewById(R.id.cardView);
		}
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int resId);
    }
}
