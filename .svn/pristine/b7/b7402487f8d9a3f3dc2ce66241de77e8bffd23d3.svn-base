package com.android.smartmobile.cz.map;

import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.util.ArithUtil;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
/**
 * 
* @ClassName:   GPSParam
* @Description: GPS设置
* @author       frank 
* @date         2013-7-23 上午9:21:15
* @Company:     www.shdci.com 
* @Copyright:   Copyright (c) 2013  All rights reserved.
 */
public class GPSParam {

	public static void setLocationOption(LocationClient mLocClient) {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		// option.setCoorType(mCoorEdit.getText().toString()); //设置坐标类型
		option.setCoorType("gcj02");
		option.setServiceName("com.baidu.location.service_v3.3");
		// option.setPoiExtraInfo(mIsAddrInfoCheck.isChecked());
		option.setAddrType("all");
		option.setScanSpan(1000);
//		if (Constants.GpsScanType == 1) {
//			option.setScanSpan(Constants.GPSScanSpan);
//		} else if (Constants.GpsScanType == 2) {
//			option.setScanSpan(1000);
//		} else {
//			option.setScanSpan(30000);
//		}
		// 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
		// option.setPriority(LocationClientOption.GpsFirst); //不设置，默认是gps优先
		// option.setPoiNumber(10);
		option.disableCache(true);
		mLocClient.setLocOption(option);
	}

	public static double getDistance(Point startPoint, Point endPoint,
			SpatialReference mSpatial) {

		return GeometryEngine.distance(startPoint, endPoint, mSpatial);
	}

	public static boolean isContains(Polyline pline, SpatialReference mapSpatia) {
		boolean flag = false;
		double length = GeometryEngine.geodesicLength(pline, mapSpatia,
				(LinearUnit) LinearUnit.create(LinearUnit.Code.METER));
		// Unit mapUnit = map.getSpatialReference().getUnit();
		// lengths = Unit.convertUnits(lengths,
		// Unit.create(LinearUnit.Code.METER), mapUnit);
		// DecimalFormat df3 = new DecimalFormat("#.####"); // 保留2位小数，带前导零
		// double dlenth=lengths * 100000;
		double gpsseting = Double.valueOf(String
				.valueOf(Constants.GPSScanSpan));
		if (ArithUtil.sub(gpsseting, length) < 0) {
			flag = true;
		}
		return flag;
	}
}
