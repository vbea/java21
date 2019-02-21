package com.vbea.java21;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import android.support.v7.app.AppCompatActivity;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.widget.TouchImageView;
import com.vbea.java21.classes.Util;

public class ShowWebImage extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagewebs);
		WebView web = findViewById(R.id.imgWebView);
		WebSettings wset = web.getSettings();
		wset.setSupportZoom(true);
		wset.setBuiltInZoomControls(true);
		wset.setDisplayZoomControls(false);
		wset.setUseWideViewPort(true);
		wset.setLoadWithOverviewMode(true);
		web.loadUrl(getIntent().getStringExtra("url"));
	}
	
	public static Drawable loadImageFromUrl(String url) throws IOException
	{
		URL m = new URL(url);
		InputStream i = (InputStream) m.getContent();
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	/** 
	 * 异步加载图片 
	 */
	public class LoadImageAsyncTask extends AsyncTask<String, Integer, Bitmap>
	{
		private TouchImageView touchImageView;

		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			if (null != touchImageView)
				touchImageView.setImageBitmap(bitmap);
		}

		// 设置图片视图实例
		public void setImageView(TouchImageView image)
		{
			this.touchImageView = image;
		}

		@Override
		protected Bitmap doInBackground(String... params)
		{
			Bitmap bitmap = Util.getNetBitmap(params[0]); // 调用前面 ApacheUtility 类的方法
			return bitmap;
		}
	}

	@Override
	public void onBackPressed()
	{
		finishAfterTransition();
	}
}
