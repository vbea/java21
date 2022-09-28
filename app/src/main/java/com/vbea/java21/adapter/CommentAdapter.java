package com.vbea.java21.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;

import com.vbea.java21.data.Comments;
import com.vbea.java21.R;
import com.vbea.java21.ui.MyThemes;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.TimeAgo;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

public class CommentAdapter extends BaseListAdapter<Comments> { //RecyclerView.Adapter<CommentAdapter.MyViewHolder>


	private TimeAgo timeAgo;
	private Context context;
	//private RoundedBitmapDrawable defalteIcon;
	private int colorActive, colorNone;
	private boolean lock = false;
	private OnItemClickListener onItemClickListener;

	public CommentAdapter(Context _context) {
		super(R.layout.item_comment);
		context = _context;
		colorActive = MyThemes.getColorAccent(context);
		colorNone = context.getResources().getColor(R.color.grey2);
		//if (defalteIcon == null)
		//	defalteIcon = Common.getRoundedIconDrawable(context, BitmapFactory.decodeResource(context.getResources(), R.drawable.head));
		if (timeAgo == null)
			timeAgo = new TimeAgo(null);
	}

	@Override
	protected void onRender(BaseViewHolder holder, Comments item, int i) {
		ImageView icon = holder.getView(R.id.comm_icon);
		ImageView img_endorse = holder.getView(R.id.img_endorse);
		ImageView img_oppose = holder.getView(R.id.img_oppose);
		TextView txt_endorse = holder.getView(R.id.txt_endorse);
		TextView txt_oppose = holder.getView(R.id.txt_oppose);
		holder.setText(R.id.comm_name, item.name);
		//holder.setText(R.id.comm_date, timeAgo.getTimeAgo(item.getCreatedAt(), item.date));
		holder.setText(R.id.comm_text, item.comment);
		holder.setText(R.id.comm_device, Util.isNullOrEmpty(item.device) ? "" : item.device);
		boolean isMy = Common.isMyUser(item.user);
		//holder.endorse.setEnabled(!isMy);
		//holder.oppose.setEnabled(!isMy);
		//holder.img_vip.setVisibility(item.isVip ? View.VISIBLE : View.GONE);
		holder.setGone(R.id.comm_btngroup, Common.isLogin());

		if (Common.isLogin()) {
			holder.setGone(R.id.comm_reply, !isMy);
			holder.setGone(R.id.comm_delete, isMy || Common.isAdminUser());
		}

		getActcount(item.endorse, txt_endorse, img_endorse);
		getActcount(item.oppose, txt_oppose, img_oppose);

		if (isMy) {
			Common.setMyIcon(icon, context);
		} else {
			icon.setImageResource(R.drawable.head_circle);
			//loadImage(item.user, icon, true);
		}
		holder.setGone(R.id.item_end, mList.size() > 5 && i == mList.size() - 1);

		if (item.reference != null && item.reference.length() > 3) {
			holder.setText(R.id.comm_refs, item.reference);
			holder.setGone(R.id.comm_refs, true);
		} else {
			holder.setGone(R.id.comm_refs, false);
		}

		holder.getView(R.id.comm_delete).setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
					onItemClickListener.onDelete(item, i);
			}
		});
		holder.getView(R.id.comm_reply).setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null)
					onItemClickListener.onReply(item.user, item.name, item.comment);
			}
		});
		holder.getView(R.id.comm_endorse).setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null && !img_oppose.isSelected() && !lock)
				{
					lock = true;
					onItemClickListener.onEndorse(item, i, img_endorse.isSelected());
				}
			}
		});
		holder.getView(R.id.comm_oppose).setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(onItemClickListener != null && !img_endorse.isSelected() && !lock)
				{
					lock = true;
					onItemClickListener.onOppose(item, i, img_oppose.isSelected());
				}
			}
		});
	}

	private void loadImage(String user, ImageView v, boolean force) {
		Bitmap icon = Common.getUserIcon(context, user);
		if (icon != null) {
			v.setImageDrawable(Common.getRoundedIconDrawable(context, icon));
		} else if (force) {
			v.setImageResource(R.drawable.head_circle);
			/*new UserImageLoad(user, new UserImageLoad.OnLoadListener() {
				@Override
				public void onComplete() {
					loadImage(user, v, false);
				}
			});*/
		}
	}
	
	public void unLock() {
		lock = false;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
	
	private void getActcount(String s, TextView txt, ImageView img) {
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

	public interface OnItemClickListener {
        void onDelete(Comments comm, int position);
		void onReply(String user, String nick, String comment);
		void onEndorse(Comments comm, int position, boolean ed);
		void onOppose(Comments comm, int position, boolean ed);
    }
}
