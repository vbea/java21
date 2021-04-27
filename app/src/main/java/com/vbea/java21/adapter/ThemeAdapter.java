package com.vbea.java21.adapter;

import com.vbea.java21.R;
import com.vbea.java21.ui.MyThemes;
import com.vbea.java21.classes.Common;
import com.vbea.java21.data.ThemeItem;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

public class ThemeAdapter extends BaseListAdapter<ThemeItem> {

	public ThemeAdapter() {
		super(R.layout.titmes);
	}

	@Override
	protected void onRender(BaseViewHolder holder, ThemeItem item, int i) {
		holder.setText(R.id.theme_title, item.name);
		holder.setGone(R.id.item_use, Common.APP_THEME_ID == item.id);
		MyThemes.setViewBackPrimaryAndAccent(holder.getView(R.id.theme_layout), holder.getView(R.id.theme_title), item.id);
		holder.addClick(R.id.cardView1);
	}
}