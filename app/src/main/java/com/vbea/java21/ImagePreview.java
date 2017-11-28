package com.vbea.java21;

import android.os.Bundle;
import android.view.View;
import android.graphics.Bitmap;

import android.support.v7.app.AppCompatActivity;
import com.vbea.java21.widget.TouchImageView;

public class ImagePreview extends AppCompatActivity
{
	public static Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageviews);
		
		TouchImageView img = (TouchImageView) findViewById(R.id.imgTouch);
		if (bitmap != null)
			img.setImageBitmap(bitmap);
		else
			img.setImageResource(R.mipmap.ic_launcher);
	}

	@Override
	public void onBackPressed()
	{
		finishAfterTransition();
	}
}
