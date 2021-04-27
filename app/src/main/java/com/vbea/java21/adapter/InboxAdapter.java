package com.vbea.java21.adapter;

import java.text.SimpleDateFormat;

import android.view.View;
import android.widget.TextView;

import com.vbea.java21.R;
import com.vbea.java21.data.Messages;
import com.vbea.java21.classes.TimeAgo;
import com.vbea.java21.widget.SlidingView;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

public class InboxAdapter extends BaseListAdapter<Messages> { //recyclerView.Adapter<InboxAdapter.MyViewHolder>

    private OnItemClickListener onItemClickListener;
    private TimeAgo timeAgo;
    private boolean isOpen = false;

    public InboxAdapter() {
        super(R.layout.item_message);
        timeAgo = new TimeAgo(new SimpleDateFormat("MM-dd"));
    }

    @Override
    protected void onRender(BaseViewHolder holder, Messages item, int p) {
        SlidingView slidingView = holder.getView(R.id.item_slidingBox);
        TextView txtTitle = holder.getView(R.id.item_messageTitle);
        TextView leftMenu = holder.getView(R.id.item_leftMenu);
        TextView rightMenu = holder.getView(R.id.item_rightMenu);
        txtTitle.setText(item.title);
        holder.setText(R.id.item_messageContent, "[" + (item.read ? "已读" : "未读") + "] " + item.message);
        holder.setText(R.id.item_messageDate, timeAgo.getTimeAgo(item.getCreatedAt()));
        slidingView.closeMenu();
        txtTitle.getPaint().setFakeBoldText(!item.read);
        leftMenu.setText(item.read ? "设为未读" : "设为已读");
        holder.setGone(R.id.item_end, p == mList.size() - 1);
        leftMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onItemClickListener.onLeftMenu(item);
                slidingView.closeMenu();
            }
        });
        rightMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onItemClickListener.onDelete(item, p);
            }
        });
        holder.getView(R.id.item_messageBox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    if (slidingView.isOpen())
                        slidingView.closeMenu();
                    else
                        notifyDataSetChanged();
                    isOpen = false;
                } else {
                    onItemClickListener.onClick(item);
                }
            }
        });
        slidingView.setMenuListener(new SlidingView.SlidingViewListener() {
            @Override
            public void onOpenMenu() {
                holder.setClickable(R.id.item_messageBox, false);
                isOpen = true;
            }

            @Override
            public void onCloseMenu() {
                holder.setClickable(R.id.item_messageBox, true);
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


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onLeftMenu(Messages msg);

        void onDelete(Messages msg, int position);

        void onClick(Messages msg);
    }
}
