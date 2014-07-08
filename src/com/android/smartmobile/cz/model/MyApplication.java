/**
 * 
 */
package com.android.smartmobile.cz.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.android.smartmobile.cz.bean.AppConfigInfo;
import com.android.smartmobile.cz.bean.CatalogBean;
import com.android.smartmobile.cz.bean.GPSLocation;
import com.android.smartmobile.cz.bean.LegendBean;
import com.android.smartmobile.cz.bean.SubLayer;
import com.android.smartmobile.cz.db.DBHelper;
import com.android.smartmobile.cz.db.SDBHelper;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.map.GPSParam;
import com.android.smartmobile.cz.service.InitConfigAsyncTask;
import com.android.smartmobile.cz.service.PullParseConfigMapService;
import com.android.smartmobile.cz.service.PullParseConfigService;
import com.android.smartmobile.cz.service.PullParseGHFieldService;
import com.android.smartmobile.cz.util.LogUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.esri.android.map.GraphicsLayer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.PopupWindow;

/**
 * 
 * @ClassName: MyApplication
 * @Description:
 * @author Administrator
 * @date 2013年7月31日 下午1:28:29
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class MyApplication extends Application {
	/** 存放所有开启的activity 退出时使用 */
	public List<Activity> mActivities;
	public Context mContext;
	/** Application对象实例 */
	public static MyApplication mApp;
	/** 百度定位 */
	/** 所有PopupWindow集合，方便清除 */
	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	public GPSLocation GPSParamInfo;
	public BDLocation bdLocation;
	public String mData;
	public GraphicsLayer rimGraphicsLayer;
	public GraphicsLayer keyGraphicsLayer;
	public GraphicsLayer singleHighlightGraphicsLayer;
	public AppConfigInfo appConfigInfo;
	public List<SubLayer> subLayers;
	public List<CatalogBean> catalogs = null;
	public List<LegendBean> legendBeans;
	public Hashtable<String, List<LegendBean>> lHashtable;
	public SDBHelper sdbHelper;
	public DBHelper dbHelper;
	public Hashtable<String, Hashtable<String, Integer>> colortable;

	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mApp = this;

		mContext = this.getApplicationContext();

		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 数据初始化
	 * 
	 * @throws Exception
	 * 
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	void init() throws Exception {
		GPSParamInfo = new GPSLocation();
		mActivities = new ArrayList<Activity>();
		mLocationClient = new LocationClient(mContext);
		mLocationClient.registerLocationListener(myListener);
		GPSParam.setLocationOption(mLocationClient);
		mLocationClient.start();

		legendBeans = new ArrayList<LegendBean>();
		subLayers = new ArrayList<SubLayer>();
		sdbHelper = new SDBHelper();
		lHashtable = new Hashtable<String, List<LegendBean>>();
		rimGraphicsLayer = new GraphicsLayer();
		keyGraphicsLayer = new GraphicsLayer();
		singleHighlightGraphicsLayer = new GraphicsLayer();
		try {
			initConfig();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			LogUtil.LooLog().e("解析失败！" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.LooLog().e("解析失败！" + e.getMessage());
			e.printStackTrace();
		}
		// DB库初始化
		dbHelper = new DBHelper(mContext, Constants.DBPath);
	}

	@SuppressWarnings("unchecked")
	void initConfig() throws Exception {
		PullParseConfigService.getInstance();
		PullParseConfigMapService.getInstance();
		PullParseGHFieldService.getInstance();

		List<SubLayer> resultLayers = PullParseConfigMapService.getSubLayers();
		appConfigInfo = PullParseConfigMapService.getAppConfigInfo();
		catalogs = PullParseConfigMapService.getCatalogs();
		if (resultLayers == null || resultLayers.size() == 0)
			return;
		for (int i = 0; i < resultLayers.size(); i++) {
			if (resultLayers.get(i).getCanQuery().equalsIgnoreCase("YES")) {
				subLayers.add(resultLayers.get(i));
			}
		}
	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			bdLocation = location;
			if (location == null)
				return;
			GPSParamInfo.setCode(location.getLocType());
			GPSParamInfo.setLatitude(location.getLatitude());
			GPSParamInfo.setLontitude(location.getLongitude());
			GPSParamInfo.setTime(location.getTime());
			GPSParamInfo.setRadius(location.getRadius());
			GPSParamInfo.setProvince(location.getProvince());
			GPSParamInfo.setCity(location.getCity());
			GPSParamInfo.setDistrict(location.getDistrict());
			GPSParamInfo.setAddrStr(location.getAddrStr());
			GPSParamInfo.setSatellite(location.getSatelliteNumber());
			GPSParamInfo.setAltitude(location.getAltitude());
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

}
