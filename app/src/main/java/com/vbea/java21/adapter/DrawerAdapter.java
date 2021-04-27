package com.vbea.java21.adapter;

import android.widget.ImageView;
import android.graphics.Bitmap;

import com.vbea.java21.R;
import com.vbea.java21.classes.Common;
import com.vbea.java21.data.DrawItem;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

public class DrawerAdapter extends BaseListAdapter<DrawItem> { //RecyclerView.Adapter<DrawerAdapter.MyViewHolder>

	public DrawerAdapter() {
		super(R.layout.drawer);
	}

	@Override
	protected void onRender(BaseViewHolder holder, DrawItem item, int i) {
		holder.setGone(R.id.listCardView, true);
		ImageView img = holder.getView(R.id.item_use);
		if (item.p) {
			img.setImageResource(item.res);
		} else {
			Bitmap bit = Common.getDrawerBack(mContext);
			if (bit != null) {
				img.setImageBitmap(bit);
			} else {
				holder.setGone(R.id.listCardView, false);
			}
		}
		holder.setGone(R.id.item_title, Common.APP_BACK_ID == item.id);
		holder.addClick(R.id.listCardView);
	}
}
