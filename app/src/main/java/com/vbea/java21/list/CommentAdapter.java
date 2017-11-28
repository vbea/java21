package com.vbea.java21.list;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.vbea.java21.data.Comments;
import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.TimeAgo;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>
{
	private OnItemClickListener onItemClickListener;
	private List<Comments> mList;
	private TimeAgo timeAgo;
	private Context context;
	private RoundedBitmapDrawable defalteIcon;
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		if (context == null)
			context = p1.getContext();
		LayoutInflater inflate = LayoutInflater.from(context);
		View v = inflate.inflate(R.layout.java8, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		if (defalteIcon == null)
			defalteIcon = Common.getRoundedIconDrawable(context, BitmapFactory.decodeResource(context.getResources(), R.mipmap.head));
		if (timeAgo == null)
			timeAgo = new TimeAgo();
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, final int p)
	{
		final Comments item = mList.get(p);
		holder.name.setText(item.name);
		holder.date.setText(timeAgo.getTimeAgo(item.getCreatedAt(), item.date));
		holder.comment.setText(item.comment);
		if (Common.isMyUser(item.user))
			Common.setMyIcon(holder.icon, context);
		else
			holder.icon.setImageDrawable(defalteIcon);
		if (mList.size() > 5 && p == mList.size() - 1)
			holder.end.setVisibility(View.VISIBLE);
		else
			holder.end.setVisibility(View.GONE);
		if (item.reference != null && item.reference.length() > 3)
		{
			holder.reference.setText(item.reference);
			holder.reference.setVisibility(View.VISIBLE);
		}
		else
			holder.reference.setVisibility(View.GONE);
		if (item.user.equals(Common.mUser.name) || Common.mUser.roles.equals("管理员"))
			holder.delete.setVisibility(View.VISIBLE);
		else
			holder.delete.setVisibility(View.GONE);
		holder.delete.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
					onItemClickListener.onDelete(item.getObjectId(), p);
			}
		});
		holder.reply.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
					onItemClickListener.onReply(item.user, item.name, item.comment);
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

	public CommentAdapter(List<Comments> list)
	{
		mList = list;
	}

	public void setList(List<Comments> list)
	{
		mList = list;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
        this.onItemClickListener = onItemClickListener;
    }

	@Override
	public long getItemId(int p1)
	{
		return 0;
	}

	public void clear()
	{
		if (mList != null)
			mList.clear();
	}

	public class MyViewHolder extends ViewHolder
	{
		TextView name;
		TextView date;
		TextView comment;
		TextView reference;
		TextView delete;
		TextView reply;
		ImageView icon;
		RelativeLayout end;
		public MyViewHolder(View v)
		{
			super(v);
			name = (TextView) v.findViewById(R.id.comm_name);
			date = (TextView) v.findViewById(R.id.comm_date);
			comment = (TextView) v.findViewById(R.id.comm_text);
			reference = (TextView) v.findViewById(R.id.comm_refs);
			delete = (TextView) v.findViewById(R.id.comm_delete);
			icon = (ImageView) v.findViewById(R.id.comm_icon);
			reply = (TextView) v.findViewById(R.id.comm_reply);
			end = (RelativeLayout) v.findViewById(R.id.item_end);
		}
	}

	public interface OnItemClickListener
	{
        void onDelete(String id, int position);
		void onReply(String user, String nick, String comment);
    }
}
