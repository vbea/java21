package com.vbea.java21.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vbea.java21.R;
import com.vbea.java21.ui.AndroidWeb;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.data.Chapter;
import com.vbea.java21.adapter.ChapterAdapter;
import com.vbea.java21.view.MyDividerDecoration;
import com.vbea.java21.classes.Common;
import com.vbes.util.fragment.BaseFragment;
import com.vbes.util.list.BaseListAdapter;

public class ChapterFragment extends BaseFragment {

    private ChapterAdapter mAdapter;
    private final int type = 5;

    public ChapterFragment() {
        super(R.layout.chapter);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(R.layout.chapter, container, false);
        return rootView;
    }*/

    @Override
    public void onInitView(View view) {

        RecyclerView recyclerView = getView(R.id.cpt_recyclerView);
        recyclerView.addItemDecoration(new MyDividerDecoration(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        List<Chapter> list = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.array_chapter);
        String[] review = getResources().getStringArray(R.array.array_review);
        for (int i = 0; i < titles.length; i++) {
            list.add(new Chapter(i, titles[i], review[i]));
        }
        mAdapter = new ChapterAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(list);
        mAdapter.setOnItemClickListener(new BaseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i, View view) {
					/*if (!Common.isLogin())
					{
						Util.toastShortMessage(getContext(), "请先登录！");
						return;
					}*/
                Chapter item = mAdapter.getItemData(i);
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
