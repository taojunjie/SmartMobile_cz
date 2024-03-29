package com.android.smartmobile.cz.util;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.R.color;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

public class SymbolUtil {
	private Context mContext;
	private SimpleFillSymbol simpleFillSymbol;
	private SimpleLineSymbol simpleLineSymbol;
	private SimpleMarkerSymbol simpleMarkerSymbol;
	private PictureMarkerSymbol pictureMarkerSymbol;

	public SymbolUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}
	
	
	/**
	 * 关键字查询构面Symbol
	 * 
	 * @return
	 */
	public SimpleFillSymbol getPolygonAllSymbol1() {

		simpleFillSymbol = new SimpleFillSymbol(Color.GREEN);
		// simpleFillSymbol.setAlpha(100);
		simpleFillSymbol.setOutline(new SimpleLineSymbol(Color.BLACK,
				(float) 0.5));
		return simpleFillSymbol;
	}

	

	/**
	 * 关键字查询构面Symbol
	 * 
	 * @return
	 */
	public SimpleFillSymbol getPolygonAllSymbol(int symbol) {

		simpleFillSymbol = new SimpleFillSymbol(symbol);
		// simpleFillSymbol.setAlpha(100);
		simpleFillSymbol.setOutline(new SimpleLineSymbol(Color.BLACK,
				(float) 0.5));
		return simpleFillSymbol;
	}

	/**
	 * 关键字查询构线Symbol
	 * 
	 * @return
	 */
	public SimpleFillSymbol getPolylineAllSymbol(int symbol) {
		simpleLineSymbol = new SimpleLineSymbol(symbol, 5.0f);
		return simpleFillSymbol;
	}

	/***
	 * 长按查询之面质心点Symbol
	 * 
	 * @return
	 */
	public PictureMarkerSymbol getPolygonCenterPointSymbol() {
		Drawable one = mContext.getResources().getDrawable(
				R.drawable.location_point);
		pictureMarkerSymbol = new PictureMarkerSymbol(one);
		pictureMarkerSymbol.setOffsetY(20);
		return pictureMarkerSymbol;
	}
}
