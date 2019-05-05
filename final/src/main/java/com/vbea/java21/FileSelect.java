package com.vbea.java21;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.Environment;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.content.Intent;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.EditText;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.design.widget.FloatingActionButton;

import com.vbea.java21.data.FileItem;
import com.vbea.java21.list.FileAdapter;
import com.vbea.java21.view.MyDividerDecoration;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbea.java21.view.MyAlertDialog;

public class FileSelect extends BaseActivity
{
	/**
	 * 邠心工作室
	 * 21天学通Java
	 * 文件夹选取页面
	 * 2017/12/04
	 */
	private RecyclerView recyclerView;
	private FileAdapter mAdapter;
	private SimpleDateFormat sdate;
	private List<FileItem> mList, fileList;
	private File rootPath, currentPath;
	private TextView txtLocation;
	private FloatingActionButton btnDone;
	private boolean isCompleted = false;

	@Override
	protected void before()
	{
		setContentView(R.layout.filelist);
	}

	@Override
	public void after()
	{
		enableBackButton();
		recyclerView = bind(R.id.file_recyclerView);
		btnDone = bind(R.id.fbtn_done);
		txtLocation = bind(R.id.file_location);
		mAdapter = new FileAdapter();
		recyclerView.addItemDecoration(new MyDividerDecoration(this));
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		sdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		init();
		
		mAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener()
		{
			@Override
			public void onOpenFile(String name)
			{
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				String mime = Util.getMimeType(name);
				if (Util.isAndroidN())
				{
					StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
					StrictMode.setVmPolicy(builder.build());
				}
				intent.setDataAndType(Uri.fromFile(new File(currentPath + File.separator + name)), mime);
				startActivity(intent);
			}

			@Override
			public void onItemClick(String name, boolean uplev)
			{
				if (uplev)
					listFiles(currentPath.getParentFile());
				else
					listFiles(new File(currentPath, name));
			}
			
			@Override
			public void onDelete(String name)
			{
				final File folder = new File(currentPath, name);
				Util.showConfirmCancelDialog(FileSelect.this, "删除文件夹", "确认要删除此文件夹？", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int s)
					{
						if (folder.list().length != 0)
							Util.toastShortMessage(getApplicationContext(), "文件夹不为空，无法删除");
						else
						{
							if (folder.delete())
							{
								Util.toastShortMessage(getApplicationContext(), "操作成功");
								listFiles(currentPath);
							}
							else
								Util.toastShortMessage(getApplicationContext(), "操作失败");
						}
					}
				});
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
		mList = new ArrayList<>();
		fileList = new ArrayList<>();
		String def = getIntent().getStringExtra("default");
		if (!Util.isNullOrEmpty(def)) {
			File defFile = new File(def);
			if (!defFile.exists())
				defFile.mkdirs();
			listFiles(defFile);
		} else
			listFiles(rootPath);
	}
	
	public void listFiles(final File files)
	{
		currentPath = files;
		mList.clear();
		fileList.clear();
		txtLocation.setText(files.getPath());
		if (!currentPath.equals(rootPath))
			mList.add(new FileItem().addUplev());
		if (!files.exists()) {
			handler.sendEmptyMessage(1);
			return;
		}
		isCompleted = false;
		handler.sendEmptyMessageDelayed(2, 100);
		if (files.exists())
		{
			new Thread(new Runnable()
			{
				public void run()
				{
					for (File f : files.listFiles())
					{
						if (f.isDirectory())
						{
							FileItem item = new FileItem();
							item.setName(f.getName());
							item.setIsFolder(true);
							item.setDetail(sdate.format(new Date(f.lastModified())));
							item.setSize(f.list().length + "个项目");
							mList.add(item);
						}
						else if (Common.isShowFile)
						{
							FileItem item = new FileItem();
							item.setName(f.getName());
							item.setIsFolder(false);
							item.setDetail(sdate.format(new Date(f.lastModified())));
							item.setSize(Util.getFormatSize(f.length()));
							fileList.add(item);
						}
					}
					mAdapter.clear();
					mAdapter.addList(mList);
					if (Common.isShowFile && fileList.size() > 0)
						mAdapter.addList(fileList);
					handler.sendEmptyMessage(1);
				}
			}).start();
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					isCompleted = true;
					mAdapter.notifyDataSetChanged();
					break;
				case 2:
					if (!isCompleted)
					{
						mAdapter.clear();
						mAdapter.addItem(new FileItem().addLoading());
						mAdapter.notifyDataSetChanged();
					}
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
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.file_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.item_showfile).setTitle(Common.isShowFile ? "只显示文件夹" : "显示所有文件");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.item_showfile)
		{
			Common.isShowFile = !Common.isShowFile;
			listFiles(currentPath);
		}
		else if (item.getItemId() == R.id.item_root)
			listFiles(rootPath);
		else if (item.getItemId() == R.id.item_add)
		{
			View view = LayoutInflater.from(FileSelect.this).inflate(R.layout.webhome, null);
			final EditText edt = (EditText) view.findViewById(R.id.edt_webhome);
			edt.setHint("请输入文件夹名");
			MyAlertDialog dialog = new MyAlertDialog(FileSelect.this);
			dialog.setTitle("新建文件夹");
			dialog.setView(view);
			dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface d, int s)
				{
					String filename = edt.getText().toString();
					if (Util.isNullOrEmpty(filename))
						Util.toastShortMessage(getApplicationContext(), "无效的文件夹名");
					else
					{
						File file = new File(currentPath, filename);
						if (file.exists() && file.isDirectory())
							Util.toastShortMessage(getApplicationContext(), "文件夹已存在");
						else
						{
							file.mkdir();
							listFiles(currentPath);
						}
					}
				}
			});
			dialog.setNegativeButton(android.R.string.cancel, null);
			dialog.show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed()
	{
		if (currentPath.getAbsolutePath().equals(rootPath.getAbsolutePath()))
			super.onBackPressed();
		else
			listFiles(currentPath.getParentFile());
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
