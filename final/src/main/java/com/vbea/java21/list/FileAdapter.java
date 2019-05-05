package com.vbea.java21.list;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
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
		files = new ArrayList<FileItem>();
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
		if (file.isUplev())
			holder.sub.setText(file.getDetail());
		else
			holder.sub.setText(file.getDetail() + " " + file.getSize());
		holder.icon.setImageResource(file.isFolder() ? R.mipmap.ic_folder : R.mipmap.ic_anyfile);
		holder.layout.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null && file.isEnable())
				{
					if (file.isFolder())
						onItemClickListener.onItemClick(file.getName(), file.isUplev());
					else
						onItemClickListener.onOpenFile(file.getName());
				}
			}
		});
		holder.layout.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View p1)
			{
				if (file.isFolder() && !file.isUplev())
				{
					onItemClickListener.onDelete(file.getName());
					return true;
				}
				return false;
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
		Collections.sort(list);
		files = list;
	}
	
	public void addList(List<FileItem> list)
	{
		Collections.sort(list);
		files.addAll(list);
	}
	
	public void addItem(FileItem item)
	{
		files.add(item);
	}
	
	public void clear()
	{
		if (files != null)
			files.clear();
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
        this.onItemClickListener = onItemClickListener;
    }

	public class MyViewHolder extends ViewHolder
	{
		TextView title;
		TextView sub;
		ImageView icon;
		LinearLayout layout;
		public MyViewHolder(View v)
		{
			super(v);
			title = (TextView) v.findViewById(R.id.file_name);
			sub = (TextView) v.findViewById(R.id.file_detail);
			icon = (ImageView) v.findViewById(R.id.file_icon);
			layout = (LinearLayout) v.findViewById(R.id.itemLayout);
		}
	}

	public interface OnItemClickListener
	{
        void onItemClick(String name, boolean uplev);
		void onOpenFile(String name);
		void onDelete(String name);
    }
}
