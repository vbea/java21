package com.vbea.java21.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vbea.java21.R;
import com.vbea.java21.AndroidWeb;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.data.Chapter;
import com.vbea.java21.list.ChapterAdapter;
import com.vbea.java21.view.MyDividerDecoration;
import com.vbea.java21.classes.Common;

public class ChapterFragment extends Fragment
{
	private RecyclerView recyclerView;
	private ChapterAdapter mAdapter;
	private View rootView;
	private final int type = 5;
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
			recyclerView.addItemDecoration(new MyDividerDecoration(getContext()));
			recyclerView.setHasFixedSize(true);
			recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

			ArrayList<Chapter> list = new ArrayList<Chapter>();
			String[] titles = getResources().getStringArray(R.array.array_chapter);
			String[] review = getResources().getStringArray(R.array.array_review);
			for (int i=0; i<titles.length; i++)
			{
				list.add(new Chapter(i, titles[i], review[i]));
			}
			mAdapter = new ChapterAdapter(list);
			recyclerView.setAdapter(mAdapter);
			mAdapter.setOnItemClickListener(new ChapterAdapter.OnItemClickListener()
			{
				@Override
				public void onItemClick(Chapter item)
				{
					/*if (!Common.isLogin())
					{
						Util.toastShortMessage(getContext(), "请先登录！");
						return;
					}*/
					String id = String.valueOf(item.id);
					ReadUtil.getInstance().addItemJava(id);
 					Intent intent = new Intent(getActivity(), AndroidWeb.class);
 					intent.putExtra("id", id);
 					intent.putExtra("url", "file:///android_asset/java/chapter" + id + ".html");
 					intent.putExtra("title", item.title);
 					//intent.putExtra("sub", item.review);
 					intent.putExtra("type", type);
					Common.startActivityOptions(getActivity(), intent);
					mAdapter.notifyDataSetChanged();
				}
			});
		}
	}
}
