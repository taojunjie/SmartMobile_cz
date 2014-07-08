/**
 * 
 */
package com.android.smartmobile.cz.util;

import com.android.smartmobile.cz.model.MyApplication;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 
 * @ClassName: ToastUtil
 * @Description:
 * @author Administrator
 * @date 2013年8月1日 下午3:00:07
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class ToastUtil {

	public static void makeToastInCenter(String str) {
		Toast toast = Toast.makeText(MyApplication.mApp.mContext, str + "",
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void makeToastInBottom(String str) {
		Toast toast = Toast.makeText(MyApplication.mApp.mContext, str + "",
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
	}
}
