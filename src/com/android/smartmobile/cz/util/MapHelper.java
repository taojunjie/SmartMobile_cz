/**
 * 
 */
package com.android.smartmobile.cz.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.bean.AddressBean;
import com.android.smartmobile.cz.bean.LegendBean;
import com.android.smartmobile.cz.bean.MapItem;
import com.android.smartmobile.cz.bean.ScaleBean;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.map.OfflineCacheTiledServiceLayer;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.service.PullParseConfigMapService;
import com.android.smartmobile.cz.view.pop.PopGestureManager;
import com.esri.android.map.Callout;
import com.esri.android.map.CalloutStyle;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

/**
 * 
 * @ClassName: MapHelper
 * @Description: 地图辅助类
 * @author Administrator
 * @date 2013年8月5日 上午11:24:53
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class MapHelper {

	private final int FLASH_SHOW = 0x0001, FLASH_HIDE = 0x0002;
	private SimpleMarkerSymbol symbol_point = null;
	private SimpleFillSymbol symbol_polygon = null;
	private SimpleLineSymbol symbol_line = null;
	private boolean flashing = false;
	private Thread flashThread = null;
	private MapView mapView;
	private Context mContext;
	private SymbolUtil symbolUtil;
	private GraphicsLayer highGraphicsLayer;
	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case FLASH_HIDE:
				highGraphicsLayer.setVisible(false);
				break;
			case FLASH_SHOW:
				highGraphicsLayer.setVisible(true);
				break;
			}

		}
	};

	public void sethGraphicsLayer(GraphicsLayer hGraphicsLayer) {
		this.highGraphicsLayer = hGraphicsLayer;
	}

	/**
	 * 
	 */
	public MapHelper(MapView mapView, Context context) {
		// TODO Auto-generated constructor stub
		this.mapView = mapView;
		this.mContext = context;
		symbolUtil = new SymbolUtil(mContext);
		// 初始化Symbols
		symbol_point = new SimpleMarkerSymbol(Color.CYAN, 18,
				SimpleMarkerSymbol.STYLE.CIRCLE);
		symbol_line = new SimpleLineSymbol(Color.CYAN, 5.0f);

		symbol_polygon = new SimpleFillSymbol(Color.GREEN);
		symbol_polygon.setOutline(new SimpleLineSymbol(Color.YELLOW, 5.0f));
		// symbol_polygon.setAlpha(3);
	}

	// 离线切片
	private List<ArcGISLocalTiledLayer> mapLayer_local = null; // 切片图层
	// 在线切片
	private List<ArcGISTiledMapServiceLayer> mapLayer_online = null; // 切片图层

	/**
	 * 返回当前显示切片的层数
	 * 
	 * @return
	 */
	public int getCurrentLevel() {
		if (mapLayer_local != null && mapLayer_local.size() > 0) {
			return mapLayer_local.get(0).getCurrentLevel();
		}
		if (mapLayer_online != null && mapLayer_online.size() > 0) {
			return mapLayer_online.get(0).getCurrentLevel();
		}
		return 0;
	}

	public void AddTiledMapLayer(String layerName) {
		String file = Constants.TPKCachePath + "/" + layerName + ".tpk";
		if (new File(file).exists()) {
			AddLocalMapLayer(layerName);
			ToastUtil.makeToastInBottom("tpk模式：" + file);
		} else {
			try {
				com.android.smartmobile.cz.bean.LayerBean layer1 = PullParseConfigMapService
						.getLayerByName(layerName);

				layer1.setResolution(MyApplication.mApp.appConfigInfo
						.getResolution());
				layer1.setScale(MyApplication.mApp.appConfigInfo.getScale());
				com.esri.android.map.GroupLayer groupLayer = new com.esri.android.map.GroupLayer();
				groupLayer.setName(layerName);
				groupLayer.addLayer(new OfflineCacheTiledServiceLayer(
						Constants.MapCachePath, layer1));
				mapView.addLayer(groupLayer); // 添加地图服务到map中
				ToastUtil.makeToastInBottom("WMTS在线模式：" + layerName);
			} catch (XmlPullParserException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	/**
	 * 加载地图
	 * 
	 * @param mapView
	 */
	public void AddBaseMapLayer(int index) {
		try {
			MapItem item = PullParseConfigMapService.getMapsgement().get(index);
			if (!StringUtil.isEmpty(item.getType())
					&& item.getType().equalsIgnoreCase("WMS")) {
				AddDynamicMapServiceLayer(item.getUrl(), item.getName());
			} else if (!StringUtil.isEmpty(item.getType())
					&& item.getType().equalsIgnoreCase("WMTS")) {
				AddTiledMapLayer(item.getName());
			} else if (!StringUtil.isEmpty(item.getType())
					&& item.getType().equalsIgnoreCase("TPK")) {
				AddLocalMapLayer(item.getName());

			}

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void AddLocalMapLayer(String layerName) {

		// 加载离线缓存切片
		String path = "file:///" + Constants.TPKCachePath + "/" + layerName
				+ ".tpk";

		LogUtil.LooLog().d(path);
		ArcGISLocalTiledLayer layer1 = new ArcGISLocalTiledLayer(path);
		layer1.setName(layerName);
		mapView.addLayer(layer1); // 添加地图服务到map中

	}

	/**
	 * 添加属性服务图层
	 * 
	 * @param layerPath
	 * @param layerName
	 */
	public void AddFeatureLayer(String layerPath, String layerName) {

		ArcGISFeatureLayer featureLayer = new ArcGISFeatureLayer(layerPath,
				ArcGISFeatureLayer.MODE.ONDEMAND);
		featureLayer.setName(layerName);
		mapView.addLayer(featureLayer); // 添加地图服务到map中
	}

	/***
	 * 添加动态服务
	 * 
	 * @param layerPath
	 * @param layerName
	 */

	public void AddDynamicMapServiceLayer(String layerPath, String layerName) {

		ArcGISDynamicMapServiceLayer dynamicMapServiceLayer = new ArcGISDynamicMapServiceLayer(
				layerPath);

		dynamicMapServiceLayer.setName(layerName);
		mapView.addLayer(dynamicMapServiceLayer); // 添加地图服务到map中
	}

	/**
	 * 通过 Name 找到图层
	 * 
	 * @param layerName
	 * @return
	 */
	public Layer getLayerByName(String layerName) {
		Layer[] layers = mapView.getLayers();
		for (Layer l : layers) {
			if (layerName.equals(l.getName())) {
				return l;
			}

			if (l instanceof ArcGISLocalTiledLayer) {
				if (layerName.equals(l.getName())) {
					return l;
				}
			}
		}
		return null;
	}

	/**
	 * 高亮显示
	 * 
	 * @param geometries
	 *            要闪烁的对象
	 * @param zoomToIt
	 *            是否要ZoomTo对象范围
	 */

	public void flashGeometrys(List<Geometry> geometries,
			GraphicsLayer hGraphicsLayer, boolean zoomToIt) {
		sethGraphicsLayer(hGraphicsLayer);
		// 先清除残留的闪烁要素
		clearFlashGeometrys();
		// 初始化高亮显示
		if (geometries == null || geometries.size() == 0)
			return;
		Graphic[] graphics = new Graphic[geometries.size()];
		for (int i = 0; i < geometries.size(); i++) {
			if (geometries.get(i).getType() == Geometry.Type.POINT) {
				// Point p = ((Point) g);
				// envelope.merge(p);
				graphics[i] = new Graphic(geometries.get(i), symbol_point);
			} else if (geometries.get(i).getType() == Geometry.Type.POLYLINE) {
				// Polyline p = ((Polyline) g);
				// envelope.merge(getPolyLineExtent(p));
				graphics[i] = new Graphic(geometries.get(i), symbol_line);
			} else if (geometries.get(i).getType() == Geometry.Type.POLYGON) {
				// Polygon p = ((Polygon) g);
				// envelope.merge(getPolygonExtent(p));
				graphics[i] = new Graphic(geometries.get(i), symbol_polygon);
			}

			if (zoomToIt) {
				setExtent(mapView, geometries.get(i));
			}
		}

		hGraphicsLayer.addGraphics(graphics);
		mapView.addLayer(hGraphicsLayer);
		// MyApplication.mApp.singleHighlightGraphicsLayer.addGraphics(graphics);
		myHandler.postDelayed(drawing, 1000);
	}

	int i = 0;
	Runnable drawing = new Runnable() {
		public void run() {
			i++;
			Message message = new Message();
			message.what = (i % 2 == 0 ? FLASH_SHOW : FLASH_HIDE);
			myHandler.sendMessage(message);
			myHandler.postDelayed(this, 1000);
		}
	};

	/**
	 * 设置显示范围
	 * 
	 * @param geometry
	 */
	public void setExtent(MapView mapView, Geometry geometry) {
		mapView.setExtent(geometry);
		mapView.setScale(13000.0);
	}

	/**
	 * 设置显示范围
	 * 
	 * @param envelope
	 */
	public void setExtent(MapView mapView, Envelope envelope) {
		mapView.setExtent(envelope);
		mapView.setScale(13000.0);
	}

	public Envelope getPolyLineExtent(Polyline line) {
		if (line == null)
			return null;
		double min_x = line.getPoint(0).getX();
		double min_y = line.getPoint(0).getY();
		double max_x = line.getPoint(0).getX();
		double max_y = line.getPoint(0).getY();
		int count = line.getPointCount();
		for (int i = 0; i < count; i++) {
			double tmp_x = line.getPoint(i).getX();
			double tmp_y = line.getPoint(i).getY();
			if (tmp_x > max_x)
				max_x = tmp_x;
			if (tmp_x < min_x)
				min_x = tmp_x;
			if (tmp_y > max_y)
				max_y = tmp_y;
			if (tmp_y < min_y)
				min_y = tmp_y;
		}
		return new Envelope(min_x, min_y, max_x, max_y);
	}

	public Envelope getPolygonExtent(Polygon gon) {
		if (gon == null)
			return null;
		double min_x = gon.getPoint(0).getX();
		double min_y = gon.getPoint(0).getY();
		double max_x = gon.getPoint(0).getX();
		double max_y = gon.getPoint(0).getY();
		int count = gon.getPointCount();
		for (int i = 0; i < count; i++) {
			double tmp_x = gon.getPoint(i).getX();
			double tmp_y = gon.getPoint(i).getY();
			if (tmp_x > max_x)
				max_x = tmp_x;
			if (tmp_x < min_x)
				min_x = tmp_x;
			if (tmp_y > max_y)
				max_y = tmp_y;
			if (tmp_y < min_y)
				min_y = tmp_y;
		}
		Envelope envelope = new Envelope(min_x, min_y, max_x, max_y);

		return envelope;
	}

	/**
	 * 清空绘制内容
	 */
	public void clearFlashGeometrys() {
		myHandler.removeCallbacks(drawing);
		MyApplication.mApp.singleHighlightGraphicsLayer.removeAll();
	}

	public void showCalloutView(final AddressBean addressBean) {

		String[] pointStringArray = addressBean.getCentroid()
				.replaceFirst("POINT", "").replaceAll("\\(", "")
				.replaceAll("\\)", "").split(" ");
		Point mapPoint = new Point(
				StringUtil.String2Double(pointStringArray[0]),
				StringUtil.String2Double(pointStringArray[1]));

		final Callout callout = mapView.getCallout();

		// callout.setOffset(0, 10);
		CalloutStyle style = new CalloutStyle();

		style.setAnchor(5);
		style.setBackgroundColor(Color.parseColor("#FFFFFF"));
		// style.setBackgroundAlpha(10);
		style.setCornerCurve(1);
		// style.setFrameColor(R.color.tra);
		callout.setStyle(style);
		View view1 = LayoutInflater.from(mContext).inflate(
				R.layout.layout_callout, null);
		callout.setContent(view1);

		// Button btn_callout_modify = (Button) view1
		// .findViewById(R.id.btn_callout_modify);
		OnClickListener onclick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.txt_callout:
				case R.id.btn_callout_go:

					callout.hide();
					PopGestureManager popGestureManager = new PopGestureManager(
							mContext, mapView);

					popGestureManager.init(addressBean);
					int[] location = new int[2];

					Constants.ll_right_menu.getLocationOnScreen(location);

					popGestureManager.getPopupWindow().showAtLocation(
							Constants.ll_right_menu,
							Gravity.NO_GRAVITY,
							location[0]
									- popGestureManager.getPopupWindow()
											.getWidth() + 70, location[1]);

					break;
				}
			}
		};

		TextView txt = (TextView) view1.findViewById(R.id.txt_callout);
		Button btn_callout_go = (Button) view1
				.findViewById(R.id.btn_callout_go);
		//btn_callout_go.setOnClickListener(onclick);
		//txt.setOnClickListener(onclick);
		txt.setText(addressBean.getName());
		if (!callout.isShowing()) {
			// mapView.setOnStatusChangedListener(onStatusChangedListener);
			mapView.zoomToScale(mapPoint, 20000);
			callout.setCoordinates(mapPoint);
			callout.show();
		} else {
			mapView.zoomToScale(mapPoint, 20000);
			callout.move(mapPoint);
		}

	}

	public void showCalloutMediaView(Point point) {

	}

	public Point getPoint(String paramString) {
		String[] arrayOfString = paramString.replaceFirst("POINT", "")
				.replaceAll("\\(", "").replaceAll("\\)", "").split(" ");
		return new Point(StringUtil.String2Double(arrayOfString[0]),
				StringUtil.String2Double(arrayOfString[1]));
	}

	public Envelope getCurrentExtend() {
		Envelope rExtent = new Envelope();
		mapView.getExtent().queryEnvelope(rExtent);
		return rExtent;
	}

	/***
	 * 得到比例尺相关参数
	 * 
	 * @param scale
	 * @return
	 */
	public ScaleBean getScaleBean(double scale) {
		ScaleBean scaleBean = new ScaleBean();
		if (scale > 0 && scale <= 500) {
			scaleBean.setCn("5 米");
			scaleBean.setBuffer(5);
			scaleBean.setLever(0);
			scaleBean.setArea(0.0);
		} else if (scale > 500 && scale <= 1000) {
			scaleBean.setCn("10 米");
			scaleBean.setBuffer(10);
			scaleBean.setLever(1);
			scaleBean.setArea(500.0);
		} else if (scale > 1000 && scale <= 2000) {
			scaleBean.setCn("20 米");
			scaleBean.setBuffer(20);
			scaleBean.setLever(2);
			scaleBean.setArea(1000.0);
		} else if (scale > 2000 && scale <= 5000) {
			scaleBean.setCn("50 米");
			scaleBean.setBuffer(50);
			scaleBean.setLever(3);
			scaleBean.setArea(1000.0);
		} else if (scale > 5000 && scale <= 10000) {
			scaleBean.setCn("100 米");
			scaleBean.setBuffer(100);
			scaleBean.setLever(4);
			scaleBean.setArea(10000.0);
		} else if (scale > 10000 && scale <= 20000) {
			scaleBean.setCn("200 米");
			scaleBean.setBuffer(200);
			scaleBean.setLever(5);
			scaleBean.setArea(50000.0);
		} else if (scale > 20000 && scale <= 50000) {
			scaleBean.setCn("500 米");
			scaleBean.setBuffer(200);
			scaleBean.setLever(6);
			scaleBean.setArea(100000.0);
		} else if (scale > 50000 && scale <= 100000) {
			scaleBean.setCn("1 公里");
			scaleBean.setBuffer(200);
			scaleBean.setLever(7);
			scaleBean.setArea(500000.0);
		} else if (scale > 100000 && scale <= 200000) {
			scaleBean.setCn("2 公里");
			scaleBean.setBuffer(200);
			scaleBean.setLever(8);
			scaleBean.setArea(1000000.0);
		} else if (scale > 200000 && scale <= 500000) {
			scaleBean.setCn("5 公里");
			scaleBean.setBuffer(200);
			scaleBean.setLever(9);
			scaleBean.setArea(2000000.0);
		} else if (scale > 500000 && scale <= 1000000) {
			scaleBean.setCn("10 公里");
			scaleBean.setBuffer(200);
			scaleBean.setLever(10);
			scaleBean.setArea(5000000.0);
		} else if (scale > 1000000 && scale <= 2000000) {
			scaleBean.setCn("20 公里");
			scaleBean.setBuffer(200);
			scaleBean.setLever(11);
			
		} else if (scale > 2000000 && scale <= 5000000) {
			scaleBean.setCn("50 公里");
			scaleBean.setBuffer(200);
			scaleBean.setLever(12);
			scaleBean.setArea(10000000.0);
		} else if (scale > 5000000 && scale <= 10000000) {
			scaleBean.setCn("100 公里");
			scaleBean.setBuffer(200);
			scaleBean.setLever(13);
			scaleBean.setArea(20000000.0);
		} else {
			scaleBean.setCn("200 公里");
			scaleBean.setBuffer(500);
			scaleBean.setLever(14);
			scaleBean.setArea(20000000.0);
		}
		scaleBean.setScale((int)scale);
		return scaleBean;

	}

	// ----------------------绘制圆形-----------------------
	private static GraphicsLayer drawCircleLayer = null;

	/**
	 * 绘制圆,配合 clearDrawCircleLayer()清除
	 * 
	 * @param center
	 * @param radius
	 * @param alpha
	 * @param FillColor
	 */
	public void DrawCircle(MapView mapView, Point center, double radius,
			int alpha, int FillColor) {
		if (drawCircleLayer == null) {
			drawCircleLayer = new GraphicsLayer();
			mapView.addLayer(drawCircleLayer);
		} else {
			drawCircleLayer.removeAll();
		}
		Polygon polygon = new Polygon();
		getCircle(center, radius, polygon);
		FillSymbol symbol = new SimpleFillSymbol(FillColor);
		symbol.setAlpha(alpha);

		Graphic g = new Graphic(polygon, symbol);
		drawCircleLayer.addGraphic(g);
	}

	/**
	 * 获取圆的图形对象
	 * 
	 * @param center
	 * @param radius
	 * @return
	 */
	public Polygon getCircle(Point center, double radius) {
		Polygon polygon = new Polygon();
		getCircle(center, radius, polygon);
		return polygon;
	}

	private void getCircle(Point center, double radius, Polygon circle) {
		circle.setEmpty();
		Point[] points = getPoints(center, radius);
		circle.startPath(points[0]);
		for (int i = 1; i < points.length; i++)
			circle.lineTo(points[i]);
	}

	private Point[] getPoints(Point center, double radius) {
		Point[] points = new Point[50];
		double sin;
		double cos;
		double x;
		double y;
		for (double i = 0; i < 50; i++) {
			sin = Math.sin(Math.PI * 2 * i / 50);
			cos = Math.cos(Math.PI * 2 * i / 50);
			x = center.getX() + radius * sin;
			y = center.getY() + radius * cos;
			points[(int) i] = new Point(x, y);
		}
		return points;
	}

	/**
	 * 通过中心点坐标和半径得到正方形的四个点
	 * 
	 * @param x
	 * @param y
	 * @param r
	 * @return
	 */
	public List<Point> getRectangleFromPR(double x, double y, double r) {
		List<Point> list = new ArrayList<Point>();
		list.add(new Point(x - r, y - r));// 左下角点
		list.add(new Point(x + r, y - r));// 右下角点
		list.add(new Point(x + r, y + r));// 右上角点
		list.add(new Point(x - r, y + r));// 左上角点
		list.add(new Point(x - r, y - r));// 左下角点(封闭点)
		return list;
	}

	public String getPolygonGeometry(double x, double y, double r) {
		List<Point> points = getRectangleFromPR(x, y, r);
		String result = null;
		if (points.size() > 0) {
			for (int i = 0; i < points.size(); i++) {
				Point p1 = points.get(i);
				if (i == 0) {
					result = String.valueOf(p1.getX()) + " "
							+ String.valueOf(p1.getY());
				} else {
					result = result + "," + String.valueOf(p1.getX()) + " "
							+ String.valueOf(p1.getY());
				}

			}
			result = "POLYGON((" + result + "))";
			result = "ST_GeometryFromText('" + result + "', 2437)";
		}
		return result;
	}

	/**
	 * 将面绘制到图层上
	 * 
	 * @param addressBean
	 * @param graphicsLayer
	 * @param isLocation
	 */
	public void addPolygonToGraphicsLayer(AddressBean addressBean,

	Hashtable<String, Integer> hashtable, List<Graphic> list) {
		if (StringUtil.isEmpty(addressBean.getGeometry()))
			return;
		// LogUtil.LooLog().e(multiplygonString);

		String[] multiplygon = addressBean.getGeometry()
				.replaceFirst("POLYGON", "").replaceAll("\\(\\(", "")
				.replaceAll("\\)\\)", "").split(",");
		Polygon polygon = new Polygon();
		for (int index = 0; index < multiplygon.length; index++) {
			String[] point = multiplygon[index].trim().split(" ");
			if (0 == index) {
				polygon.startPath(Double.parseDouble(point[0]),
						Double.parseDouble(point[1]));
			} else {
				// polygon.lineTo(Double.parseDouble(point[0]),
				// Double.parseDouble(point[1]));
				try {
					polygon.lineTo(Double.parseDouble(point[0]),
							Double.parseDouble(point[1]));

				} catch (NumberFormatException e) {
					// TODO: handle exception
					LogUtil.LooLog().d(
							"no start 异常---id:" + addressBean.getId()
									+ "----  x:" + point[0] + "----  y:"
									+ point[1]);
					return;
				}

			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID", addressBean.getId());
		map.put("NAME", addressBean.getName());
		map.put("TABLENAME", addressBean.getTableName());
		map.put("CENTROID", addressBean.getCentroid());
		map.put("LAYERNAME", addressBean.getLayerName());
		map.put("SYMBOL", addressBean.getSymbol());
		// 此处Symbol可能为空
		list.add(new Graphic(polygon, symbolUtil.getPolygonAllSymbol(hashtable
				.get(addressBean.getSymbol())), map));

	}

	/**
	 * 将面绘制到图层上
	 * 
	 * @param addressBean
	 * @param graphicsLayer
	 * @param isLocation
	 */
	public void addPolygonToGraphicsLayer1(AddressBean addressBean,

	Hashtable<String, Integer> hashtable, List<Graphic> list) {
		if (StringUtil.isEmpty(addressBean.getGeometry()))
			return;
		// LogUtil.LooLog().e(multiplygonString);
		long startTime = System.currentTimeMillis();
		String[] multiplygon = addressBean.getGeometry()
				.replaceFirst("MULTIPOLYGON", "").replaceAll("\\(\\(\\(", "")
				.replaceAll("\\)\\)\\)", "").split("\\)\\),\\ \\(\\(");
		long endTime = System.currentTimeMillis();
		float seconds = (endTime - startTime) / 1000F;
		// LogUtil.LooLog().d("本次解析总时间：" + seconds + " s");

		Polygon polygon = null;
		if (multiplygon.length > 1) {
			LogUtil.LooLog().d(
					"多面-----id:" + addressBean.getId() + "----  name:"
							+ addressBean.getName());
		}

		long startTime1 = System.currentTimeMillis();

		for (int i = 0; i < multiplygon.length; i++) {
			String[] pointStringArray = multiplygon[i].split(",");
			polygon = new Polygon();

			for (int index = 0; index < pointStringArray.length; index++) {
				String[] point = pointStringArray[index].trim().split(" ");
				if (0 == index) {
					polygon.startPath(Double.parseDouble(point[0]),
							Double.parseDouble(point[1]));
				} else {
					// polygon.lineTo(Double.parseDouble(point[0]),
					// Double.parseDouble(point[1]));
					try {
						polygon.lineTo(Double.parseDouble(point[0]),
								Double.parseDouble(point[1]));

					} catch (NumberFormatException e) {
						// TODO: handle exception
						LogUtil.LooLog().d(
								"no start 异常---id:" + addressBean.getId()
										+ "----  x:" + point[0] + "----  y:"
										+ point[1]);
						return;
					}

				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID", addressBean.getId());
			map.put("NAME", addressBean.getName());
			map.put("TABLENAME", addressBean.getTableName());
			map.put("CENTROID", addressBean.getCentroid());
			map.put("LAYERNAME", addressBean.getLayerName());
			map.put("SYMBOL", addressBean.getSymbol());
			// 此处Symbol可能为空
			if (StringUtil.isEmpty(addressBean.getSymbol()))
				continue;
			list.add(new Graphic(polygon,
					symbolUtil.getPolygonAllSymbol(hashtable.get(addressBean
							.getSymbol())), map));
		}
		long endTime1 = System.currentTimeMillis();
		float seconds1 = (endTime1 - startTime1) / 1000F;
		// LogUtil.LooLog().d("本次绘制总时间：" + seconds1 + " s");
		LogUtil.LooLog().d("新增对象id--" + addressBean.getId());
	}

	/**
	 * 将面绘制到图层上
	 * 
	 * @param addressBean
	 * @param graphicsLayer
	 * @param isLocation
	 */
	public boolean addPolygonToGraphicsLayer2(AddressBean addressBean,
			GraphicsLayer paramGraphicsLayer,

			Hashtable<String, Integer> hashtable) {
		if (StringUtil.isEmpty(addressBean.getGeometry()))
			return false;

		String[] multiplygon = addressBean.getGeometry()
				.replaceFirst("POLYGON", "").replaceAll("\\(\\(", "")
				.replaceAll("\\)\\)", "").split(",");
		Polygon polygon = new Polygon();

		for (int index = 0; index < multiplygon.length; index++) {
			String[] point = multiplygon[index].trim().split(" ");
			if (0 == index) {
				polygon.startPath(Double.parseDouble(point[0]),
						Double.parseDouble(point[1]));
			} else {
				// polygon.lineTo(Double.parseDouble(point[0]),
				// Double.parseDouble(point[1]));
				try {
					polygon.lineTo(Double.parseDouble(point[0]),
							Double.parseDouble(point[1]));

				} catch (NumberFormatException e) {
					// TODO: handle exception
					LogUtil.LooLog().d(
							"no start 异常---id:" + addressBean.getId()
									+ "----  x:" + point[0] + "----  y:"
									+ point[1]);
					return false;
				}

			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID", addressBean.getId());
		map.put("NAME", addressBean.getName());
		map.put("TABLENAME", addressBean.getTableName());
		map.put("CENTROID", addressBean.getCentroid());
		map.put("LAYERNAME", addressBean.getLayerName());
		map.put("SYMBOL", addressBean.getSymbol());
		// 此处Symbol可能为空

		Graphic localGraphic = new Graphic(polygon,
				this.symbolUtil.getPolygonAllSymbol(((Integer) hashtable
						.get(addressBean.getSymbol())).intValue()), map);
		paramGraphicsLayer.addGraphic(localGraphic);

		return true;
	}

	/**
	 * 将面绘制到图层上
	 * 
	 * @param addressBean
	 * @param graphicsLayer
	 * @param isLocation
	 */
	public boolean addMultiPolygonToGraphicsLayer(AddressBean addressBean,
			GraphicsLayer paramGraphicsLayer,

			Hashtable<String, Integer> hashtable) {
		if (StringUtil.isEmpty(addressBean.getGeometry()))
			return false;

		long startTime = System.currentTimeMillis();
		String[] multiplygon = addressBean.getGeometry()
				.replaceFirst("MULTIPOLYGON", "").replaceAll("\\(\\(\\(", "")
				.replaceAll("\\)\\)\\)", "").split("\\)\\),\\ \\(\\(");
		long endTime = System.currentTimeMillis();
		float seconds = (endTime - startTime) / 1000F;
		// LogUtil.LooLog().d("本次解析总时间：" + seconds + " s");

		Polygon polygon = null;
		if (multiplygon.length > 1) {
			LogUtil.LooLog().d(
					"多面-----id:" + addressBean.getId() + "----  name:"
							+ addressBean.getName());
		}

		long startTime1 = System.currentTimeMillis();

		for (int i = 0; i < multiplygon.length; i++) {
			String[] pointStringArray = multiplygon[i].split(",");
			polygon = new Polygon();

			for (int index = 0; index < pointStringArray.length; index++) {
				String[] point = pointStringArray[index].trim().split(" ");
				if (0 == index) {
					polygon.startPath(Double.parseDouble(point[0]),
							Double.parseDouble(point[1]));
				} else {
					// polygon.lineTo(Double.parseDouble(point[0]),
					// Double.parseDouble(point[1]));
					try {
						polygon.lineTo(Double.parseDouble(point[0]),
								Double.parseDouble(point[1]));

					} catch (NumberFormatException e) {
						// TODO: handle exception
						LogUtil.LooLog().d(
								"no start 异常---id:" + addressBean.getId()
										+ "----  x:" + point[0] + "----  y:"
										+ point[1]);
						return false;
					}

				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID", addressBean.getId());
			map.put("NAME", addressBean.getName());
			map.put("TABLENAME", addressBean.getTableName());
			map.put("CENTROID", addressBean.getCentroid());
			map.put("LAYERNAME", addressBean.getLayerName());
			map.put("SYMBOL", addressBean.getSymbol());
			// 此处Symbol可能为空

			Graphic localGraphic = new Graphic(polygon,
					symbolUtil.getPolygonAllSymbol1(), map);
			paramGraphicsLayer.addGraphic(localGraphic);
		}
		return true;
	}

	/**
	 * 将线绘制到图层上
	 * 
	 * @param addressBean
	 * @param graphicsLayer
	 * @param isLocation
	 */
	public boolean addPolylineToGraphicsLayer(AddressBean addressBean,
			Hashtable<String, Integer> hashtable, List<Graphic> list) {
		if (StringUtil.isEmpty(addressBean.getGeometry()))
			return false;
		Polyline polyline = new Polyline();
		String[] pointStringArray = addressBean.getGeometry()
				.replaceFirst("LINESTRING", "").replaceAll("\\(", "")
				.replaceAll("\\)", "").split(",\\ ");

		for (int index = 0; index < pointStringArray.length; index++) {
			String[] point = pointStringArray[index].trim().split(" ");
			if (0 == index) {
				try {
					polyline.startPath(Double.parseDouble(point[0]),
							Double.parseDouble(point[1]));
				} catch (NumberFormatException e) {
					// TODO: handle exception
					LogUtil.LooLog().d(
							"start 异常---id:" + addressBean.getId() + "----  x:"
									+ point[0] + "----  y:" + point[1]);
					return false;
				}
			} else {
				try {
					polyline.lineTo(Double.parseDouble(point[0]),
							Double.parseDouble(point[1]));
				} catch (NumberFormatException e) {
					// TODO: handle exception
					LogUtil.LooLog().d(
							"no start 异常---id:" + addressBean.getId()
									+ "----  x:" + point[0] + "----  y:"
									+ point[1]);
					return false;
				}
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID", addressBean.getId());
		map.put("NAME", addressBean.getName());
		map.put("TABLENAME", addressBean.getTableName());
		map.put("CENTROID", addressBean.getCentroid());
		map.put("LAYERNAME", addressBean.getLayerName());
		map.put("SYMBOL", addressBean.getSymbol());

		if (StringUtil.isEmpty(addressBean.getSymbol())) {
			list.add(new Graphic(polyline, symbolUtil
					.getPolylineAllSymbol(hashtable.get("1")), map));
		} else {
			list.add(new Graphic(polyline, symbolUtil
					.getPolylineAllSymbol(hashtable.get("2")), map));
		}

		return true;

	}

	/**
	 * 将点绘制到图层上
	 * 
	 * @param addressBean
	 * @param graphicsLayer
	 * @param isLocation
	 */
	public boolean addPointToGraphicsLayer(AddressBean addressBean,
			Hashtable<String, Integer> hashtable, List<Graphic> list) {

		if (StringUtil.isEmpty(addressBean.getCentroid()))
			return false;
		String[] pointStringArray = addressBean.getCentroid()
				.replaceFirst("POINT", "").replaceAll("\\(", "")
				.replaceAll("\\)", "").split(" ");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID", addressBean.getId());
		map.put("NAME", addressBean.getName());
		map.put("TABLENAME", addressBean.getTableName());
		map.put("CENTROID", addressBean.getCentroid());
		map.put("LAYERNAME", addressBean.getLayerName());
		map.put("SYMBOL", addressBean.getSymbol());
		if (StringUtil.isEmpty(addressBean.getSymbol()))
			return false;
		list.add(new Graphic(new Point(StringUtil
				.String2Double(pointStringArray[0]), StringUtil
				.String2Double(pointStringArray[1])), symbolUtil
				.getPolygonCenterPointSymbol()));
		return true;
	}

	/**
	 * 将面绘制到图层上并闪烁
	 * 
	 * @param addressBean
	 * @param graphicsLayer
	 * @param isLocation
	 */
	public void flashPolygonToGraphicsLayer(AddressBean addressBean,

	GraphicsLayer graphicsLayer, boolean isLocation) {

		if (StringUtil.isEmpty(addressBean.getGeometry()))
			return;
		String[] multiplygon = addressBean.getGeometry()
				.replaceFirst("MULTIPOLYGON", "").replaceAll("\\(\\(\\(", "")
				.replaceAll("\\)\\)\\)", "").split("\\)\\),\\ \\(\\(");
		Polygon polygon = null;
		List<Geometry> geometries = new ArrayList<Geometry>();

		for (int i = 0; i < multiplygon.length; i++) {
			String[] pointStringArray = multiplygon[i].trim().split(",");
			polygon = new Polygon();
			for (int index = 0; index < pointStringArray.length; index++) {
				String[] point = pointStringArray[index].trim().split(" ");
				if (0 == index) {
					polygon.startPath(Double.parseDouble(point[0]),
							Double.parseDouble(point[1]));
				} else {
					try {
						polygon.lineTo(Double.parseDouble(point[0]),
								Double.parseDouble(point[1]));
					} catch (NumberFormatException e) {
						// TODO: handle exception
						LogUtil.LooLog().d(
								"id:" + addressBean.getId() + "----  name:"
										+ addressBean.getName());
					}

				}

			}
			geometries.add(polygon);
		}

		flashGeometrys(geometries, graphicsLayer, isLocation);
	}

	/**
	 * 将线绘制到图层上并闪烁
	 * 
	 * @param addressBean
	 * @param graphicsLayer
	 * @param isLocation
	 */
	public void flashPolylineToGraphicsLayer(AddressBean addressBean,

	GraphicsLayer graphicsLayer, boolean isLocation) {

		if (StringUtil.isEmpty(addressBean.getGeometry()))
			return;
		Polyline polyline = new Polyline();
		String[] pointStringArray = addressBean.getGeometry()
				.replaceFirst("LINESTRING", "").replaceAll("\\(", "")
				.replaceAll("\\)", "").split(",\\ ");
		List<Geometry> geometries = new ArrayList<Geometry>();
		for (int index = 0; index < pointStringArray.length; index++) {
			String[] point = pointStringArray[index].trim().split(" ");
			if (0 == index) {
				try {
					polyline.startPath(Double.parseDouble(point[0]),
							Double.parseDouble(point[1]));
				} catch (NumberFormatException e) {
					// TODO: handle exception
					LogUtil.LooLog().d(
							"start 异常---id:" + addressBean.getId() + "----  x:"
									+ point[0] + "----  y:" + point[1]);
					return;
				}
			} else {
				try {
					polyline.lineTo(Double.parseDouble(point[0]),
							Double.parseDouble(point[1]));
				} catch (NumberFormatException e) {
					// TODO: handle exception
					LogUtil.LooLog().d(
							"no start 异常---id:" + addressBean.getId()
									+ "----  x:" + point[0] + "----  y:"
									+ point[1]);
					return;
				}
			}
		}
		geometries.add(polyline);
		flashGeometrys(geometries, graphicsLayer, isLocation);
	}

	/**
	 * 将点绘制到图层上并闪烁
	 * 
	 * @param addressBean
	 * @param graphicsLayer
	 * @param isLocation
	 */
	public void flashPointToGraphicsLayer(AddressBean addressBean,

	GraphicsLayer graphicsLayer, boolean isLocation) {

		if (StringUtil.isEmpty(addressBean.getCentroid()))
			return;
		String[] pointStringArray = addressBean.getCentroid()
				.replaceFirst("POINT", "").replaceAll("\\(", "")
				.replaceAll("\\)", "").split(" ");
		List<Geometry> geometries = new ArrayList<Geometry>();
		geometries.add(new Point(StringUtil.String2Double(pointStringArray[0]),
				StringUtil.String2Double(pointStringArray[1])));
		flashGeometrys(geometries, graphicsLayer, isLocation);

	}

	public void clearAllGraphicsLayer() {
		clearFlashGeometrys();
		Layer[] layers = mapView.getLayers();
		for (Layer layer : layers) {
			if (layer instanceof GraphicsLayer) {
				GraphicsLayer graphicsLayer = (GraphicsLayer) layer;
				graphicsLayer.removeAll();
			}
		}
	}

}
