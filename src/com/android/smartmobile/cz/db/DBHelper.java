package com.android.smartmobile.cz.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.smartmobile.cz.util.LogUtil;

public class DBHelper {
	private String _dbPath = null;
	private Context _context = null;
	private SQLiteDatabase _db = null;

	public DBHelper(Context context, String dbFilePath) {
		_context = context;
		_dbPath = dbFilePath;
	}

	/**
	 * 获取数据库实例 需"<uses-permission android:name="android.permission.
	 * WRITE_EXTERNAL_STORAGE"></uses-permission>"
	 * 
	 * @throws Exception
	 */
	private SQLiteDatabase GetDBinstant() throws Exception {
		File f = new File(_dbPath);
		if (!f.exists()) {
			throw new Exception("没有找到数据库文件:" + _dbPath);
		}
		return SQLiteDatabase.openOrCreateDatabase(f, null);
	}

	public void Open() {

		if (_db == null) {
			try {
				_db = GetDBinstant();
			} catch (Exception e) {
				Log.e("ERROR_DB", e.getMessage());
			}
		} else {
			try {
				_db.close();
				_db = null;
				_db = GetDBinstant();
			} catch (Exception e) {
				Log.e("ERROR_DB", e.getMessage());
			}
		}
	}

	public void Close() {
		if (_db != null) {
			_db.close();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		Close();
		super.finalize();
	}

	/**
	 * 执行SQL语句
	 */
	public boolean ExecSQL(String sqlString) {
		Open();
		try {
			if(_db==null)return false;
			_db.execSQL(sqlString);
			LogUtil.LooLog().d(sqlString);
			return true;
		} catch (Exception e) {
			Log.e("ERROR_DB", e.getMessage());
			return false;
		} finally {
			Close();
		}
	}

	/**
	 * 执行SQLs语句
	 */
	public boolean ExecSQL(String[] sqlStrings) {
		Open();
		try {
			for (String sql : sqlStrings) {
				android.util.Log.d("EXEC", sql);
				_db.execSQL(sql.replace(":", "").replace(",", ","));
			}
			return true;
		} catch (Exception e) {
			Log.e("ERROR_DB", e.getMessage());
			return false;
		} finally {
			Close();
		}
	}

	/**
	 * 执行SQLs语句
	 */
	public boolean ExecSQL(List<String> sqlStrings) {
		Open();
		try {
			for (String sql : sqlStrings) {
				_db.execSQL(sql);
			}
			return true;
		} catch (Exception e) {
			Log.e("ERROR_DB", e.getMessage());
			return false;
		} finally {
			Close();
		}
	}

	/**
	 * 返回游标;
	 */
	public Cursor GetCursor(String sqlString) {
		Open();
		return _db.rawQuery(sqlString, null);
	}

	Cursor cur;

	/**
	 * 获取List信息;
	 */
	public ArrayList<Map<String, String>> GetResult(String SQLString) {

		try {
			cur = GetCursor(SQLString);
			if (!cur.moveToFirst()) {
				cur.close();
				return null;
			}
			ArrayList<Map<String, String>> r = new ArrayList<Map<String, String>>();

			do {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < cur.getColumnCount(); i++) {
					map.put(cur.getColumnName(i), cur.getString(i));
				}
				r.add(map);
			} while (cur.moveToNext());
			return r;
		} catch (Exception e) {
			return null;
		} finally {
			if (!cur.isClosed())
				cur.close();
			Close();
		}
	}
	public ArrayList<Map<String, String>> GetResultJson(String SQLString) {

		try {
			cur = GetCursor(SQLString);
			if (!cur.moveToFirst()) {
				cur.close();
				return null;
			}
 
	   

			ArrayList<Map<String, String>> r = new ArrayList<Map<String, String>>();
            int i=0;  
			do {
				Map<String, String> map = new HashMap<String, String>();
//				for (int i = 0; i < cur.getColumnCount(); i++) {
//					map.put(cur.getColumnName(i), cur.getString(i));
//				}
				if(i==0){
					map.put("TID", cur.getString(cur.getColumnIndex("task_id")));
					map.put("XY", cur.getString(cur.getColumnIndex("geometry")));
					map.put("RDATE", cur.getString(cur.getColumnIndex("create_date")));
					map.put("UID", cur.getString(cur.getColumnIndex("fk_user_id")));
					r.add(map);
				}else{
					for(int j=0;j<r.size();j++){
						Map<String, String> mMap = r.get(j);
						for (Object key : mMap.keySet()) {
							if(key.equals("TID")){
								if(mMap.get(key).equals(cur.getString(cur.getColumnIndex("task_id")))){
									mMap.put("XY", mMap.get("XY")+";"+cur.getString(cur.getColumnIndex("geometry")));
								}else{
									map.put("TID", cur.getString(cur.getColumnIndex("task_id")));
									map.put("XY", cur.getString(cur.getColumnIndex("geometry")));
									map.put("RDATE", cur.getString(cur.getColumnIndex("create_date")));
									map.put("UID", cur.getString(cur.getColumnIndex("fk_user_id")));
									r.add(map);
								}
								break;
							}
							
						}
					}
					
				}
				i++;
			} while (cur.moveToNext());
			return r;
		} catch (Exception e) {
			return null;
		} finally {
			if (!cur.isClosed())
				cur.close();
			Close();
		}
	}
}
