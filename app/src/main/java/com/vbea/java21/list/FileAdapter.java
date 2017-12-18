package com.vbea.java21.list;

import java.util.List;
import java.util.Collections;

import android.widget.TextView;
import android.widget.LinearLayout;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.vbea.java21.R;
import com.vbea.java21.data.FileItem;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.MyViewHolder>
{
	private OnItemClickListener onItemClickListener;
	
	private List<FileItem> files;
	public FileAdapter()
	{
		
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		LayoutInflater inflate = LayoutInflater.from(p1.getContext());
		View v = inflate.inflate(R.layout.fileitem, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int p)
	{
		final FileItem file = files.get(p);
		holder.title.setText(file.getName());
		holder.sub.setText(file.getDetail());
		holder.layout.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
				{
					onItemClickListener.onItemClick(file.getName(), file.isUplev());
				}
			}
		});
	}

	@Override
	public int getItemCount()
	{
		if (files != null)
			return files.size();
		return 0;
	}
	
	public void setList(List<FileItem> list)
	{
		files = list;
		Collections.sort(list);
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
        void onItemClick(String name, boolean uplev);
    }
}
