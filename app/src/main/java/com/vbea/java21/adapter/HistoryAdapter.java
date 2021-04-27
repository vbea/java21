package com.vbea.java21.adapter;

import java.util.List;

import android.view.View;

import com.vbea.java21.R;
import com.vbea.java21.data.Histories;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

public class HistoryAdapter extends BaseListAdapter<Histories> { //RecyclerView.Adapter<HistoryAdapter.MyViewHolder>

	private OnItemClickListener onItemClickListener;

	public HistoryAdapter() {
		super(R.layout.historyitem);
	}
	
	public void setData(List<Histories> list)
	{
		this.mList = list;
	}

	@Override
	protected void onRender(BaseViewHolder holder, Histories item, int p) {
		holder.setText(R.id.item_title, item.getTitle());
		holder.setText(R.id.item_sub, item.getUrl());

		holder.getView(R.id.itemLayout).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(onItemClickListener != null) {
					onItemClickListener.onItemClick(item.getUrl());
				}
			}
		});

		holder.getView(R.id.itemLayout).setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if(onItemClickListener != null) {
					onItemClickListener.onItemLongClick(item.getTitle(), item.getUrl());
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
        void onItemClick(String url);
		void onItemLongClick(String title, String url);
    }
}
