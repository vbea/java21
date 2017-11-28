package com.vbea.java21;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import com.vbea.java21.list.DrawerAdapter;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;

public class SetDrawerImage extends AppCompatActivity
{
	private DrawerAdapter adapter;
	private SharedPreferences spf;
	private SharedPreferences.Editor edt;
	private String diyImagePath = "";
	private Uri imageUri = Uri.parse("file:///" + Common.DrawImagePath);
	private String[] headItems = {"拍照","从手机相册选择","从图库选择"};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(MyThemes.getTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_layout);

		Toolbar tool = (Toolbar) findViewById(R.id.toolbar);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.drw_recyclerView);
		recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),
					(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 5)));
		setSupportActionBar(tool);
		adapter = new DrawerAdapter();
		recyclerView.setAdapter(adapter);
		spf = getSharedPreferences("java21", MODE_PRIVATE);
		edt = spf.edit();

		tool.setNavigationOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				supportFinishAfterTransition();
			}
		});

		adapter.setOnItemClickListener(new DrawerAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick(int id)
			{
				if (Common.APP_BACK_ID == id)
					return;
				Common.APP_BACK_ID = id;
				MyThemes.initBackColor(SetDrawerImage.this);
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	protected void onDestroy()
	{
		if (Common.isCanUploadUserSetting())
			Common.updateUser();
		edt.putInt("back", Common.APP_BACK_ID);
		edt.commit();
		super.onDestroy();
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
			AlertDialog.Builder dialogBuild = new AlertDialog.Builder(SetDrawerImage.this);
			dialogBuild.setTitle("自定义");
			dialogBuild.setItems(headItems, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int item)
				{
					dialog.dismiss();
					switch (item)
					{
						case 0:
							Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
							startActivityForResult(intent1, 10);
							break;
						case 1:
							Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
							intent.addCategory(Intent.CATEGORY_OPENABLE);
							startActivityForResult(intent, 1);
							break;
						case 2:
							Intent intent2 = new Intent(Intent.ACTION_PICK);
							intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
							startActivityForResult(intent2, 1);
							break;
					}
				}
			});
			dialogBuild.show();
		}
		return true;
	}
	
	public void startPhotoZoom(Uri uri)
	{    
		if(uri==null)
			return;
		Intent intent = new Intent("com.android.camera.action.CROP");    
		intent.setDataAndType(uri, "image/*");    
		// 设置裁剪    
		intent.putExtra("crop", "true");    
		// aspectX aspectY 是宽高的比例    
		intent.putExtra("aspectX", 7);    
		intent.putExtra("aspectY", 5);    
		// outputX outputY 是裁剪图片宽高    
		intent.putExtra("outputX", 560);    
		intent.putExtra("outputY", 400);    
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false); 
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, 11);    
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode)
		{
			case 1:
				try
				{
					startPhotoZoom(data.getData());
				}
				catch (Exception e)
				{
					ExceptionHandler.log("back_crop1",e.toString());
				}
				break;
			case 10:
				//ContentResolver cr = getContentResolver();
				try
				{
					startPhotoZoom(imageUri);
				}
				catch (Exception e)
				{
					ExceptionHandler.log("back_corp10",e.toString());
				}
				break;
			case 11:
				try
				{
					Common.APP_BACK_ID = 100;
					adapter.notifyDataSetChanged();
					MyThemes.initBackColor(SetDrawerImage.this);
				}
				catch (Exception e)
				{
					ExceptionHandler.log("set_back", e.toString());
				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
