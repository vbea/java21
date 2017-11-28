package com.vbea.java21.list;

import java.util.ArrayList;

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

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder>
{
	private OnItemClickListener onItemClickListener;

	private ArrayList<DrawItem> mList;

	public DrawerAdapter()
	{
		mList = new ArrayList<DrawItem>();
		addItem(new DrawItem(0, MyThemes.getColorPrimary()));
		for (int i = 1; i < MyThemes.drawerImages.length; i++)
		{
			addItem(new DrawItem(i, MyThemes.getDrawerBack(i)));
		}
		addItem(new DrawItem());
	}

	public void addItem(DrawItem i)
	{
		mList.add(i);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		LayoutInflater inflate = LayoutInflater.from(p1.getContext());
		View v = inflate.inflate(R.layout.drawer, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, int id)
	{
		final DrawItem item = mList.get(id);
		holder.card.setVisibility(View.VISIBLE);
		if (item.p)
			holder.img.setImageResource(item.res);
		else
		{
			Bitmap bit = Common.getDrawerBack();
			if (bit != null)
				holder.img.setImageBitmap(bit);
			else
				holder.card.setVisibility(View.GONE);
		}
		if (Common.APP_BACK_ID == item.id)
			holder.use.setVisibility(View.VISIBLE);
		else
			holder.use.setVisibility(View.GONE);
		
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
		return p1;
	}

	public class MyViewHolder extends ViewHolder
	{
		TextView use;
		ImageView img;
		CardView card;
		public MyViewHolder(View v)
		{
			super(v);
			use = (TextView) v.findViewById(R.id.item_title);
			card = (CardView) v.findViewById(R.id.listCardView);
			img = (ImageView) v.findViewById(R.id.item_use);
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
