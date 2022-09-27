package com.vbea.java21.adapter;

import android.view.View;

import com.vbea.java21.R;
import com.vbea.java21.classes.Util;
import com.vbea.java21.data.ILearnList;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vbe on 2018/12/10.
 */
public class LearnListAdapter<T extends ILearnList> extends BaseListAdapter<T> {
    private OnItemClickListener onItemClickListener;
    private boolean isEnd;
    private int removedItem = 0;
    private String server = "";

    public LearnListAdapter() {
        super(R.layout.item_learn);
        mList = new ArrayList<>();
    }

    public void setList(List<T> list) {
        if (list != null && list.size() > 0) {
            mList.clear();
            mList.addAll(list);
            isEnd = false;
            if (Util.isServerDataId(mList.get(0).getOrder())) {
                server = mList.get(0).getUrl();
                mList.remove(0);
                removedItem = 1;
            }
        }
    }

    public int addList(List<T> list) {
        int count = mList.size();
        mList.addAll(list);
        return count;
    }

    public int size() {
        if (mList != null)
            return mList.size() + removedItem;
        return 0;
    }

    @Override
    protected void onRender(BaseViewHolder holder, T item, int p) {
        if (item.isTitle()) {
            if (Util.isNullOrEmpty(item.getPrefix()))
                holder.setText(R.id.item_subTitle, item.getTitle());
            else
                holder.setText(R.id.item_subTitle, item.getPrefix() + " " + item.getTitle());
            holder.setVisible(R.id.item_subTitle, true);
            holder.setGone(R.id.item_title, false);
            holder.setClickable(R.id.item_layout,false);
            holder.setBackgroundResource(R.id.item_layout, R.color.android_disable);
        } else {
            if (Util.isNullOrEmpty(item.getPrefix()))
                holder.setText(R.id.item_title, item.getTitle());
            else
                holder.setText(R.id.item_title, item.getPrefix() + " " + item.getTitle());
            holder.setGone(R.id.item_subTitle, !Util.isNullOrEmpty(item.getSubTitle()));
            holder.setText(R.id.item_subTitle, item.getSubTitle());
            holder.setGone(R.id.item_title, true);
            holder.setClickable(R.id.item_layout,false);
            holder.setBackgroundResource(R.id.item_layout, R.drawable.btn_menu);
            holder.getView(R.id.item_layout).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(onItemClickListener != null) {
                        onItemClickListener.onItemClick(item.getObjectId(), item.getTitle(), item.getSubTitle(), server + item.getUrl());
                    }
                }
            });
        }
        holder.setGone(R.id.item_endView, isEnd && p == mList.size() - 1);
        holder.setGone(R.id.item_readImg, item.isRead());
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public void setEnd(int count) {
        isEnd = (mList.size() + removedItem) == count;
    }

    public interface OnItemClickListener {
        void onItemClick(String id, String title, String sub, String url);
    }
}
