package com.vbea.java21.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vbea.java21.R;
import com.vbea.java21.classes.CourseUtil;
import com.vbea.java21.ui.AndroidWeb;
import com.vbea.java21.classes.ReadUtil;
import com.vbea.java21.data.JavaEE;
import com.vbea.java21.adapter.LearnListAdapter;
import com.vbea.java21.classes.Common;
import com.vbea.java21.view.MyDividerDecoration;
import com.vbes.util.fragment.BaseFragment;

public class JavaFragment extends BaseFragment {
    //private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    //private TextView errorText;
    //private ProgressBar proRefresh;
    private LearnListAdapter<JavaEE> mAdapter;
    //private int mCount = -1;
    private final int type = 3;

    public JavaFragment() {
        super(R.layout.android);
    }

    @Override
    public void onInitView(View view) {
        mAdapter = new LearnListAdapter<>();
        //errorText = getView(R.id.txt_andError);
        //proRefresh = getView(R.id.refreshProgress);
        recyclerView = getView(R.id.cpt_recyclerView);
        //refreshLayout = getView(R.id.swp_refresh);
        recyclerView.addItemDecoration(new MyDividerDecoration(getContext()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //refreshLayout.setColorSchemeResources(MyThemes.getColorPrimary(), MyThemes.getColorAccent());
        getCount();
        mAdapter.setOnItemClickListener(new LearnListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String title, String sub, String url, int p) {
                //更新数据
					/*update();
					if (true)return;*/
                ReadUtil.getInstance().addItemJavaEE(title);
                Intent intent = new Intent(getActivity(), AndroidWeb.class);
                intent.putExtra("url", url);
                intent.putExtra("title", title);
                //intent.putExtra("sub", sub);
                intent.putExtra("type", type);
                Common.startActivityOptions(getActivity(), intent);
                mAdapter.notifyItemChanged(p);
            }
        });

        /*refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCount();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView view, int x, int y) {
                if (view != null) {
                    if (view.computeVerticalScrollExtent() + view.computeVerticalScrollOffset() >= view.computeVerticalScrollRange())
                        addItem();
                }
            }
        });*/
    }

    private void getCount() {
        /*if (!Common.isNet(getContext())) {
            init();
            return;
        }
        if (mCount < 0) {
            BmobQuery<JavaEE> query = new BmobQuery<JavaEE>();
            query.addWhereEqualTo("enable", true);
            query.count(JavaEE.class, new CountListener() {
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
                        ExceptionHandler.log("JavaFragment-count", e);
                    }
                    refresh();
                }
            });
        } else {
            refresh();
        }*/
        mAdapter.setList(new CourseUtil(getContext(), "java.json").getCourseList(JavaEE.class));
    }

    private void refresh() {
        /*if (mCount < 0 || !Common.isNet(getContext())) {
            init();
            return;
        }
        if (mAdapter.getItemCount() == 0) {
            proRefresh.setVisibility(View.VISIBLE);
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("正在加载，请稍候");
        }
        /*BmobQuery<JavaEE> query = new BmobQuery<JavaEE>();
        query.addWhereEqualTo("enable", true);
        query.order("order");
        query.setLimit(15);
        query.findObjects(new FindListener<JavaEE>() {
            @Override
            public void done(List<JavaEE> list, BmobException e) {
                if (e == null) {
                    mAdapter.setList(list);
                } else
                    ExceptionHandler.log("JavaFragment-refresh", e);
                mHandler.sendEmptyMessage(1);
            }
        });*/
    }

    private void addItem() {
        /*if (mCount > mAdapter.size()) {
            recyclerView.stopScroll();
            proRefresh.setVisibility(View.VISIBLE);
            BmobQuery<JavaEE> query = new BmobQuery<JavaEE>();
            query.addWhereEqualTo("enable", true);
            query.order("order");
            query.setLimit(15);
            query.setSkip(mAdapter.size());
            query.findObjects(new FindListener<JavaEE>() {
                @Override
                public void done(List<JavaEE> list, BmobException e) {
                    if (e == null) {
                        if (list.size() > 0) {
                            mAdapter.notifyItemInserted(mAdapter.addList(list));
                            proRefresh.setVisibility(View.GONE);
                            mAdapter.setEnd(mCount);
                        }
                    } else
                        ExceptionHandler.log("JavaFragment-add", e);
                    mHandler.sendEmptyMessage(2);
                }
            });
        }*/
    }

    private void init() {
        /*proRefresh.setVisibility(View.GONE);
        errorText.setText("加载失败\n请检查你的网络连接");
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);*/
        if (mAdapter.getItemCount() > 0) {
            //errorText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
            //errorText.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    init();
                    break;
                case 3:
                    /*errorText.setText("敬请期待");
                    errorText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    if (refreshLayout.isRefreshing())
                        refreshLayout.setRefreshing(false);*/
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onResume() {
        /*if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);*/
        super.onResume();
    }
}
