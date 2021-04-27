package com.vbea.java21.adapter;

import com.vbea.java21.R;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.data.Knowledges;
import com.vbes.util.list.BaseListAdapter;
import com.vbes.util.list.BaseViewHolder;

public class KnowledgeAdapter extends BaseListAdapter<Knowledges> { //RecyclerView.Adapter<KnowledgeAdapter.MyViewHolder>

	public KnowledgeAdapter() {
		super(R.layout.item_learn);
	}

	@Override
	protected void onRender(BaseViewHolder holder, Knowledges kow, int p) {
		holder.setText(R.id.item_title, kow.TITLE);
		holder.setText(R.id.item_subTitle, kow.SUB);
		holder.addClick(R.id.item_layout);
		holder.setGone(R.id.item_readImg, ReadUtil.getInstance().isReadJavaAdvance(kow.getId()));
	}
}
