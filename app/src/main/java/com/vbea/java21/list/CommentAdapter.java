package com.vbea.java21.list;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.vbea.java21.data.Comments;
import com.vbea.java21.R;
import com.vbea.java21.MyThemes;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.TimeAgo;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>
{
	private OnItemClickListener onItemClickListener;
	private List<Comments> mList;
	private TimeAgo timeAgo;
	private Context context;
	private RoundedBitmapDrawable defalteIcon;
	private int colorActive, colorNone;
	private boolean lock = false;
	public CommentAdapter(List<Comments> list, Context _context)
	{
		context = _context;
		colorActive = MyThemes.getColorAccent(context);
		colorNone = context.getResources().getColor(R.color.grey2);
		if (defalteIcon == null)
			defalteIcon = Common.getRoundedIconDrawable(context, BitmapFactory.decodeResource(context.getResources(), R.mipmap.head));
		if (timeAgo == null)
			timeAgo = new TimeAgo(null);
		mList = list;
	}
	
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		if (context == null)
			context = p1.getContext();
		LayoutInflater inflate = LayoutInflater.from(context);
		View v = inflate.inflate(R.layout.java8, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int p)
	{
		final Comments item = mList.get(p);
		holder.name.setText(item.name);
		holder.date.setText(timeAgo.getTimeAgo(item.getCreatedAt(), item.date));
		holder.comment.setText(item.comment);
		boolean isMy = Common.isMyUser(item.user);
		//holder.endorse.setEnabled(!isMy);
		//holder.oppose.setEnabled(!isMy);
		//holder.img_vip.setVisibility(item.isVip ? View.VISIBLE : View.GONE);
		if (Common.isLogin()) {
			holder.btnGroup.setVisibility(View.VISIBLE);
			holder.reply.setVisibility(isMy?View.GONE:View.VISIBLE);
			holder.delete.setVisibility(isMy || Common.isAdminUser() ? View.VISIBLE : View.GONE);
		} else {
			holder.btnGroup.setVisibility(View.GONE);
		}
		getActcount(item.endorse, holder.txt_endorse, holder.img_endorse);
		getActcount(item.oppose, holder.txt_oppose, holder.img_oppose);
		if (isMy)
			Common.setMyIcon(holder.icon, context, defalteIcon);
		else
		{
			Bitmap icon = Common.getIcon(item.user);
			if (icon != null)
				holder.icon.setImageDrawable(Common.getRoundedIconDrawable(context, icon));
			else
				holder.icon.setImageDrawable(defalteIcon);
		}
		if (mList.size() > 5 && p == mList.size() - 1)
			holder.end.setVisibility(View.VISIBLE);
		else
			holder.end.setVisibility(View.GONE);
		holder.device.setText(Util.isNullOrEmpty(item.device) ? "" : item.device);
		if (item.reference != null && item.reference.length() > 3)
		{
			holder.reference.setText(item.reference);
			holder.reference.setVisibility(View.VISIBLE);
		}
		else
			holder.reference.setVisibility(View.GONE);
		holder.delete.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
					onItemClickListener.onDelete(item, p);
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
		holder.endorse.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null && !holder.img_oppose.isSelected() && !lock)
				{
					lock = true;
					onItemClickListener.onEndorse(item, p, holder.img_endorse.isSelected());
				}
			}
		});
		holder.oppose.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null && !holder.img_endorse.isSelected() && !lock)
				{
					lock = true;
					onItemClickListener.onOppose(item, p, holder.img_oppose.isSelected());
				}
			}
		});
	}
	
	public void unLock()
	{
		lock = false;
	}

	@Override
	public int getItemCount()
	{
		if (mList != null)
			return mList.size();
		return 0;
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
	
	private void getActcount(String s, TextView txt, ImageView img)
	{
		//endorse
		img.setSelected(false);
		txt.setText("0");
		txt.setTextColor(colorNone);
		img.setColorFilter(colorNone);
		if (!Util.isNullOrEmpty(s))
		{
			txt.setText(String.valueOf(s.split(",").length));
			if (Common.isContainsUser(s))
			{
				txt.setTextColor(colorActive);
				img.setColorFilter(colorActive);
				img.setSelected(true);
			}
		}
	}

	public class MyViewHolder extends ViewHolder
	{
		TextView name, date, comment, reference, delete, reply, txt_endorse, txt_oppose, device;
		ImageView icon,img_endorse,img_oppose;
		TableRow endorse, oppose;
		RelativeLayout end;
		View btnGroup;
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
			device = (TextView) v.findViewById(R.id.comm_device);
			end = (RelativeLayout) v.findViewById(R.id.item_end);
			img_endorse = (ImageView) v.findViewById(R.id.img_endorse);
			img_oppose = (ImageView) v.findViewById(R.id.img_oppose);
			txt_endorse = (TextView) v.findViewById(R.id.txt_endorse);
			txt_oppose = (TextView) v.findViewById(R.id.txt_oppose);
			endorse = (TableRow) v.findViewById(R.id.comm_endorse);
			oppose = (TableRow) v.findViewById(R.id.comm_oppose);
			btnGroup = v.findViewById(R.id.comm_btngroup);
		}
	}

	public interface OnItemClickListener
	{
        void onDelete(Comments comm, int position);
		void onReply(String user, String nick, String comment);
		void onEndorse(Comments comm, int position, boolean ed);
		void onOppose(Comments comm, int position, boolean ed);
    }
}
