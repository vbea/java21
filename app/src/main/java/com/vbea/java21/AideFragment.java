package com.vbea.java21;

import java.util.List;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.content.DialogInterface;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.DividerItemDecoration;
import com.vbea.java21.data.AndroidIDE;
import com.vbea.java21.list.AideAdapter;
import com.vbea.java21.list.MyDecoration;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
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
	private AideAdapter mAdapter;
	private List<AndroidIDE> mList;
	private View rootView;
	private int mCount = -1;
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
			mList = new ArrayList<AndroidIDE>();
			mAdapter = new AideAdapter();
			errorText = (TextView) view.findViewById(R.id.txt_andError);
        	recyclerView = (RecyclerView) view.findViewById(R.id.cpt_recyclerView);
			refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swp_refresh);

			DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
			decoration.setDrawable(getResources().getDrawable(R.drawable.ic_divider));
			recyclerView.addItemDecoration(decoration);
			recyclerView.setAdapter(mAdapter);
			recyclerView.setHasFixedSize(true);
			recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
			refreshLayout.setColorSchemeResources(MyThemes.getColorPrimary(), MyThemes.getColorAccent());
			//mList = new ArrayList<AndroidHtml>();
			if (Common.isLogin())
				getCount();
			else
			{
				errorText.setVisibility(View.VISIBLE);
				errorText.setText("请登录后下拉刷新获取章节列表");
			}
			
			mAdapter.setOnItemClickListener(new AideAdapter.OnItemClickListener()
			{
				@Override
				public void onItemClick(String id, String title, String sub, String url)
				{
					//更新数据
					/*update();
					 if (true)return;*/
					//Common.addJavaEeRead(id);
					Intent intent = new Intent(getActivity(), AndroidWeb.class);
					intent.putExtra("id", id);
					intent.putExtra("url", url);
					intent.putExtra("title", title);
					intent.putExtra("sub", sub);
					intent.putExtra("type", 4);
					Common.startActivityOptions(getActivity(), intent);
					mAdapter.notifyDataSetChanged();
				}
			});

			refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
			{
				@Override
				public void onRefresh()
				{
					if (Common.isNotLogin())
					{
						recyclerView.setVisibility(View.GONE);
						errorText.setVisibility(View.VISIBLE);
						errorText.setText("加载失败，请登录后重试");
					}
					else
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
		BmobQuery<AndroidIDE> query = new BmobQuery<AndroidIDE>();
		query.addWhereEqualTo("enable", true);
		query.count(AndroidIDE.class, new CountListener()
		{
			@Override
			public void done(Integer count, BmobException e)
			{
				if (e == null)
				{
					mCount = count;
					if (count == 0)
					{
						mHandler.sendEmptyMessage(3);
						return;
					}
				}
				else
					mCount = -1;
				refresh();
			}
		});
	}

	/*public void update()
	 {
	 Util.showConfirmCancelDialog(getActivity(), "数据更新", "你确定要更新数据吗", new DialogInterface.OnClickListener()
	 {
	 public void onClick(DialogInterface d, int s)
	 {
	 mPdialog = ProgressDialog.show(getActivity(), null, "请稍候...");
	 new UpdateThread().start();
	 }
	 });
	 }*/

	private void refresh()
	{
		if (mCount < 0 || !Common.isNet(getContext()) || mCount == mList.size())
		{
			init();
			return;
		}
		if (mList == null || mList.size() == 0)
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
				if (e == null)
				{
					if (list.size() > 0)
					{
						mList = list;
						mAdapter.setEnd(false);
					}
				}
				mHandler.sendEmptyMessage(1);
			}
		});
	}

	private void addItem()
	{
		if (mCount > mList.size())
		{
			BmobQuery<AndroidIDE> query = new BmobQuery<AndroidIDE>();
			query.addWhereEqualTo("enable", true);
			query.order("order");
			query.setLimit(15);
			query.setSkip(mList.size());
			query.findObjects(new FindListener<AndroidIDE>()
			{
				@Override
				public void done(List<AndroidIDE> list, BmobException e)
				{
					if (e == null)
					{
						if (list.size() > 0)
						{
							mList.addAll(list);
						}
					}
					mHandler.sendEmptyMessage(2);
				}
			});
		}
	}

	private void init()
	{
		errorText.setText("加载失败\n请检查你的网络连接");
		if (mList.size() > 0)
		{
			errorText.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
		}
		else
			errorText.setVisibility(View.VISIBLE);
		if (refreshLayout.isRefreshing())
			refreshLayout.setRefreshing(false);
		mAdapter.setList(mList);
		mAdapter.notifyDataSetChanged();
	}

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
				case 2:
					mAdapter.setList(mList);
					if (mList.size() == mCount)
						mAdapter.setEnd(true);
					mAdapter.notifyItemInserted(mAdapter.getItemCount());
					break;
				case 3:
					errorText.setText("敬请期待");
					errorText.setVisibility(View.VISIBLE);
					recyclerView.setVisibility(View.GONE);
					if (refreshLayout.isRefreshing())
						refreshLayout.setRefreshing(false);
					break;
				/*case 4:
					 Util.toastShortMessage(getActivity(), "更新成功");
					 mPdialog.dismiss();
					 break;
				 case 5:
					 Util.toastShortMessage(getActivity(), "更新失败");
					 mPdialog.dismiss();
					 break;*/
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onResume()
	{
		if (Common.isNotLogin())
		{
			recyclerView.setVisibility(View.GONE);
		}
		if (refreshLayout.isRefreshing())
			refreshLayout.setRefreshing(false);
		super.onResume();
	}

	class UpdateThread extends Thread implements Runnable
	{
		public void run()
		{
			try
			{
				for (AndroidIDE item : mList)
				{
					if (item.isTitle)
						continue;
					item.url = item.url.replace("http://vbea.wicp.net/","");
					item.update(new UpdateListener()
					{
						public void done(BmobException e)
						{
							if (e!=null)
								ExceptionHandler.log("update", e.toString());
						}
					});
					sleep(500);
				}
				mHandler.sendEmptyMessage(4);
			}
			catch (Exception e)
			{
				mHandler.sendEmptyMessage(5);
				ExceptionHandler.log("update", e.toString());
			}
		}
	}
}