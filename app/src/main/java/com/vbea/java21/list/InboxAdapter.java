package com.vbea.java21.list;

import java.util.List;
import java.text.SimpleDateFormat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.vbea.java21.R;
import com.vbea.java21.data.Messages;
import com.vbea.java21.classes.TimeAgo;
import com.vbea.java21.widget.SlidingView;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.MyViewHolder>
{
	private OnItemClickListener onItemClickListener;
	private List<Messages> mList;
	private TimeAgo timeAgo;
	private boolean isOpen = false;
	public InboxAdapter()
	{
		timeAgo = new TimeAgo(new SimpleDateFormat("MM-dd"));
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		LayoutInflater inflate = LayoutInflater.from(p1.getContext());
		View v = inflate.inflate(R.layout.item_message, p1, false);
		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int p)
	{
		final Messages item = mList.get(p);
		holder.title.setText(item.title);
		holder.content.setText("["+(item.read?"已读":"未读")+"] "+item.message);
		holder.date.setText(timeAgo.getTimeAgo(item.getCreatedAt()));
		holder.slidingView.closeMenu();
		holder.title.getPaint().setFakeBoldText(!item.read);
		holder.leftMenu.setText(item.read?"设为未读":"设为已读");
		if (p == mList.size() - 1)
			holder.end.setVisibility(View.VISIBLE);
		else
			holder.end.setVisibility(View.GONE);
		holder.leftMenu.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				onItemClickListener.onLeftMenu(item);
				holder.slidingView.closeMenu();
			}
		});
		holder.rightMenu.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				onItemClickListener.onDelete(item, p);
			}
		});
		holder.layout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (isOpen)
				{
					if (holder.slidingView.isOpen())
						holder.slidingView.closeMenu();
					else
						notifyDataSetChanged();
					isOpen = false;
				}
				else
					onItemClickListener.onClick(item);
			}
		});
		holder.slidingView.setMenuListener(new SlidingView.SlidingViewListener()
		{
			@Override
			public void onOpenMenu()
			{
				holder.layout.setClickable(false);
				isOpen = true;
			}
			@Override
			public void onCloseMenu() {
				holder.layout.setClickable(true);
				isOpen = false;
			}
			@Override
			public boolean onStart(SlidingView v) {
				//holder.title.setText(v.mScreenWidth + "/" + v.leftMenuWidth + "/" + v.rightMenuWidth);
				if (isOpen) {
					if (v.isOpen())
						v.closeMenu();
					/*else
						notifyDataSetChanged();*/
					isOpen = false;
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public int getItemCount() {
		if (mList != null)
			return mList.size();
		return 0;
	}

	public void setList(List<Messages> list) {
		mList = list;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

	@Override
	public long getItemId(int p1) {
		return 0;
	}

	public void clear() {
		if (mList != null)
			mList.clear();
	}

	public class MyViewHolder extends ViewHolder {
		TextView leftMenu, rightMenu, title, content, date;
		SlidingView slidingView;
		LinearLayout layout;
		View end;
		public MyViewHolder(View v) {
			super(v);
			layout = (LinearLayout) v.findViewById(R.id.item_messageBox);
			leftMenu = (TextView) v.findViewById(R.id.item_leftMenu);
			rightMenu = (TextView) v.findViewById(R.id.item_rightMenu);
			title = (TextView) v.findViewById(R.id.item_messageTitle);
			content = (TextView) v.findViewById(R.id.item_messageContent);
			date = (TextView) v.findViewById(R.id.item_messageDate);
			slidingView = (SlidingView) v.findViewById(R.id.item_slidingBox);
			end = v.findViewById(R.id.item_end);
		}
	}

	public interface OnItemClickListener
	{
        void onLeftMenu(Messages msg);
		void onDelete(Messages msg, int position);
		void onClick(Messages msg);
    }
}
