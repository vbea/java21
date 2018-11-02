package com.vbea.java21.classes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.Assert;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;
import android.webkit.MimeTypeMap;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import com.vbea.java21.R;
import org.apache.commons.io.FileUtils;

public class Util
{
	//byte转为字符串
    public static String bytesToHexString(byte[] src)
	{
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0)
            return null;
        for (int i = 0; i < src.length; i++)
		{
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
                stringBuilder.append(0);
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString)
	{
        if (hexString == null || hexString.equals(""))
            return null;
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++)
		{
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c)
	{
        return (byte)"0123456789ABCDEF".indexOf(c);
    }

    /*
     * 16进制数字字符集
     */
    private static String hexString="0123456789ABCDEF";
    /*
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */
    public static String toHexString(String str)
    {
		//根据默认编码获取字节数组
        byte[] bytes = null;
		try
		{
			bytes = str.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			ExceptionHandler.log("Util.toHexString", e.toString());
			//e.printStackTrace();
		}
		if (bytes == null)
			return null;
        StringBuilder sb=new StringBuilder(bytes.length*2);
		//将字节数组中每个字节拆解成2位16进制整数
        for(int i=0;i<bytes.length;i++)
        {
            sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
            sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
        }
        return sb.toString();
    }

    //转换十六进制编码为字符串
    public static String hexToString(String s)
    {
        if("0x".equals(s.substring(0, 2)))
        {
            s =s.substring(2);
        }
        byte[] baKeyword = new byte[s.length()/2];
        for(int i = 0; i < baKeyword.length; i++)
        {
            try
            {
                baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16));
            }
            catch(Exception e)
            {
				ExceptionHandler.log("Util.hexToString", e.toString());
                //e.printStackTrace();
            }
        }
        try
        {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        }
        catch (Exception e1)
        {
			ExceptionHandler.log("Util.hexToString_2", e1.toString());
			//e1.printStackTrace();
        }
        return s;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle)
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle)
		{
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try
		{
			output.close();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Util.bmpToByteArray", e.toString());
			//e.printStackTrace();
		}
		return result;
	}
	
	public static byte[] getHtmlByteArray(final String url)
	{
		URL htmlUrl = null;     
		InputStream inStream = null;     
		try
		{
			htmlUrl = new URL(url);         
			URLConnection connection = htmlUrl.openConnection();         
			HttpURLConnection httpConnection = (HttpURLConnection)connection;         
			int responseCode = httpConnection.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK)
				inStream = httpConnection.getInputStream();
		}
		catch (MalformedURLException e)
		{               
			ExceptionHandler.log("Util.getHtmlByteArray", e.toString());
		}
		catch (IOException e)
		{              
			ExceptionHandler.log("Util.getHtmlByteArray", e.toString());    
		} 
		byte[] data = inputStreamToByte(inStream);
		return data;
	}
	
	public static byte[] inputStreamToByte(InputStream is)
	{
		try
		{
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		}
		catch(Exception e)
		{
			ExceptionHandler.log("Util.inputStreamToByte", e.toString());
			//e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] readFromFile(String fileName, int offset, int len)
	{
		if (fileName == null)
			return null;
		File file = new File(fileName);
		if (!file.exists())
			return null;
		if (len == -1)
			len = (int) file.length();
		if(offset <0)
			return null;
		if(len <=0 )
			return null;
		if(offset + len > (int) file.length())
			return null;
		byte[] b = null;
		try
		{
			RandomAccessFile in = new RandomAccessFile(fileName, "r");
			b = new byte[len];
			in.seek(offset);
			in.readFully(b);
			in.close();
		}
		catch (Exception e)
		{
			ExceptionHandler.log("Util.readFromFile", e.toString());
		}
		return b;
	}

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels)
	{
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8)
		{
            roundedSize = 1;
            while (roundedSize < initialSize)
			{
                roundedSize <<= 1;
            }
        }
		else
            roundedSize = (initialSize + 7) / 8 * 8;
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels)
	{
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound)
		{
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1))
            return 1;
        else if (minSideLength == -1)
            return lowerBound;
        else
            return upperBound;
    }

    /**
     * 以最省内存的方式读取图片
     */
    public static Bitmap readBitmap(final String path)
	{
        try
		{
            FileInputStream stream = new FileInputStream(new File(path));
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 8;
            opts.inPurgeable=true;
            opts.inInputShareable=true;
            Bitmap bitmap = BitmapFactory.decodeStream(stream , null, opts);
            return bitmap;
        }
		catch (OutOfMemoryError e)
		{
			ExceptionHandler.log("readBitmap_OOM", e.toString());
            return null;
        }
		catch (Exception e)
		{
			ExceptionHandler.log("Util.readBitmap", e.toString());
            return null;
        }
    }
	
	public static void saveBitmap(Context context, String folder, String filename, Bitmap bitmap) throws IOException
	{
		File directory = new File(folder);
		if (!directory.exists())
			directory.mkdirs();
		File file = new File(directory, filename);
		if (file.exists())
			file.delete();
		file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		out.flush();
		out.close();
		updateGallery(context, file);
	}
	
	public static void updateGallery(Context context, File f)
	{
		try
		{
			//MediaStore.Images.Media.insertImage(context.getContentResolver(), f.getAbsolutePath(), f.getName(), null);
			ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.DATA, f.getAbsolutePath());
			values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
			context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f.getParentFile())));
		}
		catch (Exception e)
		{
			ExceptionHandler.log("updateGallery()", e);
		}
	}
	
	public static String getDeviceModel()
	{
		try
		{
			return Build.MODEL;
		}
		catch (Exception e)
		{
			return "";
		}
	}

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
	public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop)
	{
		Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);
		BitmapFactory.Options options = new BitmapFactory.Options();
		try
		{
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null)
			{
				tmp.recycle();
				tmp = null;
			}
			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1)
				options.inSampleSize = 1;
			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE)
			{
				options.inSampleSize++;
			}
			int newHeight = height;
			int newWidth = width;
			if (crop)
			{
				if (beY > beX)
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				else
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
			}
			else
			{
				if (beY < beX)
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				else
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
			}
			options.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm == null)
				return null;
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
			if (scale != null)
			{
				bm.recycle();
				bm = scale;
			}
			if (crop)
			{
				final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
				if (cropped == null)
					return bm;
				bm.recycle();
				bm = cropped;
			}
			return bm;
		}
		catch (OutOfMemoryError e)
		{
			ExceptionHandler.log("Util.extractThumbNail", e.toString());
			options = null;
		}
		return null;
	}
	
	public static final void showResultDialog(Context context, String msg, String title)
	{
		showResultDialog(context, msg, title, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface d, int s)
			{
				
			}
		});
	}
	
	public static final void showResultDialog(Context context, String msg, String title, String ok)
	{
		showResultDialog(context, msg, title, ok, null);
	}
	
	public static final void showResultDialog(Context context, String msg, String title, DialogInterface.OnClickListener lis)
	{
		showResultDialog(context, msg, title, "知道了", lis);
	}
	
	public static final void showResultDialog(Context context, String msg, String title, String ok, DialogInterface.OnClickListener lis)
	{
		if(msg == null) return;
		//String rmsg = msg.replace(",", "\n");
		new MyAlertDialog(context).setTitle(title).setMessage(msg).setNegativeButton(ok, lis).create().show();
	}
	
	public static void showConfirmCancelDialog(Context context, String title, String message, DialogInterface.OnClickListener posListener)
	{
		MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setPositiveButton(android.R.string.ok, posListener);
		dialog.setNegativeButton(android.R.string.cancel, null).create();
		//dialog.create().setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	public static void showConfirmCancelDialog(Context context, String message, DialogInterface.OnClickListener posListener)
	{
		showConfirmCancelDialog(context, android.R.string.dialog_alert_title, message, posListener);
	}
	
	public static void showConfirmCancelDialog(Context context, int title, String message, DialogInterface.OnClickListener posListener)
	{
		MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setPositiveButton(android.R.string.ok, posListener);
		dialog.setNegativeButton(android.R.string.cancel, null).create();
		//dialog.create().setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	public static void showConfirmCancelDialog(Context context, String ok, String message, String cacel, DialogInterface.OnClickListener posListener)
	{
		MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.setTitle(android.R.string.dialog_alert_title);
		dialog.setMessage(message);
		dialog.setPositiveButton(ok, posListener);
		dialog.setNegativeButton(cacel, null).create();
		//dialog.create().setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	public static String Join(String splitter, String[] strs)
	{
		if (strs.length <= 0)
			return "";
		StringBuffer sb = new StringBuffer();
        for(String s:strs)
		{
            sb.append(s+splitter);
        }
        return sb.toString().substring(0, sb.toString().length()-1);
	}
	
	public static String Join(String splitter, List<String> strs)
	{
		if (strs.size() <= 0)
			return "";
		StringBuffer sb = new StringBuffer();
        for(String s:strs)
		{
            sb.append(s+splitter);
        }
        return sb.toString().substring(0, sb.toString().length()-1);
	}
	
	//打印消息并且用Toast显示消息
	public static void toastShortMessage(Context activity, String message)
	{
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}

	public static void toastLongMessage(Context activity, String message)
	{
		Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
	}
	
	public static String trim(String src, char element)
	{
		boolean beginIndexFlag = true;
		boolean endIndexFlag = true;
		do
		{
			int beginIndex = src.indexOf(element)==0?1:0;
			int endIndex = src.lastIndexOf(element)+1==src.length()?src.lastIndexOf(element):src.length();
			src = src.substring(beginIndex,endIndex);
			beginIndexFlag=(src.indexOf(element)==0);
			endIndexFlag=(src.lastIndexOf(element)+1==src.length());
		}
		while(beginIndexFlag||endIndexFlag);
		return src;
	}

	/**
	 * 根据一个网络连接(String)获取bitmap图像
	 * 
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getNetBitmap(String imageUri)
	{
		// 显示网络上的图片
		Bitmap bitmap = null;
		try
		{
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
        }
		catch (OutOfMemoryError e)
		{
            e.printStackTrace();
            bitmap = null;
		}
		catch (IOException e)
		{
			ExceptionHandler.log("Util.getNetBitmap", e.toString());
            bitmap = null;
		}
		return bitmap;
	}
	
	//下载文件
	public static void downloadFile(final String url, String path, String name)
	{
		try
		{
			InputStream inStream = null;
			URL htmlUrl = new URL(url);         
			URLConnection connection = htmlUrl.openConnection();         
			connection.setDoInput(true);
			connection.connect();
			inStream = connection.getInputStream();
			File file = new File(path, name);
			if (!file.exists())
				file.createNewFile();
			FileUtils.copyToFile(inStream, file);
			inStream.close();
		}
		catch (Exception e)
		{              
			ExceptionHandler.log("Util.downloadFile", e.toString());    
		}
	}

    // =========
    // =通过URI获取本地图片的path
    // =兼容android 5.0
    // ==========
    public static String ACTION_OPEN_DOCUMENT = "android.intent.action.OPEN_DOCUMENT";
    public static int Build_VERSION_KITKAT = 19;
    public static String getPath(final Context context, final Uri uri)
	{
        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;
        // DocumentProvider
        if (isKitKat && isDocumentUri(context, uri))
		{
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri))
			{
                final String docId = getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type))
				{
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri))
			{
                final String id = getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri))
			{
                final String docId = getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type))
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                else if ("video".equals(type))
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                else if ("audio".equals(type))
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme()))
		{
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme()))
            return uri.getPath();
        return null;
    }

    private static final String PATH_DOCUMENT = "document";
    /**
     * Test if the given URI represents a {@link Document} backed by a
     * {@link DocumentsProvider}.
     */
    private static boolean isDocumentUri(Context context, Uri uri)
	{
        final List<String> paths = uri.getPathSegments();
        if (paths.size() < 2)
            return false;
        if (!PATH_DOCUMENT.equals(paths.get(0)))
            return false;
        return true;
    }

    private static String getDocumentId(Uri documentUri)
	{
        final List<String> paths = documentUri.getPathSegments();
        if (paths.size() < 2)
            throw new IllegalArgumentException("Not a document: " + documentUri);
        if (!PATH_DOCUMENT.equals(paths.get(0)))
            throw new IllegalArgumentException("Not a document: " + documentUri);
        return paths.get(1);
    }
	
	public static boolean AuthKey(Context context)
	{
		try
		{
			return MD5Util.CheckKey(context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures);
		}
		catch (Exception e)
		{
			return false;
		}
	}

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     *            [url=home.php?mod=space&uid=7300]@return[/url] The value of
     *            the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs)
	{
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try
		{
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst())
			{
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }
		finally
		{
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri)
	{
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri)
	{
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri)
	{
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
	
	public static String getFormatSize(long bytes) 
	{
        if (bytes < 1024)
            return bytes + "B";
		double kiloByte = bytes / 1024;
		if (kiloByte < 1024)
		{  
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));  
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";  
        }  
        double megaByte = kiloByte / 1024;  
        if (megaByte < 1024)
		{
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";  
        }
        double gigaByte = megaByte / 1024;  
        if (gigaByte < 1024)
		{
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));  
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";  
        }
        double teraByte = gigaByte / 1024;  
        BigDecimal result4 = new BigDecimal(teraByte);  
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";  
    }  

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri)
	{
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
	
	public static boolean isNullOrEmpty(String obj)
	{
		if (obj != null)
			return obj.equals("");
		return true;
	}
	
	public static String getFileType(String filename)
	{
		if (filename.indexOf(".") >= 0)
			return filename.substring(filename.lastIndexOf("."));
		return filename;
	}
	
	public static String getFileTypeName(String filename)
	{
		if (filename.indexOf(".") >= 0)
			return filename.substring(filename.lastIndexOf(".")+1);
		return filename;
	}
	
	public static boolean isImageFile(String path)
	{
		String p = path.toLowerCase();
		return p.endsWith(".jpg") || p.endsWith(".png") || p.endsWith(".gif") || p.endsWith(".bmp");
	}
	
	public static String removeEmptyItem(String[] list)
	{
		if (list.length == 1)
			return list[0];
		StringBuilder sb = new StringBuilder();
		for (String s : list)
		{
			if (!isNullOrEmpty(s))
			{
				sb.append(",");
				sb.append(s);
			}
		}
		return sb.toString().substring(1);
	}
	
	public static boolean isAndroidM()
	{
		return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
	}
	
	public static boolean isAndroidN()
	{
		return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N);
	}
	
	public static boolean hasPermission(Context c, String p)
	{
		//ExceptionHandler.log(p, "permission:"+ (c.checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED));
		return c.checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED;
	}
	
	public static boolean hasAllPermissions(Context c, String...p)
	{
		for (String s:p)
		{
			if (!hasPermission(c, s))
				return false;
		}
		return true;
	}
	
	public static void requestPermission(Activity a, int code, String...permissions)
	{
		a.requestPermissions(permissions, code);
	}
	
	public static boolean hasAllPermissionsGranted(int[] grantResults)
	{
		for (int grantResult : grantResults)
		{
			if (grantResult == PackageManager.PERMISSION_DENIED)
				return false;
		}
		return true;
	}
	
	public static void addClipboard(Context c, String lebel, String msg)
	{
		ClipboardManager cbm = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
		cbm.setPrimaryClip(ClipData.newPlainText(lebel, msg));
	}
	
	public static void addClipboard(Context c, String msg)
	{
		ClipboardManager cbm = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
		cbm.setText(msg);
	}
	
	public static String ReadFileToString(InputStream is)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int i = -1;
			while((i=is.read())!=-1)
			{
				baos.write(i);
			}
			baos.close();
			return baos.toString();
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	//获取设备序列号
	public static String getSerialNo(Context context)
	{
		StringBuilder deviceId = new StringBuilder();
		try
		{
			TelephonyManager tm	= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String sn =	tm.getSimSerialNumber();
			if(!isNullOrEmpty(sn))
				deviceId.append(sn);
			else
				deviceId.append(getDeviceModel());
			return deviceId.toString();
		}
		catch (Exception e)
		{
			deviceId.append(getDeviceModel());
			return deviceId.toString();
		}
	}
	
	//获取设备标识
	public static String getDeviceId(Context context)
	{
		StringBuilder deviceId = new StringBuilder();
		try
		{
			deviceId.append("version:");
			deviceId.append(context.getResources().getString(R.string.update));
			deviceId.append(",model:");
			deviceId.append(getDeviceModel());
			//IMEI（imei）
			TelephonyManager tm	= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String imei	= tm.getDeviceId();
			if(!isNullOrEmpty(imei))
			{
				deviceId.append(",imei:");
				deviceId.append(imei);
				return deviceId.toString();
			}
		}
		catch (Exception e)
		{
			return deviceId.toString();
		}
		return deviceId.toString();
	}
	
	//获取文件扩展名
	public static String getExtension(String name)
	{
		String suffix = "";
		int idx = name.lastIndexOf(".");
		if(idx > 0)
			suffix = name.substring(idx);
		return suffix;
	}
	
	//获取文件扩展名(去掉.)
	public static String getExtensionName(String name)
	{
		String suffix = "";
		int idx = name.lastIndexOf(".");
		if(idx > 0)
			suffix = name.substring(idx + 1);
		return suffix;
	}
	
	//得到扩展名MIME类型
	public static String getMimeType(String fileName)
	{
		String mime = "*/*";
		String tmp = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtensionName(fileName));
		if (tmp != null)
			mime = tmp;
		return mime;
	}
	
	//显示敏感信息
	public static String getSecstr(String str, int start, int end)
	{
		String regex = "(\\S{"+start+"})(.*)(\\S{"+end+"})";
		Matcher m = Pattern.compile(regex).matcher(str);
		if (m.find())
		{
			String rep = m.group(2);
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<rep.length(); i++)
			{
				sb.append("*");
			}
			return str.replaceAll(rep, sb.toString());
		}
		return str;
	}
	
	//获取清单文件数据
	public static String getMetaValue(Context context, String metaKey)
	{
		String apiKey = "";
		if (context == null || isNullOrEmpty(metaKey))
			return apiKey;
        try
		{
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            if (ai != null)
			{
				if (ai.metaData != null)
					apiKey = ai.metaData.getString(metaKey);
            }
        }
		catch (Exception e)
		{
			ExceptionHandler.log("Util.getMetaValue()", e);
		}
        return apiKey;
	}
	
	public static int getStatusBarHeight(Activity p)
	{
		Rect rect = new Rect();
		p.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		return rect.top;
	}
}
