package com.vbea.java21.adapter;

import android.widget.LinearLayout;
import android.database.Cursor;
import android.view.View;

import com.vbea.java21.R;
import com.vbea.java21.data.WebHelper;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

public class BookAdapter extends BaseListAdapter<Object> { // RecyclerView.Adapter<BookAdapter.MyViewHolder>

	private Cursor cursor;
	private OnItemClickListener onItemClickListener;

	public BookAdapter(Cursor data) {
		super(R.layout.item_normal);
		cursor = data;
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

	@Override
	protected void onRender(BaseViewHolder holder, Object o, int p) {
		if (cursor.moveToPosition(p)) {
			final int id = cursor.getInt(cursor.getColumnIndex(WebHelper.COL_ID));
			final String url = cursor.getString(cursor.getColumnIndex(WebHelper.COL_URL));
			holder.setText(R.id.item_title, cursor.getString(cursor.getColumnIndex(WebHelper.COL_TITLE)));
			holder.setText(R.id.item_sub, url);
			//holder.setGone(R.id.item_end, p == getItemCount() - 1);
			LinearLayout layout = holder.getView(R.id.itemLayout);
			layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onItemClickListener.onItemClick(url);
				}
			});
			layout.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					onItemClickListener.onItemLongClick(id, url);
					return true;
				}
			});
		}
	}

	public void close() {
		if (cursor != null)
			cursor.close();
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public interface OnItemClickListener {
		void onItemClick(String url);
		void onItemLongClick(int id, String url);
	}
}
