package com.android.smartmobile.cz.db;

import java.io.File;

import jsqlite.Callback;
import jsqlite.Exception;
import android.content.Context;
import android.util.Log;

import com.android.smartmobile.cz.finals.Constants;

/**
 * 
 * @ClassName: DatabaseHelper
 * @Description: spatialite管理类
 * @author zhoul
 * @date 2013-8-26 下午10:05:53
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 */
public class DatabaseHelper {

	private static final String TAG = "DatabaseHelper";

	// Spatialite空间数据库,可以执行空间操作
	public static jsqlite.Database spDatabase;

	public jsqlite.Database getSPDatabase() {

		try {
			Class.forName("jsqlite.JDBCDriver").newInstance();
			spDatabase = new jsqlite.Database();
			String dbFile = Constants.SDBPath;
			if (new File(dbFile).exists() == false) {
				Log.i(TAG, "未找到数据库文件:" + dbFile);
				return null;
			}
			spDatabase.open(dbFile, jsqlite.Constants.SQLITE_OPEN_READWRITE);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return spDatabase;
	}

	public void closeDatabase() {

		try {
			spDatabase.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
