package com.vbea.java21.list;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.graphics.Bitmap;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.vbea.java21.R;
import com.vbea.java21.MyThemes;
import com.vbea.java21.classes.Common;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder>
{
	private OnItemClickListener onItemClickListener;
	
	private List<ThemeItem> mList;
	
	public ThemeAdapter(List<ThemeItem> items)
	{
		mList = items;
	}
	
	public void addItem(ThemeItem i)
	{
		mList.add(i);
	}
	
	public void setList(List<ThemeItem> list)
	{
		mList = list;
	}
	
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		LayoutInflater inflate = LayoutInflater.from(p1.getContext());
		View v = inflate.inflate(R.layout.titmes, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int id)
	{
		final ThemeItem item = mList.get(id);
		holder.title.setText(item.name);
		//holder.title2.setText(item.name2);
		if (Common.APP_THEME_ID == item.id)
			holder.use.setVisibility(View.VISIBLE);
		else
			holder.use.setVisibility(View.GONE);
		MyThemes.setViewBackPrimaryAndAccent(holder.layout, holder.title, item.id);
		//MyThemes.setViewBackPrimaryAndAccent(holder.layout2, holder.title2, item.id2);
		holder.card.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
				{
					onItemClickListener.onItemClick(item.id);
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
		TextView title;//,title2;
		CardView card;//, card2;
		ImageView use;//, use2;
		LinearLayout layout;//, layout2;
		public MyViewHolder(View v)
		{
			super(v);
			title = (TextView) v.findViewById(R.id.theme_title);
			//title2 = (TextView) v.findViewById(R.id.theme_title2);
			card = (CardView) v.findViewById(R.id.cardView1);
			//card2 = (CardView) v.findViewById(R.id.cardView2);
			use = (ImageView) v.findViewById(R.id.item_use);
			//use2 = (ImageView) v.findViewById(R.id.item_use2);
			layout = (LinearLayout) v.findViewById(R.id.theme_layout);
			//layout2 = (LinearLayout) v.findViewById(R.id.theme_layout2);
		}
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener
	{
        void onItemClick(int themeId);
    }
}
