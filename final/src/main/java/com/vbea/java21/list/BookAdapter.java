package com.vbea.java21.adapter;

import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.vbea.java21.R;
import com.vbea.java21.data.WebHelper;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    private Cursor cursor;
    private OnItemClickListener onItemClickListener;

    public BookAdapter(Cursor data) {
        cursor = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        LayoutInflater inflate = LayoutInflater.from(p1.getContext());
        View v = inflate.inflate(R.layout.item_normal, p1, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int p) {
        if (cursor.moveToPosition(p)) {
            final int id = cursor.getInt(cursor.getColumnIndex(WebHelper.COL_ID));
            final String url = cursor.getString(cursor.getColumnIndex(WebHelper.COL_URL));
            holder.title.setText(cursor.getString(cursor.getColumnIndex(WebHelper.COL_TITLE)));
            holder.sub.setText(url);
            if (p == getItemCount() - 1)
                holder.end.setVisibility(View.VISIBLE);
            else
                holder.end.setVisibility(View.GONE);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(url);
                    }
                }
            });
            holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(id, url);
                    return true;
                }
            });
        }
    }

    public void setData(Cursor data) {
        cursor = data;
    }

    @Override
    public int getItemCount() {
        if (cursor != null)
            return cursor.getCount();
        return 0;
    }

    public void close() {
        if (cursor != null)
            cursor.close();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class MyViewHolder extends ViewHolder {
        TextView title;
        TextView sub;
        LinearLayout layout;
        View end;

        public MyViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.item_title);
            sub = (TextView) v.findViewById(R.id.item_sub);
            layout = (LinearLayout) v.findViewById(R.id.itemLayout);
            end = v.findViewById(R.id.item_end);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String url);

        void onItemLongClick(int id, String url);
    }
}
