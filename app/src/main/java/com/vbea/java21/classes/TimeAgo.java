package com.vbea.java21.classes;

import java.util.Date;
import java.text.SimpleDateFormat;

public class TimeAgo
{
	private final long minute = 60 * 1000;// 1分钟
	private final long hour = 60 * minute;// 1小时
	private final long day = 24 * hour;// 1天
	private final long month = 31 * day;// 月
	private final long year = 12 * month;// 年
	private SimpleDateFormat fom,fom2;
	public TimeAgo()
	{
		fom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public TimeAgo(SimpleDateFormat f)
	{
		this();
		fom2 = f;
	}
	
	public String getTimeAgo(String time, String def)
	{
		try
		{
			if (time != null)
				return getTimeFormatText(fom.parse(time), def);
			else
				return getTimeFormatText(null, "刚刚");
		}
		catch (Exception e)
		{
			return getTimeFormatText(null, def);
		}
	}
	public String getTimeAgo(String time)
	{
		if (fom2 == null)
			return time;
		try
		{
			if (time != null)
				return getTimeFormatText(fom.parse(time));
			else
				return getTimeFormatText(null, "刚刚");
		}
		catch (Exception e)
		{
		}
		return time;
	}
	
	private String getTimeFormatText(Date date)
	{
		return getTimeFormatText(date, fom2.format(date));
	}
    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public String getTimeFormatText(Date date, String def)
	{
        if (date == null)
            return def;
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year)
		{
			r = (diff / year);
			return r + "年前";
		}
        if (diff > month)
		{
            /*r = (diff / month);
            return r + "个月前";*/
			return def;
        }
        if (diff > day)
		{
			r = (diff / day);
			if (r < 4)
            	return r + "天前";
			else
				return def;
        }
        if (diff > hour)
		{
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute)
		{
			r = (diff / minute);
			return r + "分钟前";
		}
        return "刚刚";
	}
}
