package com.vbea.java21.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.math.BigInteger;

import android.content.pm.Signature;
import android.graphics.Bitmap;

public class MD5Util
{
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F' };
	private final static String DEBUG_DN = "fdd40c3010ade9953b9fca216d553265";
	public static String toHexString(byte[] b)
	{  //String to  byte
		StringBuilder sb = new StringBuilder(b.length * 2); 
		for (int i = 0; i < b.length; i++)
		{
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);  
		}
		return sb.toString(); 
	}
	
	public static String getMD5(String s)
	{
		return getMD5(s.getBytes(), "vbes");
	}
	
	public static String getMD5(Bitmap bitmap, String def)
	{
		if (bitmap == null)
			return def;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try
		{
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out))
			{
				out.flush();
				out.close();
				return getMD5(out.toByteArray(), def).toLowerCase().substring(0, 16);
			}
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getMD5(bitmap)", e.toString());
		}
		return def;
	}
	
	public static String getMD5(byte[] s, String def)
	{
		try
		{
			if (s == null)
				return def;
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s);
			byte messageDigest[] = digest.digest();
			return toHexString(messageDigest);
		}
		catch (Exception e)
		{
			ExceptionHandler.log("getMD5(byte[])", e.toString());
			return def;
		}
	}
	
	public static boolean CheckKey(Signature[] signs)
	{
		return getKeyMD5(signs).equals(DEBUG_DN);
	}
	
	public static String getKeyMD5(Signature[] signatures)
	{
        try
		{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            if (signatures != null)
			{
                for (Signature s : signatures)
                    digest.update(s.toByteArray());
            }
            return toHexString(digest.digest()).toLowerCase();
        }
		catch (Exception e)
		{
            return "";
        }
    }

	//获取单个文件的MD5值！
	public static String getFileMD5(String defat,File file)
	{
		if (!file.isFile() || !file.canRead())
		{
			return defat;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try
		{
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1)
			{
				digest.update(buffer, 0, len);
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return defat;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	//获取单个文件的SHA1值！
	public static String getFileSHA1(String defat,File file)
	{
		if (!file.isFile() || !file.canRead())
		{
			return defat;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try
		{
			digest = MessageDigest.getInstance("SHA1");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1)
			{
				digest.update(buffer, 0, len);
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return defat;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}
}
