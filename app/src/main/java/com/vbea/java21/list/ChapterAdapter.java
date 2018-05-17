package com.vbea.java21.list;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.vbea.java21.R;
import com.vbea.java21.classes.Common;

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
		View v = inflate.inflate(R.layout.java6, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int id)
	{
		final Chapter item = mList.get(id);
		holder.text.setText(item.title);
		holder.review.setText(item.review);
		holder.layout.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
				{
					onItemClickListener.onItemClick(id, item.title, item.review);
				}
			}
		});
		holder.read.setVisibility(Common.READ_Android.contains(id) ? View.VISIBLE : View.GONE);
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
		ImageView read;
		LinearLayout layout;
		public MyViewHolder(View v)
		{
			super(v);
			text = (TextView) v.findViewById(R.id.know_title);
			review = (TextView) v.findViewById(R.id.know_subTitle);
			read = (ImageView) v.findViewById(R.id.item_imgread);
			layout = (LinearLayout) v.findViewById(R.id.itemLayout);
		}
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int resId, String title, String sub);
    }
}
