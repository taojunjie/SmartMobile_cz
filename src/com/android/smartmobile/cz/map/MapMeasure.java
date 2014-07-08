package com.android.smartmobile.cz.map;

import java.text.DecimalFormat;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.smartmobile.cz.R;
import com.esri.android.map.Callout;
import com.esri.android.map.MapView;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Unit;
/**
 * 
* @ClassName:   MapMeasure
* @Description: 测量计算
* @author       frank 
* @date         2013-7-23 上午9:23:11
* @Company:     www.shdci.com 
* @Copyright:   Copyright (c) 2013  All rights reserved.
 */
public class MapMeasure {
  
	public MapMeasure() { 
		 
	}
	
	static MapMeasure mMapMeasure;

	public static MapMeasure getInstance() {
		if (mMapMeasure == null) {
			synchronized (MapMeasure.class) {
				if (mMapMeasure == null) {
					mMapMeasure = new MapMeasure();
				}
			}
		}
		return mMapMeasure;
	}
  
	
	public void showMeasureArea(double Areas, Point endpoint,MapView mMainMapView) {
		String sArea = "";// getAreaString(tempPolygon.calculateArea2D());
		double radPerDegree = Math.PI / 180.0;
		double dlon = (Areas) * radPerDegree;
		double len_geo = Math.pow(Math.sin(dlon / 2), 2);
		len_geo = Math.abs(Math.min(1, len_geo));
		double c = 2 * Math.atan2(Math.sqrt(len_geo), Math.sqrt(1 - len_geo));
		double md = c * 6371008.77141506;

		float b = (float) (Math.round(md * 10000000)) / 100;
		DecimalFormat df = new DecimalFormat("#.00");
		if (b >= 1000000) {
			double dArea = b / 1000000.0;

			sArea = df.format(dArea) + " 平方千米";
		} else {
			sArea = df.format(b) + " 平方米";
		}

		// sArea=String.valueOf(b);
		Callout callout = mMainMapView.getCallout();
		callout.setStyle(R.xml.calloutstyle);
		Color color = new Color();
		LayoutInflater factory = LayoutInflater.from(mMainMapView.getContext());
		View v = factory.inflate(R.layout.measure_callout, null);
		TextView dd = (TextView) v.findViewById(R.id.measure_Title);
		dd.setText("面积:");
		dd.setTextColor(color.BLACK);
		dd.setTextSize(12);
		TextView context = (TextView) v.findViewById(R.id.measure_values);
		context.setText(sArea);
		context.setTextColor(color.BLACK);
		context.setTextSize(12);
		mMainMapView.getCallout().show(endpoint, v);
	}

	public void showMeasureLength(double lengths, Point endpoint,MapView mMainMapView) {
//		Unit mapUnit = mMainMapView.getSpatialReference().getUnit();  
//		 //GPSParam.isContains(lengths, mapUnit);
//		Unit ufrom=Unit.create(LinearUnit.Code.METER);
//		try{
//			 
//			 lengths = LinearUnit.convertUnits(lengths,mapUnit,ufrom);  
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
		 
		String length = "";
		DecimalFormat df3 = new DecimalFormat("#.#"); // 保留2位小数，带前导零 
		String[] str = String.valueOf(lengths).split("\\.");
		if (Integer.valueOf(str[0]) > 1000) {
			length = String.valueOf(df3.format(lengths / 1000)) + "千米";
		} else {
			length = String.valueOf(df3.format(lengths)) + "米";
		}

		Callout callout = mMainMapView.getCallout();
		callout.setStyle(R.xml.calloutstyle);
		Color color = new Color();
		LayoutInflater factory = LayoutInflater.from(mMainMapView.getContext());
		View v = factory.inflate(R.layout.measure_callout, null);
		TextView dd = (TextView) v.findViewById(R.id.measure_Title);
		dd.setText("长度:");
		dd.setTextColor(color.BLACK);
		dd.setTextSize(12);
		TextView context = (TextView) v.findViewById(R.id.measure_values);
		context.setText(length);
		context.setTextColor(color.BLACK);
		context.setTextSize(12);
		mMainMapView.getCallout().show(endpoint, v);
	}
}
