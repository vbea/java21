package com.vbea.java21;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DividerItemDecoration;

import com.vbea.java21.data.SQLHelper;
import com.vbea.java21.list.ModeAdapter;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.ExceptionHandler;

public class Modelist extends AppCompatActivity
{
	/**
	 * 邠心工作室
	 * 21天学通Java
	 * 语言列表页面
	 * 2017/12/04 一
	 */
	private RecyclerView recyclerView;
	private ModeAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modelist);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		recyclerView = (RecyclerView) findViewById(R.id.mode_recyclerView);
		mAdapter = new ModeAdapter(new SQLHelper(this).select());
		setSupportActionBar(tool);
		DividerItemDecoration decoration = new DividerItemDecoration(Modelist.this, DividerItemDecoration.VERTICAL);
		decoration.setDrawable(getResources().getDrawable(R.drawable.ic_divider));
		recyclerView.addItemDecoration(decoration);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		mAdapter.close();
		super.onDestroy();
	}
}
