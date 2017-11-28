package com.vbea.java21;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import android.support.v7.app.AppCompatActivity;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.widget.TouchImageView;
import com.vbea.java21.classes.Util;

public class ShowWebImageActivity extends AppCompatActivity
{
	//private TextView imageTextView = null;
	private String imagePath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageviews);
		//this.imagePath = getIntent().getStringExtra("image");
		if (imagePath == null)
			imagePath = "null";
		//this.imageTextView = (TextView) findViewById(R.id.show_webimage_imagepath_textview);
		//imageTextView.setText(this.imagePath);
		//TouchImageView imageView = (TouchImageView) findViewById(R.id.imgTouch);
		Util.toastShortMessage(this, imagePath);
		/*try
		{
			imageView.setImageBitmap(((BitmapDrawable) ShowWebImageActivity.loadImageFromUrl(this.imagePath)).getBitmap());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}*/
		/*LoadImageAsyncTask task = new LoadImageAsyncTask();
		task.setImageView(imageView); 
		task.execute(this.imagePath); */
		// 执行任务,参数与 doInBackground(String... params) 方法参数一致
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
			Bitmap bitmap = GetBitmapByUrl(params[0]); // 调用前面 ApacheUtility 类的方法
			return bitmap;
		}
	}

	/** 
	 * 获取图片流 
	 *
	 * @param uri 图片地址 
	 * @return 
	 * @throws MalformedURLException 
	 */
	public static InputStream GetImageByUrl(String uri) throws MalformedURLException
	{
		URL url = new URL(uri);
		URLConnection conn;
		InputStream is;
		try
		{
			conn = url.openConnection();
			conn.connect();
			is = conn.getInputStream();
			//或者用如下方法
			//is=(InputStream)url.getContent();
			return is;
		}
		catch (IOException e)
		{
			ExceptionHandler.log("ShowWebImageActivity:" + "GetImageByUrl", e.toString());
		}
		return null;
	}

	 /** 
	 * 获取Bitmap 
	 *
	 * @param uri 图片地址 
	 * @return 
	 */
	public static Bitmap GetBitmapByUrl(String uri)
	{
		Bitmap bitmap;
		InputStream is;
		try
		{
			is = GetImageByUrl(uri);
			if (is != null)
			{
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
				return bitmap;
			}
		}
		catch (MalformedURLException e)
		{
			ExceptionHandler.log("ShowWebImageActivity:" + "GetBitmapByUrl-1", e.toString());
		}
		catch (IOException e)
		{
			ExceptionHandler.log("ShowWebImageActivity:" + "GetBitmapByUrl-2", e.toString());
		}
		catch (Exception e)
		{
			ExceptionHandler.log("ShowWebImageActivity:" + "GetBitmapByUrl-3", e.toString());
		}
		return null;
	}

	 /** 
	 * 获取Drawable 
	 *
	 * @param uri图片地址 
	 * @return 
	 */
	public static Drawable GetDrawableByUrl(String uri)
	{
		Drawable drawable;
		InputStream is;
		try
		{
			is = GetImageByUrl(uri); 
			drawable= Drawable.createFromStream(is, "src");
			is.close();
			return drawable;
		}
		catch (MalformedURLException e)
		{
			ExceptionHandler.log("ShowWebImageActivity:" + "GetDrawableByUrl-1", e.toString());
		}
		catch (IOException e)
		{
			ExceptionHandler.log("ShowWebImageActivity:" + "GetDrawableByUrl-2", e.toString());
		}
		return null;
	}
	
	@Override
	public void onBackPressed()
	{
		finishAfterTransition();
	}
}
