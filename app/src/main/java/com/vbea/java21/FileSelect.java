package com.vbea.java21;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Environment;
import android.view.View;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.design.widget.FloatingActionButton;

import com.vbea.java21.data.FileItem;
import com.vbea.java21.list.FileAdapter;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.ExceptionHandler;

public class FileSelect extends AppCompatActivity
{
	/**
	 * 邠心工作室
	 * 21天学通Java
	 * 文件夹选取页面
	 * 2017/12/04 一
	 */
	private RecyclerView recyclerView;
	private FileAdapter mAdapter;
	private SimpleDateFormat sdate;
	private List<FileItem> mList;
	private File rootPath, currentPath;
	private FloatingActionButton btnDone;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filelist);
		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		recyclerView = (RecyclerView) findViewById(R.id.file_recyclerView);
		btnDone = (FloatingActionButton) findViewById(R.id.fbtn_done);
		mAdapter = new FileAdapter();
		setSupportActionBar(tool);
		DividerItemDecoration decoration = new DividerItemDecoration(FileSelect.this, DividerItemDecoration.VERTICAL);
		decoration.setDrawable(getResources().getDrawable(R.drawable.ic_divider));
		recyclerView.addItemDecoration(decoration);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		sdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		init();
		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});
		
		mAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(String name, boolean uplev)
			{
				if (uplev)
					listFiles(currentPath.getParentFile());
				else
					listFiles(new File(currentPath, name));
			}
		});
		
		btnDone.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (currentPath != null)
					selectedFile(currentPath.getAbsolutePath());
			}
		});
	}
	
	public void init()
	{
		rootPath = Environment.getExternalStorageDirectory();
		mList = new ArrayList<FileItem>();
		String def = getIntent().getStringExtra("default");
		if (!Util.isNullOrEmpty(def))
			listFiles(new File(def));
		else
			listFiles(rootPath);
	}
	
	public void listFiles(File files)
	{
		currentPath = files;
		mList.clear();
		if (!currentPath.equals(rootPath))
			mList.add(new FileItem().addUplev());
		if (files.exists())
		{
			for (File f : files.listFiles())
			{
				if (f.isDirectory())
				{
					FileItem item = new FileItem();
					item.setName(f.getName());
					item.setDetail(sdate.format(new Date(f.lastModified())));
					mList.add(item);
				}
			}
		}
		mAdapter.setList(mList);
		handler.sendEmptyMessageDelayed(1, 100);
	}
	
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					mAdapter.notifyDataSetChanged();
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	private void selectedFile(String path)
	{
		Intent intent = new Intent();
		intent.putExtra("path", path);
		setResult(RESULT_OK, intent);
		finishAfterTransition();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
