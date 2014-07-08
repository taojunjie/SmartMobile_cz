package com.android.smartmobile.cz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	
	public static Date getDateFromStr(String strTime)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			if (null == strTime || strTime.trim().length() < 1)
			{
				strTime = new Date().toLocaleString();
			}
			return format.parse(strTime);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String getDateFromData(Date date)
	{
		// SimpleDateFormat format = new
		// SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");//格式化成此格式的时候在外盒核查专用手机上将会出现莫名其妙的Bug；；
		// 出现的Bug情况为无法保存相册 。以此formatter的字符串将无法创建新的文件 以这样的文件名创建;;;;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		return format.format(date);
	}
	
	public static String getStringDateTimeFromDate(Date date)
	{
		// SimpleDateFormat format = new
		// SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");//格式化成此格式的时候在外盒核查专用手机上将会出现莫名其妙的Bug；；
		// 出现的Bug情况为无法保存相册 。以此formatter的字符串将无法创建新的文件 以这样的文件名创建;;;;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
}
