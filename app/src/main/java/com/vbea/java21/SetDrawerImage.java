package com.vbea.java21;

import java.io.File;

import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import com.vbea.java21.list.DrawerAdapter;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.SettingUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

public class SetDrawerImage extends BaseActivity
{
	private DrawerAdapter adapter;
	private SharedPreferences spf;
	private SharedPreferences.Editor edt;
	//private String[] headItems = {"拍照","相册"};
	//private Uri TEMP_URI;

	@Override
	protected void before()
	{
		setContentView(R.layout.drawer_layout);
	}

	@Override
	protected void after()
	{
		enableBackButton();
		RecyclerView recyclerView = bind(R.id.drw_recyclerView);
		recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),
					(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 5)));
		adapter = new DrawerAdapter();
		recyclerView.setAdapter(adapter);
		spf = getSharedPreferences("java21", MODE_PRIVATE);
		edt = spf.edit();

		adapter.setOnItemClickListener(new DrawerAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(int id)
			{
				if (Common.APP_BACK_ID == id)
					return;
				Common.APP_BACK_ID = id;
				MyThemes.initBackColor(SetDrawerImage.this);
				if (Common.isLogin())
				{
					try
					{
						Common.mUser.settings = Common.getSettingJson(new SettingUtil());
						Common.updateUser();
					}
					catch (Exception e)
					{
						ExceptionHandler.log("setdraw.updateuser", e);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	protected void onFinish()
	{
		goBack();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	private void goBack()
	{
		if (MyThemes.ISCHANGED)
		{
			edt.putInt("back", Common.APP_BACK_ID);
			edt.commit();
		}
	}

	@Override
	public void onBackPressed()
	{
		goBack();
		super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.draw_item, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.item_drawer)
		{
			if (Util.hasAllPermissions(SetDrawerImage.this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
				Matisse.from(this).choose(MimeType.ofAll()).imageEngine(new GlideEngine()).thumbnailScale(0.85f).forResult(1);
			else
				Util.requestPermission(SetDrawerImage.this, 1001, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}
		return true;
	}
	
	public void startPhotoZoom(Uri uri)
	{
		CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setActivityTitle("裁剪图片")
		.setAspectRatio(7, 5).setOutputUri(Uri.fromFile(new File(Common.getDrawImagePath())))
		.setAutoZoomEnabled(true).start(this);
		//560*400
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != RESULT_OK)
			return;
		if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			/*CropImage.ActivityResult result = CropImage.getActivityResult(data);
			File file = new File(result.getUri().getPath());
			Util.toastShortMessage(this, ""+file.exists());
			try {
				FileUtils.copyFile(file, new File(Common.getDrawImagePath()));*/
				Common.APP_BACK_ID = 100;
				Common.IsChangeICON = true;
				adapter.notifyDataSetChanged();
				MyThemes.initBackColor(SetDrawerImage.this);
			/*} catch (Exception e) {
				ExceptionHandler.log("CropImage.ActivityResult", e);
			}*/
			//Util.toastLongMessage(this, result.getUri().toString());
			//Util.saveCropBitmap(result.getUri().getPath(), );
			
			return;
		} else if (requestCode == 1) {
			startPhotoZoom(Matisse.obtainResult(data).get(0));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1001 && Util.hasAllPermissionsGranted(grantResults))
			Matisse.from(this).choose(MimeType.ofAll()).imageEngine(new GlideEngine()).thumbnailScale(0.85f).forResult(1);
	}
}
