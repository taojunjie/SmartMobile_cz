/**
 * 
 */
package com.android.smartmobile.cz.view.pop;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.adapter.ExAdapter;
import com.android.smartmobile.cz.bean.CatalogBean;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.interf.InitActivity;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.service.PullParseConfigMapService;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.MapHelper;
import com.esri.android.map.MapView;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout;

/**
 * 
 * @ClassName: PopLayer
 * @Description:
 * @author Administrator
 * @date 2013年8月3日 下午12:59:17
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class PopLayerManager implements InitActivity, OnClickListener {
	private Context mContext;
	private Button btn_pop_basemap_terrain, btn_pop_basemap_vector,
			btn_pop_basemap_img;
	private ImageView img_basemap_is_select_vector, img_basemap_is_select_img,
			img_basemap_is_select_terrain;
	private MapView mapView = null;
	// 声明PopupWindow对象的引用
	// private PopupWindow popupWindow = null;
	private List<CatalogBean> catalogs = null;
	private ExpandableListView expandableListView;
	private ExAdapter exAdapter;
	private LinearLayout ll_main_layermanager;
	private MapHelper mapHelper;

	/**
	 * 
	 * @param context
	 * @param mainMapView
	 */
	public PopLayerManager(Context context, MapView mainMapView,
			LinearLayout ll_main_layermanager) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mapView = mainMapView;
		this.ll_main_layermanager = ll_main_layermanager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.smartmobile.cz.interf.InitActivity#initData()
	 */
	@Override
	public void initData() {
		mapHelper = new MapHelper(mapView, mContext);
		catalogs=MyApplication.mApp.catalogs;
	}

	@Override
	public void initView() {
		initData();
		// TODO Auto-generated method stub
		// 获取自定义布局文件pop.xml的视图
		LinearLayout popupWindow_view = (LinearLayout) ll_main_layermanager
				.findViewById(R.id.main_layermanager_content);
		// 创建PopupWindow实例,200,150分别是宽度和高度

		// pop.xml视图里面的控件
		btn_pop_basemap_terrain = (Button) popupWindow_view
				.findViewById(R.id.btn_pop_basemap_terrain);
		btn_pop_basemap_vector = (Button) popupWindow_view
				.findViewById(R.id.btn_pop_basemap_vector);
		btn_pop_basemap_img = (Button) popupWindow_view
				.findViewById(R.id.btn_pop_basemap_img);

		img_basemap_is_select_vector = (ImageView) popupWindow_view
				.findViewById(R.id.img_basemap_is_select_vector);
		img_basemap_is_select_img = (ImageView) popupWindow_view
				.findViewById(R.id.img_basemap_is_select_img);
		img_basemap_is_select_terrain = (ImageView) popupWindow_view
				.findViewById(R.id.img_basemap_is_select_terrain);

		btn_pop_basemap_img.setOnClickListener(this);
		btn_pop_basemap_terrain.setOnClickListener(this);
		btn_pop_basemap_vector.setOnClickListener(this);

		expandableListView = (ExpandableListView) popupWindow_view
				.findViewById(R.id.ex_pop_map_type);
		expandableListView.setGroupIndicator(null);// 取消默认的ExpandableListview的默认指示的图标;;;
		exAdapter = new ExAdapter(mContext, catalogs, mapView);
		expandableListView.setAdapter(exAdapter);
		// 将所有项设置成默认展开

		expandableListView.setDivider(null);

		int groupCount = expandableListView.getCount();

		for (int i = 0; i < groupCount; i++) {

			expandableListView.expandGroup(i);

		}

		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub

				String name = catalogs.get(groupPosition).getName();
				// ToastUtil.makeToastInCenter(name);
				return false;
			}
		});
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub

				String name = catalogs.get(groupPosition).getGroupLayers()
						.get(childPosition).getAliasName();
				// ToastUtil.makeToastInCenter(name);

				final RelativeLayout ll_child_tool = (RelativeLayout) v
						.findViewById(R.id.ll_child_tool);
				if (ll_child_tool.getVisibility() == View.VISIBLE) {
					ll_child_tool.setVisibility(View.GONE);
				} else {
					ll_child_tool.setVisibility(View.VISIBLE);
				}

				return true;
			}
		});

		initMapTypeView(Constants.MapType);

	}

	void initMapTypeView(int index) {

		switch (index) {
		case 0:
			if (img_basemap_is_select_vector.getVisibility() == View.VISIBLE)
				return;
			img_basemap_is_select_vector.setVisibility(View.VISIBLE);
			img_basemap_is_select_img.setVisibility(View.INVISIBLE);
			img_basemap_is_select_terrain.setVisibility(View.INVISIBLE);
			// mainMapView.removeAll();
			mapHelper.AddBaseMapLayer(0);
			break;
		case 1:
			if (img_basemap_is_select_img.getVisibility() == View.VISIBLE)
				return;
			img_basemap_is_select_vector.setVisibility(View.INVISIBLE);
			img_basemap_is_select_img.setVisibility(View.VISIBLE);
			img_basemap_is_select_terrain.setVisibility(View.INVISIBLE);

			// com.esri.android.map.Layer layer1 = MapHelper.getLayerByName(
			// mainMapView, "基础矢量");
			// if (layer1 != null) {
			// mainMapView.removeLayer(layer1);
			// }

			// mainMapView.removeAll();

			// MapHelper.AddBaseMapLayer(mainMapView, 1);

			break;
		case 2:
			if (img_basemap_is_select_terrain.getVisibility() == View.VISIBLE)
				return;
			img_basemap_is_select_vector.setVisibility(View.INVISIBLE);
			img_basemap_is_select_img.setVisibility(View.INVISIBLE);
			img_basemap_is_select_terrain.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_pop_basemap_vector:

			Constants.MapType = 0;
			initMapTypeView(Constants.MapType);

			// try {
			// catalogs = PullParseConfigMapService.getCatalogs();
			// Constants.catalogs = catalogs;
			// exAdapter.notifyDataSetChanged();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			break;

		case R.id.btn_pop_basemap_img:

			Constants.MapType = 1;
			initMapTypeView(Constants.MapType);

			break;

		case R.id.btn_pop_basemap_terrain:

			Constants.MapType = 2;
			initMapTypeView(Constants.MapType);

			break;

		default:
			break;
		}
	}

}
