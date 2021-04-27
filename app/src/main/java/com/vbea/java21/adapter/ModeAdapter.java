package com.vbea.java21.adapter;

import android.database.Cursor;

import com.vbea.java21.R;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

public class ModeAdapter extends BaseListAdapter<Object> { //RecyclerView.Adapter<ModeAdapter.MyViewHolder>

	private Cursor cursor;

	public ModeAdapter(Cursor data) {
		super(R.layout.item_normal);
		cursor = data;
	}

	@Override
	protected void onRender(BaseViewHolder holder, Object o, int p) {
		if (cursor.moveToPosition(p)) {
			holder.setText(R.id.item_title, cursor.getString(cursor.getColumnIndex("name")));
			holder.setText(R.id.item_sub, cursor.getString(cursor.getColumnIndex("mode")));
			holder.setGone(R.id.item_end, p == getItemCount() - 1);
			holder.addClick(R.id.itemLayout);
		}
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
}