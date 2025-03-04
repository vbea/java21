package com.vbea.java21.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.data.SQLHelper;
import com.vbea.java21.adapter.ModeAdapter;
import com.vbea.java21.view.MyDividerDecoration;

public class Modelist extends BaseActivity {
    /**
     * 邠心工作室
     * 21天学通Java
     * 语言列表页面
     * 2017/12/04 一
     */
    private RecyclerView recyclerView;
    private ModeAdapter mAdapter;

    @Override
    protected void before() {
        setContentView(R.layout.modelist);
    }

    @Override
    public void after() {
        enableBackButton(R.id.toolbar);
        recyclerView = bind(R.id.mode_recyclerView);
        mAdapter = new ModeAdapter(new SQLHelper(this).select());
        recyclerView.addItemDecoration(new MyDividerDecoration(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAdapter.close();
        super.onDestroy();
    }
}
