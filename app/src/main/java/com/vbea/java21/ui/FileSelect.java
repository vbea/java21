package com.vbea.java21.ui;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import android.os.StrictMode;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
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

import com.vbea.java21.BaseActivity;
import com.vbea.java21.BuildConfig;
import com.vbea.java21.R;
import com.vbea.java21.data.FileItem;
import com.vbea.java21.adapter.FileAdapter;
import com.vbea.java21.view.MyDividerDecoration;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.Util;
import com.vbes.util.VbeUtil;
import com.vbes.util.lis.DialogResult;
import com.vbes.util.view.MyAlertDialog;

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
	public static final int RESULT_FILE_ACCESS = 5320;

	@Override
	protected void before() {
		setContentView(R.layout.filelist);
	}

	@Override
	public void after() {
		enableBackButton(R.id.toolbar);
		recyclerView = bind(R.id.file_recyclerView);
		btnDone = bind(R.id.fbtn_done);
		txtLocation = bind(R.id.file_location);
		mAdapter = new FileAdapter();
		recyclerView.addItemDecoration(new MyDividerDecoration(this));
		recyclerView.setAdapter(mAdapter);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		sdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (VbeUtil.isAndroidR()) {
			if (Environment.isExternalStorageManager()) {
				init();
			} else {
				VbeUtil.showResultDialog(this, "受到Android 11分区存储限制，需要设置允许访问非公共存储目录才能将文件保存在自定义目录，请点击设置并允许文件管理权限。", "访问受限", "设置", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						requestAccessFile();
					}
				});
			}
		} else {
			init();
		}
		
		mAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(FileItem fileItem) {
				if (fileItem.isFolder()) {
					if (fileItem.isUplevel()) {
						listFiles(currentPath.getParentFile());
					} else {
						listFiles(new File(currentPath, fileItem.getName()));
					}
				} else {
					String mime = Util.getMimeType(fileItem.getName());
					Intent intent;
					if (VbeUtil.isAndroidM()) {
						intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
					} else {
						intent = new Intent();
					}
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					/*if (Util.isAndroidN()) {
						StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
						StrictMode.setVmPolicy(builder.build());
					}*/
					if (Util.isAndroidN()) {
						intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
						intent.setDataAndType(FileProvider.getUriForFile(FileSelect.this, Common.FileProvider, new File(fileItem.getPath())), mime);
					} else {
						intent.setDataAndType(Uri.fromFile(new File(fileItem.getPath())), mime);
					}
					startActivity(intent);
				}
			}
			
			@Override
			public void onDelete(FileItem fileItem) {
				final File folder = new File(fileItem.getPath());
				VbeUtil.showConfirmCancelDialog(FileSelect.this, "删除文件夹", "确认要删除此文件夹？", new DialogResult() {
					@Override
					public void onConfirm() {
						if (folder.list().length != 0)
							Util.toastShortMessage(getApplicationContext(), "文件夹不为空，无法删除");
						else {
							if (folder.delete()) {
								Util.toastShortMessage(getApplicationContext(), "操作成功");
								listFiles(currentPath);
							} else {
								Util.toastShortMessage(getApplicationContext(), "操作失败");
							}
						}
					}

					@Override
					public void onCancel() {

					}
				});
			}
		});
		
		btnDone.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (currentPath != null)
					selectedFile(currentPath.getAbsolutePath());
			}
		});
	}
	
	public void init() {
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
	
	public void listFiles(final File files) {
		currentPath = files;
		mList.clear();
		fileList.clear();
		txtLocation.setText(files.getPath());
		mList.add(new FileItem().addLoading());
		if (files == null || !files.exists()) {
			mAdapter.clear();
		} else {
			mAdapter.setNewData(mList);
			VbeUtil.runDelayed(new Runnable() {
				public void run() {
					mList.clear();
					if (!currentPath.equals(rootPath))
						mList.add(new FileItem().addUplev());
					File[] filesList = files.listFiles();
					if (filesList != null) {
						for (File f : filesList) {
							if (f != null) {
								if (f.isDirectory()) {
									FileItem item = new FileItem();
									item.setName(f.getName());
									item.setPath(f.getAbsolutePath());
									item.setIsFolder(true);
									item.setDetail(sdate.format(new Date(f.lastModified())));
									item.setSize(getChildList(f) + "个项目");
									mList.add(item);
								} else if (Common.isShowFile) {
									FileItem item = new FileItem();
									item.setName(f.getName());
									item.setPath(f.getAbsolutePath());
									item.setIsFolder(false);
									item.setDetail(sdate.format(new Date(f.lastModified())));
									item.setSize(Util.getFormatSize(f.length()));
									fileList.add(item);
								}
							}
						}
					}
					mAdapter.setList(mList);
					if (Common.isShowFile && fileList.size() > 0) {
						mAdapter.addList(fileList);
					}
				}
			}, 0);
		}
	}

	private int getChildList(File direct) {
		if (direct.list() != null) {
			return direct.list().length;
		} else {
			return 0;
		}
	}
	
	private void selectedFile(String path) {
		Intent intent = new Intent();
		intent.putExtra("path", path);
		setResult(RESULT_OK, intent);
		finishAfterTransition();
	}

	//申请访问所有文件
	public void requestAccessFile() {
		Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
		startActivityForResult(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri), RESULT_FILE_ACCESS);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.file_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.item_showfile).setTitle(Common.isShowFile ? "只显示文件夹" : "显示所有文件");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.item_showfile) {
			Common.isShowFile = !Common.isShowFile;
			listFiles(currentPath);
		} else if (item.getItemId() == R.id.item_root) {
			listFiles(rootPath);
		} else if (item.getItemId() == R.id.item_add) {
			View view = LayoutInflater.from(FileSelect.this).inflate(R.layout.webhome, null);
			final EditText edt = (EditText) view.findViewById(R.id.edt_webhome);
			edt.setHint("请输入文件夹名");
			MyAlertDialog dialog = new MyAlertDialog(FileSelect.this);
			dialog.setTitle("新建文件夹");
			dialog.setView(view);
			dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int s) {
					String filename = edt.getText().toString();
					if (Util.isNullOrEmpty(filename)) {
						Util.toastShortMessage(getApplicationContext(), "无效的文件夹名");
					} else {
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
	public void onBackPressed() {
		if (currentPath.getAbsolutePath().equals(rootPath.getAbsolutePath()))
			super.onBackPressed();
		else
			listFiles(currentPath.getParentFile());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		if (requestCode == RESULT_FILE_ACCESS && Environment.isExternalStorageManager()) {
			init();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
