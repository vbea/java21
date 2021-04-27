package com.vbea.java21.fragment;

import java.util.ArrayList;

import android.view.View;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vbea.java21.R;
import com.vbea.java21.ui.AndroidWeb;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.data.Knowledges;
import com.vbea.java21.adapter.KnowledgeAdapter;
import com.vbea.java21.classes.Common;
import com.vbea.java21.view.MyDividerDecoration;
import com.vbes.util.fragment.BaseFragment;
import com.vbes.util.list.BaseListAdapter;

public class KnowFragment extends BaseFragment {

    private KnowledgeAdapter mAdapter;
    private final int type = 0;

    public KnowFragment() {
        super(R.layout.chapter);
    }

    @Override
    public void onInitView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.cpt_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new MyDividerDecoration(getContext()));

        ArrayList<Knowledges> list = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.array_shizhan);
        String[] urls1 = getResources().getStringArray(R.array.url_shizhan);
        String[] review = getResources().getStringArray(R.array.array_baodian);
        String[] urls2 = getResources().getStringArray(R.array.url_baodian);

        for (int i = 0; i < review.length; i++) {
            list.add(new Knowledges(review[i], "宝典", urls2[i]));
        }
        for (int i = 0; i < titles.length; i++) {
            list.add(new Knowledges(titles[i], "实战", urls1[i]));
        }

        mAdapter = new KnowledgeAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(list);
        mAdapter.setOnItemClickListener(new BaseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i, View view) {
                Knowledges item = mAdapter.getItemData(i);
					/*if (Common.isLogin())
					{
						Util.toastShortMessage(getContext(), "id:" + item.ID);
						return;
					}*/
                ReadUtil.getInstance().addItemJavaAdvance(item.getId());
                Intent intent = new Intent(getActivity(), AndroidWeb.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("url", item.getUrl());
                intent.putExtra("title", item.TITLE);
                //intent.putExtra("sub", item.SUB);
                intent.putExtra("type", type);
                Common.startActivityOptions(getActivity(), intent);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}
