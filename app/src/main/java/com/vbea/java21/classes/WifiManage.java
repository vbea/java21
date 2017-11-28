package com.vbea.java21.classes;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.vbea.java21.list.WifiItem;
import rx.exceptions.*;

public class WifiManage
{
	public List<WifiItem> Read() throws Exception
	{
		List<WifiItem> wifiInfos = new ArrayList<WifiItem>();

		Process process = null;
		DataOutputStream dataOutputStream = null;
		DataInputStream dataInputStream = null;
		StringBuffer wifiConf = new StringBuffer();
		try
		{
			process = Runtime.getRuntime().exec("su");
			dataOutputStream = new DataOutputStream(process.getOutputStream());
			dataInputStream = new DataInputStream(process.getInputStream());
			dataOutputStream.writeBytes("cat /data/misc/wifi/*.conf\n");
			dataOutputStream.writeBytes("exit\n");
			dataOutputStream.flush();
			InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null)
			{
				wifiConf.append(line);
			}
			bufferedReader.close();
			inputStreamReader.close();
			process.waitFor();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if (dataOutputStream != null)
					dataOutputStream.close();
				if (dataInputStream != null)
					dataInputStream.close();
				process.destroy();
			}
			catch (Exception e)
			{
				throw e;
			}
		}	

		Pattern network = Pattern.compile("network=\\{([^\\}]+)\\}", Pattern.DOTALL);
        Matcher networkMatcher = network.matcher(wifiConf.toString() );
        while (networkMatcher.find())
		{
            String networkBlock = networkMatcher.group();
            Pattern ssid = Pattern.compile("ssid=\"([^\"]+)\"");
            Matcher ssidMatcher = ssid.matcher(networkBlock);
            if (ssidMatcher.find())
			{   
            	WifiItem wifiInfo=new WifiItem();
                wifiInfo.SSID = ssidMatcher.group(1);
                Pattern psk = Pattern.compile("psk=\"([^\0]+)\"");
                Matcher pskMatcher = psk.matcher(networkBlock);
                if (pskMatcher.find())
                    wifiInfo.Password = pskMatcher.group(1);
				Pattern key = Pattern.compile("key_mgmt=([\\S]+)");
                Matcher keyMatcher = key.matcher(networkBlock);
				if (keyMatcher.find())
                    wifiInfo.Safety = keyMatcher.group(1);
				if (wifiInfo.Safety.indexOf(" ") > 0)
					wifiInfo.Safety = wifiInfo.Safety.split(" ")[0];
				Pattern pri = Pattern.compile("priority=([\\d]+)");
                Matcher priMatcher = pri.matcher(networkBlock);
				if (priMatcher.find())
                    wifiInfo.Priority = Integer.parseInt(priMatcher.group(1));
                wifiInfos.add(wifiInfo);
            }
        }
		return wifiInfos;
	}
}
