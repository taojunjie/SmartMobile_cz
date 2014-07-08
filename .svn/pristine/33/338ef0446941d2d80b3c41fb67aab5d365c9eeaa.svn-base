/**
 * 
 */
package com.android.smartmobile.cz.listener;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.android.smartmobile.cz.activity.MainActivity;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.ToastUtil;
import com.android.smartmobile.cz.view.pop.PopGestureManager;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;

/**
 * 
 * @ClassName: MapDefaultListener
 * @Description:
 * @author Administrator
 * @date 2013年8月5日 下午12:07:16
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class MapDefaultListener extends MapOnTouchListener {
	private LinearLayout ll_main_layermanager;
	private PopGestureManager popGestureManager;
	private MapView mapView;

	/**
	 * @param context
	 * @param view
	 */
	public MapDefaultListener(Context context, MapView view,
			LinearLayout ll_main_layermanager) {
		super(context, view);
		// TODO Auto-generated constructor stub
		this.ll_main_layermanager = ll_main_layermanager;
		this.mapView = view;
		popGestureManager = new PopGestureManager(context, view);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (ll_main_layermanager.getVisibility() == View.VISIBLE) {
			ll_main_layermanager.setVisibility(View.GONE);
		}
		return super.onTouch(v, event);
	}

	

	
}
