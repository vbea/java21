package com.vbea.java21.classes;

import java.io.File;
import java.security.MessageDigest;
import java.math.BigInteger;

import android.content.Context;
import android.content.pm.Signature;

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
		try
		{
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			
			return toHexString(messageDigest);
		}
		catch (Exception e)
		{
			return "vbes";
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
	
	/*public static String getSignature(byte[] sign)
	{
		try
		{
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(sign));
			return "public:" + cert.getPublicKey().toString() + "\nsign:" + cert.getSerialNumber().toString() + "\nName" + cert.getSigAlgName()
			+ "\nDN:"+ cert.getSubjectDN().toString() + "\n验证:" + (cert.getSubjectDN().equals(DEBUG_DN) ? "正版" : "盗版");
		}
		catch (Exception e)
		{
			return "";
		}
	}*/
}
