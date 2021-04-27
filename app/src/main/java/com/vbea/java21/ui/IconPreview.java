package com.vbea.java21.ui;

import java.io.File;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.net.Uri;
import android.widget.ImageView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import com.vbea.java21.R;
import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import cn.bmob.v3.exception.BmobException;

import com.vbes.util.CropImage;
import com.vbes.util.GalleryUtil;
import com.vbes.util.VbeUtil;
import com.vbes.util.matisse.entity.CaptureStrategy;
import com.vbes.util.media.MimeType;
import com.vbes.util.view.CropImageView;

public class IconPreview extends AppCompatActivity {
	private ImageView img_icon;
	private Uri ICON_URI;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.headicon);
		
		img_icon = (ImageView) findViewById(R.id.img_headicon);
		ImageView setIcon = (ImageView) findViewById(R.id.img_setIcon);
		LayoutParams params = img_icon.getLayoutParams();
		params.height = getWindowManager().getDefaultDisplay().getWidth();
		img_icon.setLayoutParams(params);
		Common.setIcon(img_icon, false);
		
		img_icon.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				supportFinishAfterTransition();
			}
		});
		
		setIcon.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Util.hasAllPermissions(IconPreview.this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
					GalleryUtil.from(IconPreview.this).choose(MimeType.ofImage()).theme(MyThemes.getTheme()).capture(true)
							.captureStrategy(new CaptureStrategy(true, "com.vbea.java21.fileprovider")).thumbnailScale(0.85f).forResult(1);
				else
					Util.requestPermission(IconPreview.this, 1001, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
			}
		});
	}
	
	public void startPhotoZoom(Uri uri) {
		if (uri == null)
			return;
		ICON_URI = Uri.fromFile(new File(Common.getAvatarPath(this)));
		CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setTheme(MyThemes.getTheme())
		.setAspectRatio(1, 1).setOutputUri(ICON_URI)
		.setAutoZoomEnabled(true).start(this);  
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(ICON_URI));
				if (bitmap != null) {
					setIcon(bitmap);
					File file = new File(Common.getAvatarPath(this));
					Common.IsChangeICON = true;
					if (file.exists()) VbeUtil.toastShortMessage(this,"修改成功");
					//	uploadIcon(file);
				}
			} catch (Exception e) {
				ExceptionHandler.log("set_icon", e.toString());
			}
		} else if (requestCode == 1) {
			startPhotoZoom(GalleryUtil.obtainResult(data).get(0));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void uploadIcon(File file) {
		final BmobFile icon = new BmobFile(file);
		icon.upload(new UploadFileListener()
		{
			@Override
			public void done(BmobException p1)
			{
				if (p1 == null) {
					Util.toastShortMessage(getApplicationContext(), "上传成功");
					Common.mUser.icon = icon;
					Common.updateUser();
				} else {
					ExceptionHandler.log("uploadIcon", p1.toString());
					Util.toastShortMessage(getApplicationContext(), "上传失败");
				}
			}
		});
	}
	
	public void setIcon(Bitmap src) {
		if (src != null)
			img_icon.setImageBitmap(src);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1001 && Util.hasAllPermissionsGranted(grantResults))
			GalleryUtil.from(IconPreview.this).choose(MimeType.ofImage()).theme(MyThemes.getTheme()).capture(true).capture(true)
					.captureStrategy(new CaptureStrategy(true, "com.vbea.java21.fileprovider")).thumbnailScale(0.85f).forResult(1);
	}
}
