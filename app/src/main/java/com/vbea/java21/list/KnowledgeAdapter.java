package com.vbea.java21.list;

import java.util.ArrayList;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.vbea.java21.R;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.data.Knowledges;

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
		View v = inflate.inflate(R.layout.item_learn, p1, false);
		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int p)
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
					onItemClickListener.onItemClick(kow);
				}
			}
		});
		holder.read.setVisibility(ReadUtil.getInstance().isReadJavaAdvance(kow.getId()) ? View.VISIBLE : View.GONE);
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
		ImageView read;
		public MyViewHolder(View v)
		{
			super(v);
			title = v.findViewById(R.id.item_title);
			sub = v.findViewById(R.id.item_subTitle);
			layout = v.findViewById(R.id.item_layout);
			read = v.findViewById(R.id.item_readImg);
		}
	}

	public interface OnItemClickListener
	{
        void onItemClick(Knowledges knowledge);
    }
}
