package com.android.smartmobile.cz.model;

import com.android.smartmobile.cz.util.ActivityUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
/**
 * 
 * @ClassName: BaseActivity
 * @Description:
 * @author Administrator
 * @date 2013年7月31日 下午1:26:56
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public abstract class BaseActivity extends Activity {

	
	
	
	
	/**
	 * 
	 */
	private void initView() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	private void initData() {
		// TODO Auto-generated method stub

	}

	/**
	 * 退出应用程序
	 */
	public void exitApp() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("消息");
		alertDialog.setMessage("确认退出!");
		alertDialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
		alertDialog.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						finishProcess();
					}
				});
		alertDialog.create().show();
	}
	


	private void finishProcess() {
		for (int i = 0; i < MyApplication.mApp.mActivities.size(); i++) {
			if (MyApplication.mApp.mActivities.get(i) != null) {
				MyApplication.mApp.mActivities.get(i).finish();
			}
		}
		System.exit(0);
	}

	/**
	 * 网络判断
	 * 
	 * @return
	 */
	public boolean checkNet() {
		return ActivityUtil.isNetAvailable();
	}
}