/**
 * 
 */
package com.android.smartmobile.cz.view.pop;

import java.util.List;

import jsqlite.Exception;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.adapter.ExRightTitleAdapter;
import com.android.smartmobile.cz.bean.AddressBean;
import com.android.smartmobile.cz.bean.GeoType;
import com.android.smartmobile.cz.bean.TitleBean;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.interf.InitActivity;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.MapHelper;
import com.esri.android.map.MapView;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout;

/**
 * 
 * @ClassName: PopListManager
 * @Description:
 * @author Administrator
 * @date 2013年8月3日 下午19:52:17
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class PopRightTitleManager implements InitActivity, OnTouchListener,
		android.view.GestureDetector.OnGestureListener {
	private Context mContext;
	private MapView mapView = null;
	// 声明PopupWindow对象的引用
	private PopupWindow popupWindow = null;
	private ExpandableListView expandableListView;
	private ExRightTitleAdapter exListTypeAdapter;
	private Handler mHandler = new Handler();
	private GestureDetector mGestureDetector;
	private static final int FLING_MIN_DISTANCE = 100;
	private static final int FLING_MIN_VELOCITY = 200;
	private List<TitleBean> titleBeans;
	private MapHelper mapHelper;
	private LinearLayout ll_main_right_title;
	private Button btn_right_title_close;

	public void setPopupWindow(PopupWindow popupWindow) {
		this.popupWindow = popupWindow;
	}

	/**
	 * 
	 * @param context
	 * @param mainMapView
	 */
	public PopRightTitleManager(Context context, MapView mainMapView,
			LinearLayout ll_main_right_title) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mapView = mainMapView;
		this.ll_main_right_title = ll_main_right_title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.smartmobile.cz.interf.InitActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mGestureDetector = new GestureDetector(this);
		mapHelper = new MapHelper(mapView, mContext);
	}

	/***
	 * 获取PopupWindow实例
	 */
	public PopupWindow getPopupWindow() {
		return popupWindow;
	}

	public void init(List<TitleBean> titleBeans) {
		LogUtil.LooLog().e(
				"titleBeans----------------------" + titleBeans.size());
		// TODO Auto-generated method stub
		this.titleBeans = titleBeans;
		initData();
		initView();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.smartmobile.cz.interf.InitActivity#clearView()
	 */
	@Override
	public void clearView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

		RelativeLayout popupWindow_view = (RelativeLayout) ll_main_right_title
				.findViewById(R.id.main_right_title_content);

		// pop.xml视图里面的控件

		expandableListView = (ExpandableListView) popupWindow_view
				.findViewById(R.id.ex_right_title);

		btn_right_title_close = (Button) popupWindow_view
				.findViewById(R.id.btn_right_title_close);
		btn_right_title_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ll_main_right_title.setVisibility(View.GONE);
			}
		});
		// expandableListView.setOnTouchListener(this);
		expandableListView.setGroupIndicator(null);
		expandableListView.setDivider(null);

		exListTypeAdapter = new ExRightTitleAdapter(mContext, titleBeans,
				mapView);
		expandableListView.setAdapter(exListTypeAdapter);
		int groupCount = expandableListView.getCount();

		for (int i = 0; i < groupCount; i++) {
			expandableListView.expandGroup(i);
		}
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				if (titleBeans.get(groupPosition).getGeoType()
						.equals(GeoType.POINT.name())) {
					mapHelper.flashPointToGraphicsLayer(
							titleBeans.get(groupPosition).getGroupLayers()
									.get(childPosition),
							MyApplication.mApp.singleHighlightGraphicsLayer,
							true);
				} else if (titleBeans.get(groupPosition).getGeoType()
						.equals(GeoType.POLYLINE.name())) {
					mapHelper.flashPolylineToGraphicsLayer(
							titleBeans.get(groupPosition).getGroupLayers()
									.get(childPosition),
							MyApplication.mApp.singleHighlightGraphicsLayer,
							true);
				} else if (titleBeans.get(groupPosition).getGeoType()
						.equals(GeoType.POLYGON.name())) {
					List<AddressBean> list = titleBeans.get(groupPosition)
							.getGroupLayers();
					mapHelper.flashPolygonToGraphicsLayer(
							list.get(childPosition),
							MyApplication.mApp.singleHighlightGraphicsLayer,
							true);
				}

				PopGestureManager popGestureManager = new PopGestureManager(
						mContext, mapView);

				popGestureManager.init(titleBeans.get(groupPosition)
						.getGroupLayers().get(childPosition));
				int[] location = new int[2];

				Constants.ll_right_menu.getLocationOnScreen(location);

				popGestureManager.getPopupWindow().showAtLocation(
						Constants.ll_right_menu,
						Gravity.NO_GRAVITY,
						location[0]
								- popGestureManager.getPopupWindow().getWidth()
								+ 70, location[1]);

				return false;
			}
		});

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			// 当像右侧滑动的时候
			ll_main_right_title.setVisibility(View.GONE);
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
