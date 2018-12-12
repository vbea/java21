package com.vbea.java21.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vbea.java21.R;
import com.vbea.java21.AndroidWeb;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.data.Knowledges;
import com.vbea.java21.list.KnowledgeAdapter;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.view.MyDividerDecoration;

public class KnowFragment extends Fragment
{
	private RecyclerView recyclerView;
	private KnowledgeAdapter mAdapter;
	private View rootView;
	private final int type = 0;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        if (rootView == null)
			rootView = inflater.inflate(R.layout.chapter, container, false);
		return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
        super.onViewCreated(view, savedInstanceState);
		if (recyclerView == null)
		{
			recyclerView = (RecyclerView) view.findViewById(R.id.cpt_recyclerView);
			recyclerView.setHasFixedSize(true);
			recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
			recyclerView.addItemDecoration(new MyDividerDecoration(getContext()));
			
			ArrayList<Knowledges> list = new ArrayList<Knowledges>();
			String[] titles = getResources().getStringArray(R.array.array_shizhan);
			String[] urls1 = getResources().getStringArray(R.array.url_shizhan);
			String[] review = getResources().getStringArray(R.array.array_baodian);
			String[] urls2 = getResources().getStringArray(R.array.url_baodian);
			
			for (int i=0; i<review.length; i++)
			{
				list.add(new Knowledges(review[i], "宝典", urls2[i]));
			}
			for (int i=0; i<titles.length; i++)
			{
				list.add(new Knowledges(titles[i], "实战", urls1[i]));
			}
			
			mAdapter = new KnowledgeAdapter(list);
			recyclerView.setAdapter(mAdapter);
			mAdapter.setOnItemClickListener(new KnowledgeAdapter.OnItemClickListener()
			{
				@Override
				public void onItemClick(Knowledges item)
				{
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
}
