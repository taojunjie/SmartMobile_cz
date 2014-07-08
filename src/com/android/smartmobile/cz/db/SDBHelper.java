package com.android.smartmobile.cz.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.android.smartmobile.cz.bean.AddressBean;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.StringUtil;

import jsqlite.Callback;
import jsqlite.TableResult;

public class SDBHelper {

	public SDBHelper() {
		// TODO Auto-generated constructor stub
		mDatabaseHelper = new DatabaseHelper();
	}

	public DatabaseHelper mDatabaseHelper;

	/**
	 * 执行sql语句
	 * 
	 * @param string
	 * @throws jsqlite.Exception
	 */
	public List<AddressBean> getResultBySql(String sql, final String tableName,
			final String layerName) throws jsqlite.Exception {
		if (StringUtil.isEmpty(sql))
			return null;
		LogUtil.LooLog().d(sql);
		List<AddressBean> list = new ArrayList<AddressBean>();
		long startTime = System.currentTimeMillis();
		TableResult result = mDatabaseHelper.getSPDatabase().get_table(sql);
		long endTime = System.currentTimeMillis();
		float seconds = (endTime - startTime) / 1000F;
		LogUtil.LooLog().d("本次查询花费总时间：" + seconds + " s");

		if (null == result) {
			return null;
		}
		AddressBean addressBean = null;
		for (int j = 0; j < result.rows.size(); j++) {
			String[] columnStrings = (String[]) result.rows.get(j);
			addressBean = toAddressBean(result.column, columnStrings);
			if (addressBean == null)
				continue;
			addressBean.setTableName(tableName);
			addressBean.setLayerName(layerName);
			list.add(addressBean);
		}
		return list;
	}

	/**
	 * 生成 AddressBean
	 * 
	 * @param arrayColums
	 * @param columnStrings
	 * @return
	 */
	private AddressBean toAddressBean(String[] arrayColums,
			String[] columnStrings) {
		// TODO Auto-generated method stub
		AddressBean addressBean = new AddressBean();
		for (int i = 0; i < columnStrings.length; i++) {

			if (StringUtil.isEmpty(columnStrings[i])) {
				return null;
			}
			if (arrayColums[i].equalsIgnoreCase("ID")) {

				addressBean.setId(columnStrings[i]);
			} else if (arrayColums[i].equalsIgnoreCase("NAME")) {

				addressBean.setName(columnStrings[i]);
			} else if (arrayColums[i].equalsIgnoreCase("SUBNAME")) {

				addressBean.setSubname(columnStrings[i]);
			} else if (arrayColums[i].equalsIgnoreCase("GEOMETRY")) {

				addressBean.setGeometry(columnStrings[i]);
			} else if (arrayColums[i].equalsIgnoreCase("CENTROID")) {
				addressBean.setCentroid(columnStrings[i]);
			} else if (arrayColums[i].equalsIgnoreCase("AREA")) {

				addressBean.setArea(StringUtil.String2Double(columnStrings[i]));
			} else if (arrayColums[i].equalsIgnoreCase("SYMBOL")) {

				addressBean.setSymbol(columnStrings[i]);
			}
		}
		return addressBean;
	}

	/**
	 * 执行sql语句
	 * 
	 * @param string
	 * @throws jsqlite.Exception
	 */
	public List<Map<String, String>> getResultByName(String sql)
			throws jsqlite.Exception {
		if (StringUtil.isEmpty(sql))
			return null;
		LogUtil.LooLog().d(sql);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		TableResult result = mDatabaseHelper.getSPDatabase().get_table(sql);
		// mDatabaseHelper.getSPDatabase().e
		Map<String, String> map = null;
		if (null == result) {
			return null;
		}
		for (int j = 0; j < result.rows.size(); j++) {
			map = new HashMap<String, String>();
			String[] columnStrings = (String[]) result.rows.get(j);
			for (int i = 0; i < columnStrings.length; i++) {
				map.put(result.column[i], columnStrings[i]);
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 执行sql语句
	 * 
	 * @param string
	 * @throws jsqlite.Exception
	 */
	public List<String> getResultByCum(String cum) throws jsqlite.Exception {
		if (StringUtil.isEmpty(cum))
			return null;
		LogUtil.LooLog().d(cum);
		List<String> list = new ArrayList<String>();
		TableResult result = mDatabaseHelper.getSPDatabase().get_table(cum);
		if (null == result) {
			return null;
		}
		for (int j = 0; j < result.rows.size(); j++) {
			String[] columnStrings = (String[]) result.rows.get(j);
			for (int i = 0; i < columnStrings.length; i++) {
				if (!StringUtil.isEmpty(columnStrings[i])) {
					list.add(columnStrings[i]);
				}
			}
		}
		return list;
	}

	/**
	 * 执行sql语句
	 * 
	 * @param string
	 * @throws jsqlite.Exception
	 */
	public Map<String, Object> getResultByID(String sql)
			throws jsqlite.Exception {

		if (StringUtil.isEmpty(sql))
			return null;
		LogUtil.LooLog().d(sql);
		TableResult result = mDatabaseHelper.getSPDatabase().get_table(sql);
		Map<String, Object> map = null;
		if (null == result) {
			return null;
		}
		for (int j = 0; j < result.rows.size(); j++) {
			map = new HashMap<String, Object>();
			String[] columnStrings = (String[]) result.rows.get(j);
			for (int i = 0; i < columnStrings.length; i++) {
				map.put(result.column[i], columnStrings[i]);
			}
		}
		return map;
	}

	/**
	 * 执行sql语句
	 * 
	 * @param string
	 * @throws jsqlite.Exception
	 */
	public List<AddressBean> getRimResult(String sql) throws jsqlite.Exception {
		List<AddressBean> addressBeans = new ArrayList<AddressBean>();
		if (StringUtil.isEmpty(sql))
			return null;
		LogUtil.LooLog().d(sql);

		Callback cb = new Callback() {

			@Override
			public void types(String[] arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean newrow(String[] arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void columns(String[] arg0) {
				// TODO Auto-generated method stub
				String[] arg1 = arg0;
				LogUtil.LooLog().d(arg1.length);
			}
		};

		mDatabaseHelper.getSPDatabase().exec(sql, cb);

		return addressBeans;
	}

}
