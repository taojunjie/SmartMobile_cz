package com.android.smartmobile.cz.listener;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.Gravity;

import com.android.smartmobile.cz.bean.AddressBean;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.MapHelper;
import com.android.smartmobile.cz.view.pop.PopGestureManager;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;

public class LooSingleTapListener implements OnSingleTapListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MapView mapView;
	private MapHelper mapHelper;
	private Context mContext;
	private GraphicsLayer keyGraphicsLayer, singleHighlightGraphicsLayer;

	public LooSingleTapListener(MapView mapView, Context context,
			GraphicsLayer keyGraphicsLayer,
			GraphicsLayer singleHighlightGraphicsLayer) {
		// TODO Auto-generated constructor stub
		this.mapView = mapView;
		this.mContext = context;
		this.keyGraphicsLayer = keyGraphicsLayer;
		this.singleHighlightGraphicsLayer = singleHighlightGraphicsLayer;
		mapHelper = new MapHelper(mapView, context);

	}

	@Override
	public void onSingleTap(float x, float y) {
		// TODO Auto-generated method stub
		mapView.getCallout().hide();

		if (!mapView.isLoaded()) {
			return;
		}
		if (null == keyGraphicsLayer) {
			return;
		}
		Point screenPoint = mapView.toMapPoint(x, y);
		SpatialReference sp=mapView.getSpatialReference();
		LogUtil.LooLog().d("x----" + screenPoint.getX() + "---y----" + screenPoint.getY()+"---SpatialReference----"+mapView.getSpatialReference().getID());
		// 获取点击位置的要素
		int[] gids = keyGraphicsLayer.getGraphicIDs(x, y, 5, 1);
		if (gids.length <= 0) {
			return;
		}
		// 获取到的点击对象
		Graphic gra = keyGraphicsLayer.getGraphic(gids[0]);

		// AddressBean addressBean = (AddressBean)
		// gra.getAttributeValue("TEST");
		// Point point=(Point) gra.getAttributeValue("CENT");
		// mapHelper.showCalloutView(mapView, mContext, addressBean, new
		// Point(StringUtil.String2Double(pointStringArray[0]),StringUtil.String2Double(pointStringArray[1])));

		String id = gra.getAttributeValue("ID").toString();
		String name = gra.getAttributeValue("NAME").toString();
		String tableName = gra.getAttributeValue("TABLENAME").toString();
		String centroid = gra.getAttributeValue("CENTROID").toString();
		String layerName = gra.getAttributeValue("LAYERNAME").toString();
		String symbol = gra.getAttributeValue("SYMBOL").toString();

		AddressBean addressBean = new AddressBean();
		addressBean.setId(id);
		addressBean.setName(name);
		addressBean.setTableName(tableName);
		addressBean.setCentroid(centroid);
		addressBean.setLayerName(layerName);
		addressBean.setSymbol(symbol);
		PopGestureManager popGestureManager = new PopGestureManager(mContext,
				mapView);

		popGestureManager.init(addressBean);
		int[] location = new int[2];

		Constants.ll_right_menu.getLocationOnScreen(location);

		popGestureManager.getPopupWindow().showAtLocation(
				Constants.ll_right_menu,
				Gravity.NO_GRAVITY,
				location[0] - popGestureManager.getPopupWindow().getWidth()
						+ 70, location[1]);
		ArrayList<Geometry> list = new ArrayList<Geometry>();
		list.add(gra.getGeometry());

		mapHelper.flashGeometrys(list, singleHighlightGraphicsLayer, true);
	}

}
