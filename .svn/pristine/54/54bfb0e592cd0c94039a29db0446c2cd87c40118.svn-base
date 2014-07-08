package com.android.smartmobile.cz.util;

import com.android.smartmobile.cz.model.MyApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;

public class ActivityUtil {

	/** 检测网络 **/
	public static boolean isNetAvailable() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) MyApplication.mApp.mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/** 获取屏幕的宽度 */
	public static int getScreenWidth() {
		WindowManager manager = (WindowManager) MyApplication.mApp.mContext
				.getSystemService(Context.WINDOW_SERVICE);
		return manager.getDefaultDisplay().getWidth();
	}

	/** 获取屏幕的高度 */
	public static int getScreenHeight() {
		WindowManager manager = (WindowManager) MyApplication.mApp.mContext
				.getSystemService(Context.WINDOW_SERVICE);
		return manager.getDefaultDisplay().getHeight();
	}
}
