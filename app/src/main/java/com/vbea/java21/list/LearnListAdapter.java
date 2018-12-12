package com.vbea.java21.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.classes.Util;
import com.vbea.java21.data.ILearnList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vbe on 2018/12/10.
 */
public class LearnListAdapter<T extends ILearnList> extends RecyclerView.Adapter<LearnListAdapter.MyViewHolder>
{
    private OnItemClickListener onItemClickListener;
    private List<T> mList;
    private boolean isEnd;
    private String server = "";

    public LearnListAdapter() {
        mList = new ArrayList<>();
        setEnd(false);
    }

    public void setList(List<T> list)
    {
        mList.clear();
        mList.addAll(list);
        if (Util.isServerDataId(mList.get(0).getOrder()))
        {
            server = mList.get(0).getUrl();
            mList.remove(0);
        }
    }

    @Override
    public int getItemCount()
    {
        if (mList != null)
            return mList.size();
        return 0;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2)
    {
        LayoutInflater inflate = LayoutInflater.from(p1.getContext());
        View v = inflate.inflate(R.layout.item_learn, p1, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LearnListAdapter.MyViewHolder holder, int p)
    {
        T item = mList.get(p);
        if (item.isTitle())
        {
            if (Util.isNullOrEmpty(item.getPrefix()))
                holder.sub.setText(item.getTitle());
            else
                holder.sub.setText(item.getPrefix() + " " + item.getTitle());
            holder.sub.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.GONE);
            holder.layout.setClickable(false);
            holder.layout.setBackgroundResource(R.color.android_disable);
        }
        else
        {
            if (Util.isNullOrEmpty(item.getPrefix()))
                holder.title.setText(item.getTitle());
            else
                holder.title.setText(item.getPrefix() + " " + item.getTitle());
            if (Util.isNullOrEmpty(item.getSubTitle()))
                holder.sub.setVisibility(View.GONE);
            else {
                holder.sub.setVisibility(View.VISIBLE);
                holder.sub.setText(item.getSubTitle());
            }
            holder.title.setVisibility(View.VISIBLE);
            holder.layout.setClickable(true);
            holder.layout.setBackgroundResource(R.drawable.btn_menu);
            holder.layout.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(onItemClickListener != null)
                    {
                        onItemClickListener.onItemClick(item.getObjectId(), item.getTitle(), item.getSubTitle(), server + item.getUrl());
                    }
                }
            });
        }
        if (isEnd && p == mList.size() - 1) {
            holder.end.setVisibility(View.VISIBLE);
        } else {
            holder.end.setVisibility(View.GONE);
        }
        holder.read.setVisibility(item.isRead() ? View.VISIBLE : View.GONE);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView sub;
        ImageView read;
        LinearLayout layout;
        TextView end;

        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.item_title);
            sub = v.findViewById(R.id.item_subTitle);
            layout = v.findViewById(R.id.item_layout);
            end = v.findViewById(R.id.item_endView);
            read = v.findViewById(R.id.item_readImg);
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(String id, String title, String sub, String url);
    }
}
