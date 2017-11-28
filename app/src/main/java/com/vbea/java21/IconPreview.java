package com.vbea.java21;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.net.Uri;
import android.widget.ImageView;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.vbea.java21.data.Users;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import cn.bmob.v3.exception.BmobException;

public class IconPreview extends AppCompatActivity
{
	private ImageView img_icon;
	private String[] headItems = {"拍照","从手机相册选择","从图库选择"};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.headicon);
		
		img_icon = (ImageView) findViewById(R.id.img_headicon);
		ImageView setIcon = (ImageView) findViewById(R.id.img_setIcon);
		LayoutParams params = img_icon.getLayoutParams();
		params.height = getWindowManager().getDefaultDisplay().getWidth();
		img_icon.setLayoutParams(params);
		setIcon(Common.getIcon());
		
		img_icon.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				supportFinishAfterTransition();
			}
		});
		
		setIcon.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				AlertDialog.Builder dialogBuild = new AlertDialog.Builder(IconPreview.this);
				dialogBuild.setTitle("设置头像");
				dialogBuild.setItems(headItems, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
						dialog.dismiss();
						switch (item)
						{
							case 0:
								Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
								//intent2.setType("image/*");
								//intent2.addCategory(Intent.CATEGORY_OPENABLE);
								intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
								startActivityForResult(intent2, 1);
								break;
						}
					}
				});
				dialogBuild.show();
			}
		});
	}
	
	public void startPhotoZoom(Uri uri)
	{    
		if(uri==null){  
			return;
		}  
		Intent intent = new Intent("com.android.camera.action.CROP");    
		intent.setDataAndType(uri, "image/*");    
		// 设置裁剪    
		intent.putExtra("crop", "true");    
		// aspectX aspectY 是宽高的比例    
		intent.putExtra("aspectX", 1);    
		intent.putExtra("aspectY", 1);    
		// outputX outputY 是裁剪图片宽高    
		intent.putExtra("outputX", 270);    
		intent.putExtra("outputY", 270);    
		intent.putExtra("return-data", true);    
		startActivityForResult(intent, 11);    
	}
	
	private void startPhotoZoom(Intent data)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		if (data.getData() != null)
			intent.setData(data.getData());
		intent.setType("image/*");
		if (data.getExtras() != null)
			intent.putExtras(data.getExtras());
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 270);
		intent.putExtra("outputY", 270);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		//intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		//intent.putExtra("noFaceDetection", true);
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
					ExceptionHandler.log("icon_crop1",e.toString());
				}
				break;
			case 10:
				//ContentResolver cr = getContentResolver();
				try
				{
					startPhotoZoom(data);
				}
				catch (Exception e)
				{
					ExceptionHandler.log("icon_corp10",e.toString());
				}
				break;
			case 11:
				try
				{
					Bitmap bitmap = null;
					if (data.getData() != null)
					{
						bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
					}
					else if (data.getExtras() != null)
					{
						bitmap = data.getExtras().getParcelable("data");
					}
					if (bitmap != null)
					{
						setIcon(bitmap);
						String filename = Common.mUser.name + ".png";
						File dir = new File(Common.IconPath);
						if (!dir.exists())
							dir.mkdirs();
						File file = new File(Common.IconPath + filename);
						saveFile(bitmap, file);
						Common.IsChangeICON = true;
						if (file.exists())
							uploadIcon(file);
					}
				}
				catch (Exception e)
				{
					ExceptionHandler.log("set_icon", e.toString());
				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void saveFile(Bitmap bm, File file) throws IOException
	{
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
    }

	public void uploadIcon(File file)
	{
		final BmobFile icon = new BmobFile(file);
		icon.upload(new UploadFileListener()
		{
			@Override
			public void done(BmobException p1)
			{
				if (p1 == null)
				{
					Util.toastShortMessage(getApplicationContext(), "上传成功");
					Common.mUser.icon = icon;
					Common.updateUser();
				}
				else
					//ExceptionHandler.log("上传头像："+p1.toString());
					Util.toastShortMessage(getApplicationContext(), "上传失败");
			}
		});
	}
	
	public void setIcon(Bitmap src)
	{
		if (src != null)
			img_icon.setImageBitmap(src);
	}
}
