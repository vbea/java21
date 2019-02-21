package com.vbea.java21.fragment;

import java.util.List;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vbea.java21.R;
import com.vbea.java21.AndroidWeb;
import com.vbea.java21.MyThemes;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.data.AndroidIDE;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.list.LearnListAdapter;
import com.vbea.java21.view.MyDividerDecoration;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.exception.BmobException;

public class AideFragment extends Fragment
{
	private SwipeRefreshLayout refreshLayout;
	private RecyclerView recyclerView;
	private TextView errorText;
	private ProgressBar proRefresh;
	private LearnListAdapter<AndroidIDE> mAdapter;
	private View rootView;
	private int mCount = -1;
	private final int type = 4;
	//private ProgressDialog mPdialog;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (rootView == null)
			rootView = inflater.inflate(R.layout.android, container, false);
		return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
        super.onViewCreated(view, savedInstanceState);
		if (recyclerView == null)
		{
			mAdapter = new LearnListAdapter<>();
			errorText = (TextView) view.findViewById(R.id.txt_andError);
			proRefresh = (ProgressBar) view.findViewById(R.id.refreshProgress);
        	recyclerView = (RecyclerView) view.findViewById(R.id.cpt_recyclerView);
			refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swp_refresh);
			recyclerView.addItemDecoration(new MyDividerDecoration(getContext()));
			recyclerView.setAdapter(mAdapter);
			recyclerView.setHasFixedSize(true);
			recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
			refreshLayout.setColorSchemeResources(MyThemes.getColorPrimary(), MyThemes.getColorAccent());
			getCount();
			
			mAdapter.setOnItemClickListener(new LearnListAdapter.OnItemClickListener()
			{
				@Override
				public void onItemClick(String id, String title, String sub, String url)
				{
					//更新数据
					/*update();
					 if (true)return;*/
					ReadUtil.getInstance().addItemAide(id);
					Intent intent = new Intent(getActivity(), AndroidWeb.class);
					intent.putExtra("id", id);
					intent.putExtra("url", url);
					intent.putExtra("title", title);
					intent.putExtra("sub", sub);
					intent.putExtra("type", type);
					Common.startActivityOptions(getActivity(), intent);
					mAdapter.notifyDataSetChanged();
				}
			});

			refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
			{
				@Override
				public void onRefresh()
				{
					getCount();
				}
			});

			recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
			{
				@Override
				public void onScrolled(RecyclerView view, int x, int y)
				{
					if (view != null)
					{
						if (view.computeVerticalScrollExtent() + view.computeVerticalScrollOffset() >= view.computeVerticalScrollRange())
							addItem();
					}
				}
			});
		}
	}

	private void getCount()
	{
		if (!Common.isNet(getContext()))
		{
			init();
			return;
		}
		if (mCount < 0) {
			BmobQuery<AndroidIDE> query = new BmobQuery<AndroidIDE>();
			query.addWhereEqualTo("enable", true);
			query.count(AndroidIDE.class, new CountListener() {
				@Override
				public void done(Integer count, BmobException e) {
					if (e == null) {
						mCount = count;
						if (count == 0) {
							mHandler.sendEmptyMessage(3);
							return;
						}
					} else {
						mCount = -1;
						ExceptionHandler.log("AideFragment-count", e);
					}
					refresh();
				}
			});
		} else {
			refresh();
		}
	}

	private void refresh()
	{
		if (mCount < 0 || !Common.isNet(getContext()))
		{
			init();
			return;
		}
		if (mAdapter.getItemCount() == 0)
		{
			errorText.setVisibility(View.VISIBLE);
			errorText.setText("正在加载，请稍候");
		}
		BmobQuery<AndroidIDE> query = new BmobQuery<AndroidIDE>();
		query.addWhereEqualTo("enable", true);
		query.order("order");
		query.setLimit(15);
		query.findObjects(new FindListener<AndroidIDE>()
		{
			@Override
			public void done(List<AndroidIDE> list, BmobException e)
			{
				if (e == null) {
					mAdapter.setList(list);
				} else
					ExceptionHandler.log("AideFragment-refresh", e);
				mHandler.sendEmptyMessage(1);
			}
		});
	}

	private void addItem()
	{
		if (mCount > mAdapter.size())
		{
			recyclerView.stopScroll();
			proRefresh.setVisibility(View.VISIBLE);
			BmobQuery<AndroidIDE> query = new BmobQuery<AndroidIDE>();
			query.addWhereEqualTo("enable", true);
			query.order("order");
			query.setLimit(15);
			query.setSkip(mAdapter.size());
			query.findObjects(new FindListener<AndroidIDE>()
			{
				@Override
				public void done(List<AndroidIDE> list, BmobException e)
				{
					if (e == null)
					{
						if (list.size() > 0)
						{
							mAdapter.notifyItemInserted(mAdapter.addList(list));
							proRefresh.setVisibility(View.GONE);
							mAdapter.setEnd(mCount);
						}
					} else
						ExceptionHandler.log("AideFragment-add", e);
				}
			});
		}
	}

	private void init()
	{
		errorText.setText("加载失败\n请检查你的网络连接");
		if (refreshLayout.isRefreshing())
			refreshLayout.setRefreshing(false);
		if (mAdapter.getItemCount() > 0)
		{
			errorText.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			mAdapter.notifyDataSetChanged();
		} else {
			recyclerView.setVisibility(View.GONE);
			errorText.setVisibility(View.VISIBLE);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					init();
					break;
				case 3:
					errorText.setText("敬请期待");
					errorText.setVisibility(View.VISIBLE);
					recyclerView.setVisibility(View.GONE);
					if (refreshLayout.isRefreshing())
						refreshLayout.setRefreshing(false);
					break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onResume()
	{
		if (refreshLayout.isRefreshing())
			refreshLayout.setRefreshing(false);
		super.onResume();
	}
}
