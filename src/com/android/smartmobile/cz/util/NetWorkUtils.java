/**
 * 
 */
package com.android.smartmobile.cz.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @ClassName:
 * @Description:
 * @author
 * @date 2013-7-8 上午11:04:15
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 */
/**
 * @author lixiaodaoaaa
 * 
 */
public class NetWorkUtils
{
	public static final int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
	public static final int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;

	public static boolean checkNetWork(Context context)
	{
		if (IsMoblieConnected(context) == false && IsWifiConnected(context) == false) { return false; }
		return true;
	}

	public static boolean getNetTypeIsWifi(Context context)
	{
		ConnectivityManager connectM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo actiNetInfo = connectM.getActiveNetworkInfo();
		if (actiNetInfo.getType() == TYPE_WIFI) { return true; }
		return false;
	}

	/**
	 * 判断当前手机的wifi是否连接了;;;;
	 * 
	 * @param context
	 * @return
	 */
	public static boolean IsWifiConnected(Context context)
	{
		ConnectivityManager connectM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = connectM.getNetworkInfo(TYPE_WIFI);
		if (wifiInfo != null && wifiInfo.isConnected()) { return true; }
		return false;
	}

	/**
	 * 判断当前手机的网络是不是手机net wap方式登录的;;;;;;;;;
	 * 
	 * @param context
	 * @return
	 */
	public static boolean IsMoblieConnected(Context context)
	{
		ConnectivityManager connectM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = null;
		if (null != connectM)
		{
			wifiInfo = connectM.getNetworkInfo(TYPE_MOBILE);
		}
		if (null != wifiInfo && wifiInfo.isConnected()) { return true; }
		return false;
	}

	/**
	 * 检查网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null)
		{
			return false;
		} else
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
			{
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED) { return true; }
				}
			}
		}
		return false;
	}
}
