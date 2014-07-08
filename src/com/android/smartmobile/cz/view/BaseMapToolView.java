/**
 * 
 */
package com.android.smartmobile.cz.view;

import java.io.File;
import java.util.List;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.bean.GPSLocation;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.interf.InitActivity;
import com.android.smartmobile.cz.listener.MapDefaultListener;
import com.android.smartmobile.cz.map.GPSParam;
import com.android.smartmobile.cz.map.MapMeasure;
import com.android.smartmobile.cz.model.BaseActivity;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.util.FileUtil;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.MapHelper;
import com.android.smartmobile.cz.util.ToastUtil;
import com.android.smartmobile.cz.util.UUIDUtil;
import com.android.smartmobile.cz.view.pop.PopLayerManager;
import com.android.smartmobile.cz.view.pop.PopLegendManager;
import com.android.smartmobile.cz.view.pop.PopMediaManager;
import com.android.smartmobile.cz.view.pop.PopPlotManager;
import com.baidu.location.LocationClient;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.map.Legend;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.smartmobile.sdk.graphics.DrawTool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 
 * @ClassName: RightToolBarView
 * @Description: 右侧工具类
 * @author Administrator
 * @date 2013年7月31日 下午5:27:33
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class BaseMapToolView extends BaseActivity implements InitActivity,
		OnClickListener {

	private MapView mapView;
	private MapHelper mapHelper;
	private Context mContext;
	private GraphicsLayer temp_graphicsLayer = null;
	private ImageView img_right_menu_search, img_right_menu_measure,
			img_right_menu_plotting, img_right_menu_location,
			img_right_menu_legend, img_right_menu_delete, img_right_menu_tool,
			ib_right_photo, img_right_menu_photo;
	private LocationClient mLocClient;
	private PictureMarkerSymbol locationSymbol;
	private LinearLayout ll_right_tools, ll_right_toolmenu,
			ll_main_layermanager, ll_main_legend, ll_main_media;
	private PopLegendManager popLegendManager;
	private PopPlotManager popPlotManager;
	private PopLayerManager popLayerManager;
	private PopMediaManager popMediaManager;
	private PopupWindow measure_window;// 测量窗口
	private View ll_main_menu_left;
	private String mediaName = "";
	private String imgPath = "";
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			switch (msg.what) {
			case 23:
				double areaValue = bundle.getDouble("Area2D");
				Point mpoint = (Point) bundle.getSerializable("mPoint");
				MapMeasure.getInstance().showMeasureArea(areaValue, mpoint,
						mapView);
				break;
			case 22:
				double lengthValue = bundle.getDouble("Length2D");
				Point linepoint = (Point) bundle.getSerializable("mPoint");
				MapMeasure.getInstance().showMeasureLength(lengthValue,
						linepoint, mapView);
				break;
			case Constants.GPSLocation:

				showBDLocation(MyApplication.mApp.GPSParamInfo);
				break;
			case Constants.REFRESH_PLOTTING:
				img_right_menu_plotting
						.setBackgroundResource(R.drawable.btn_right_menu_plotting);
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 
	 */

	public BaseMapToolView(MapView mapView, Context context,
			LinearLayout ll_right_toolmenu, LinearLayout ll_main_layermanager,
			LinearLayout ll_main_legend, LinearLayout ll_main_media) {
		// TODO Auto-generated constructor stub
		this.mapView = mapView;
		this.mContext = context;
		this.ll_right_toolmenu = ll_right_toolmenu;
		this.ll_main_layermanager = ll_main_layermanager;
		this.ll_main_legend = ll_main_legend;
		this.ll_main_media = ll_main_media;

	}

	public void Init() {
		initView();
		initData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.smartmobile.cz.interf.InitActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		Constants.HANDLER_TOOL = handler;
		mLocClient = MyApplication.mApp.mLocationClient;
		locationSymbol = new PictureMarkerSymbol(mContext.getResources()
				.getDrawable(R.drawable.marker_gpsvalid));

		temp_graphicsLayer = new GraphicsLayer();
		mapView.addLayer(temp_graphicsLayer);
		popLayerManager = new PopLayerManager(mContext, mapView,
				ll_main_layermanager);
		popLayerManager.initView();
		popPlotManager = new PopPlotManager(mContext, mapView,
				temp_graphicsLayer);
		popLegendManager = new PopLegendManager(mContext, mapView,
				ll_main_legend);
		popLegendManager.initView();

		popMediaManager = new PopMediaManager(mContext, mapView, ll_main_media);
		mapHelper = new MapHelper(mapView, mContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.smartmobile.cz.interf.InitActivity#initView()
	 */
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		ll_right_tools = (LinearLayout) ll_right_toolmenu
				.findViewById(R.id.ll_right_tools);
		ib_right_photo = (ImageView) ll_right_toolmenu
				.findViewById(R.id.ib_right_photo);
		img_right_menu_tool = (ImageView) ll_right_toolmenu
				.findViewById(R.id.img_right_menu_tool);
		img_right_menu_search = (ImageView) ll_right_toolmenu
				.findViewById(R.id.img_right_menu_search);
		img_right_menu_measure = (ImageView) ll_right_toolmenu
				.findViewById(R.id.img_right_menu_measure);
		img_right_menu_plotting = (ImageView) ll_right_toolmenu
				.findViewById(R.id.img_right_menu_plotting);
		img_right_menu_location = (ImageView) ll_right_toolmenu
				.findViewById(R.id.img_right_menu_location);
		img_right_menu_legend = (ImageView) ll_right_toolmenu
				.findViewById(R.id.img_right_menu_legend);
		img_right_menu_photo = (ImageView) ll_right_toolmenu
				.findViewById(R.id.img_right_menu_photo);
		img_right_menu_delete = (ImageView) ll_right_toolmenu
				.findViewById(R.id.img_right_menu_delete);
		ib_right_photo.setOnClickListener(this);
		img_right_menu_search.setOnClickListener(this);
		img_right_menu_measure.setOnClickListener(this);
		img_right_menu_plotting.setOnClickListener(this);
		img_right_menu_location.setOnClickListener(this);
		img_right_menu_legend.setOnClickListener(this);
		img_right_menu_delete.setOnClickListener(this);
		img_right_menu_tool.setOnClickListener(this);
		img_right_menu_photo.setOnClickListener(this);
		ll_main_menu_left = Constants.ll_left_menu;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */

	private int num_img_right_menu_legend = 1;
	private int num_img_right_menu_location = 1;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.ib_right_photo:
			clearPOP();
			// 这里是位置显示方式,在按钮的左下角

			ll_main_layermanager
					.setVisibility(ll_main_layermanager.getVisibility() != View.VISIBLE ? View.VISIBLE
							: View.GONE);
			break;
		case R.id.img_right_menu_tool:
			clearView();
			clearPOP();
			if ((ll_right_tools.getVisibility() == View.VISIBLE)) {
				ll_right_tools.setVisibility(View.GONE);
			} else {
				ll_right_tools.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.img_right_menu_search:
			clearView();
			break;

		case R.id.img_right_menu_measure:
			if (Constants.ll_main_list_left != null
					&& Constants.ll_main_list_left.getVisibility() == View.VISIBLE) {
				Constants.ll_main_list_left.setVisibility(View.GONE);
			}
			if (popPlotManager.getPopupWindow() != null
					&& popPlotManager.getPopupWindow().isShowing()) {
				popPlotManager.getPopupWindow().dismiss();
				popPlotManager.setPopupWindow(null);
				img_right_menu_plotting
						.setBackgroundResource(R.drawable.btn_right_menu_plotting);
			}

			clearView();
			if (null != measure_window) {
				measure_window.dismiss();
				measure_window = null;
				img_right_menu_measure
						.setBackgroundResource(R.drawable.btn_right_menu_measure);
				return;
			} else {
				img_right_menu_measure
						.setBackgroundResource(R.drawable.btn_right_menu_measure_p);
				measureWindowShow();

			}

			break;
		case R.id.img_right_menu_plotting:
			if (Constants.ll_main_list_left != null
					&& Constants.ll_main_list_left.getVisibility() == View.VISIBLE) {
				Constants.ll_main_list_left.setVisibility(View.GONE);
			}
			clearView();

			if (measure_window != null && measure_window.isShowing()) {
				measure_window.dismiss();
				measure_window = null;
				img_right_menu_measure
						.setBackgroundResource(R.drawable.btn_right_menu_measure);
			}

			if (null != popPlotManager.getPopupWindow()) {
				img_right_menu_plotting
						.setBackgroundResource(R.drawable.btn_right_menu_plotting);
				popPlotManager.getPopupWindow().dismiss();
				popPlotManager.setPopupWindow(null);

				return;
			} else {
				img_right_menu_plotting
						.setBackgroundResource(R.drawable.btn_right_menu_plotting_p);
				popPlotManager.initView();
				int[] location = new int[2];
				ll_main_menu_left.getLocationOnScreen(location);

				popPlotManager.getPopupWindow().showAtLocation(
						ll_main_menu_left,
						Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
						location[0] + Constants.MAIN_LEFT_MENU_WIDTH + 40,
						location[1] + 20);

				popPlotManager.getPopupWindow().update();
			}
			break;
		case R.id.img_right_menu_location:// 定位
			if (num_img_right_menu_location % 2 != 0) {
				img_right_menu_location
						.setBackgroundResource(R.drawable.btn_right_menu_location_p);

				if (MyApplication.mApp.bdLocation == null) {
					ToastUtil.makeToastInBottom("无法获取定位信息,请确保网络畅通或GPS开启");
				} else {
					showBDLocation(MyApplication.mApp.GPSParamInfo);
				}

			} else {
				img_right_menu_location
						.setBackgroundResource(R.drawable.btn_right_menu_location);
				// mLocClient.stop();
				temp_graphicsLayer.removeAll();
			}
			num_img_right_menu_location++;

			break;
		case R.id.img_right_menu_legend:
			if (Constants.ll_main_list_left != null
					&& Constants.ll_main_list_left.getVisibility() == View.VISIBLE) {
				Constants.ll_main_list_left.setVisibility(View.GONE);
			}

			if (MyApplication.mApp.legendBeans.size() == 0
					|| MyApplication.mApp.legendBeans == null) {
				ToastUtil.makeToastInBottom("请先选中图层");
			} else {
				// if (num_img_right_menu_legend % 2 != 0) {
				// img_right_menu_legend
				// .setBackgroundResource(R.drawable.btn_right_menu_legend_p);
				// } else {
				// img_right_menu_legend
				// .setBackgroundResource(R.drawable.btn_right_menu_legend);
				// }
				// num_img_right_menu_legend++;
				// clearView();

				clearPOP();
				// 这里是位置显示方式,在按钮的左下角

				ll_main_legend
						.setVisibility(ll_main_legend.getVisibility() != View.VISIBLE ? View.VISIBLE
								: View.GONE);

				if (ll_main_legend.getVisibility() == View.VISIBLE) {
					popLegendManager.initView();
				}
			}

			break;
		case R.id.img_right_menu_photo:
			clearPOP();
			ll_main_media.setVisibility(View.VISIBLE);
			popMediaManager.initView();

			break;
		case R.id.img_right_menu_delete:
			clearView();
			clearPOP();
			resetRightToolMenu();
			deleteAll();
			break;

		default:
			break;
		}
	}

	void deleteAll() {
		Layer[] layers = mapView.getLayers();
		for (Layer layer : layers) {
			if (layer instanceof GraphicsLayer) {
				GraphicsLayer graphicsLayer = (GraphicsLayer) layer;
				graphicsLayer.removeAll();
				// if (null == graphicsLayer.getName()) {
				// continue;
				// }
				// if (graphicsLayer.getName().equals("gps_drive")
				// || graphicsLayer.getName().equals("GPSLocation")) {
				// graphicsLayer.removeAll();
				// break;
				// }
			}
		}
		Constants.HANDLER_MAIN
				.sendEmptyMessage(Constants.WHAT_CLEAR_ALL_GRAPGICSLAYER);
		Constants.HANDLER_LEFT_LIST
				.sendEmptyMessage(Constants.WHAT_LEFT_LIST_CLEAR_ALL_DATA);

		mapView.setOnTouchListener(new MapDefaultListener(mContext, mapView,
				ll_main_layermanager));
		if (mapView.getCallout().isShowing())
			mapView.getCallout().hide();
	}

	// GPS显示定位结果
	public void showBDLocation(GPSLocation GPSParamInfo) {
		double locy = GPSParamInfo.getLatitude();// location.getLatitude();//纬度
		double locx = GPSParamInfo.getLontitude();// location.getLongitude();//经度
		// BigDecimal y1 = new BigDecimal(String.valueOf(locy));
		// BigDecimal y2 = new BigDecimal(String.valueOf(Constants.offset_y));
		// BigDecimal x1 = new BigDecimal(String.valueOf(locx));
		// BigDecimal x2 = new BigDecimal(String.valueOf(Constants.offset_x));
		//
		// locx = x1.add(x2).doubleValue();
		// locy = y1.add(y2).doubleValue();
		// Point wgspoint = new Point(506410.0673, 3517009.6170);
		LogUtil.LooLog().d("定位成功---x:" + locx + "----- y:" + locy);
		if (locx == 4.9E-324 || locy == 4.9E-324) {
			ToastUtil.makeToastInBottom("无法获取定位信息,请确保网络畅通或GPS开启");
			return;
		}

		Point wgspoint = new Point(locx, locy);
		final Point mapPoint = (Point) GeometryEngine.project(wgspoint,
				SpatialReference.create(4326), mapView.getSpatialReference());
		if (temp_graphicsLayer == null) {
			temp_graphicsLayer = new GraphicsLayer();
			temp_graphicsLayer.setName("GPSLocation");
		}
		temp_graphicsLayer.removeAll();
		temp_graphicsLayer.addGraphic(new Graphic(mapPoint, locationSymbol));

		mapView.addLayer(temp_graphicsLayer);
		LogUtil.LooLog().d("x:" + mapPoint.getX() + "y:" + mapPoint.getY());
		mapView.centerAt(mapPoint, true);
		mapView.setScale(2000000);

	}

	// 测量窗口弹出
	private void measureWindowShow() {
		if (popPlotManager.getPopupWindow() != null) {
			popPlotManager.getPopupWindow().dismiss();
			popPlotManager.setPopupWindow(null);
		}

		View measureView = LayoutInflater.from(mContext).inflate(
				R.layout.measure, null, true);
		ImageView measure_length = (ImageView) measureView
				.findViewById(R.id.img_measure_length);
		ImageView measure_area = (ImageView) measureView
				.findViewById(R.id.img_measure_area);

		// 测距
		measure_length.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (null != temp_graphicsLayer)
					temp_graphicsLayer.removeAll();
				DrawTool d = new DrawTool(mContext, mapView,
						temp_graphicsLayer, handler);
				d.activate(DrawTool.POLYLINE);
				measure_window.dismiss();

			}
		});

		// 测面积
		measure_area.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (null != temp_graphicsLayer)
					temp_graphicsLayer.removeAll();
				DrawTool d = new DrawTool(mContext, mapView,
						temp_graphicsLayer, handler);
				d.activate(DrawTool.POLYGON);
				measure_window.dismiss();

			}
		});

		measure_window = new PopupWindow(measureView, 200, 90);
		int[] location = new int[2];
		ll_main_menu_left.getLocationOnScreen(location);
		measure_window.showAtLocation(ll_main_menu_left, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, location[0]
				+ Constants.MAIN_LEFT_MENU_WIDTH, location[1]);
		measure_window.update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.smartmobile.cz.interf.InitActivity#clearView()
	 */
	@Override
	public void clearView() {
		// TODO Auto-generated method stub

		if (null != temp_graphicsLayer)
			temp_graphicsLayer.removeAll();

		if (mapView.getCallout().isShowing()) {
			mapView.getCallout().hide();
		}

		mapView.setOnTouchListener(new MapDefaultListener(mContext, mapView,
				ll_main_layermanager));
	}

	void resetRightToolMenu() {
		// 复原右边工具栏
		img_right_menu_measure
				.setBackgroundResource(R.drawable.btn_right_menu_measure);
		img_right_menu_legend
				.setBackgroundResource(R.drawable.btn_right_menu_legend);
		img_right_menu_plotting
				.setBackgroundResource(R.drawable.btn_right_menu_plotting);
		img_right_menu_location
				.setBackgroundResource(R.drawable.btn_right_menu_location);

	}

	void clearPOP() {
		if (measure_window != null && measure_window.isShowing()) {
			measure_window.dismiss();
			measure_window = null;
			img_right_menu_measure
					.setBackgroundResource(R.drawable.btn_right_menu_measure);
		}

		if (popPlotManager.getPopupWindow() != null
				&& popPlotManager.getPopupWindow().isShowing()) {
			popPlotManager.getPopupWindow().dismiss();
			popPlotManager.setPopupWindow(null);
			img_right_menu_plotting
					.setBackgroundResource(R.drawable.btn_right_menu_plotting);
		}

	}

}
