package com.vbea.java21.adapter;

import com.vbea.java21.R;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.data.Chapter;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

public class ChapterAdapter extends BaseListAdapter<Chapter> { //RecyclerView.Adapter<ChapterAdapter.MyViewHolder>

	public ChapterAdapter() {
		super(R.layout.item_learn);
	}

	@Override
	protected void onRender(BaseViewHolder holder, Chapter item, int i) {
		holder.setText(R.id.item_title, item.title);
		holder.setText(R.id.item_subTitle, item.review);
		holder.addClick(R.id.item_layout);
		holder.setGone(R.id.item_readImg, ReadUtil.getInstance().isReadJava(String.valueOf(i)));
	}
}
