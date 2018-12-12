package com.vbea.java21.list;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.data.Chapter;

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
		View v = inflate.inflate(R.layout.item_learn, p1, false);
		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int id)
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
					onItemClickListener.onItemClick(item);
				}
			}
		});
		holder.read.setVisibility(ReadUtil.getInstance().isReadJava(String.valueOf(id)) ? View.VISIBLE : View.GONE);
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
			text = v.findViewById(R.id.item_title);
 			review = v.findViewById(R.id.item_subTitle);
 			read = v.findViewById(R.id.item_readImg);
 			layout = v.findViewById(R.id.item_layout);
		}
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener
	{
        void onItemClick(Chapter chapter);
    }
}
