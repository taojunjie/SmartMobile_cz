package com.android.smartmobile.cz.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.android.smartmobile.cz.bean.AddressBean;
import com.android.smartmobile.cz.bean.GeoType;
import com.android.smartmobile.cz.bean.SubLayer;
import com.android.smartmobile.cz.bean.TitleBean;
import com.android.smartmobile.cz.db.SDBHelper;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.MapHelper;
import com.android.smartmobile.cz.util.ToastUtil;
import com.android.smartmobile.cz.view.pop.PopRightTitleManager;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

public class LooLongPressListener implements OnLongPressListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GraphicsLayer rimGraphicsLayer;
	private MapView mapView;
	private MapHelper mapHelper;
	private Context mContext;
	private List<SubLayer> subLayers;
	private PopRightTitleManager popRightTitleManager;
	private LinearLayout ll_main_right_title;
	private Hashtable<String, Hashtable<String, Integer>> colortable;
	private List<Graphic> graphics = new ArrayList<Graphic>();

	public LooLongPressListener(MapView mapView, Context mContext,
			GraphicsLayer rimGraphicsLayer, LinearLayout ll_main_right_title) {
		// TODO Auto-generated constructor stub
		this.mapView = mapView;
		this.mContext = mContext;
		this.rimGraphicsLayer = rimGraphicsLayer;
		this.ll_main_right_title = ll_main_right_title;
		initData();
	}

	void initData() {
		mapHelper = new MapHelper(mapView, mContext);
		popRightTitleManager = new PopRightTitleManager(mContext, mapView,
				ll_main_right_title);
		subLayers = MyApplication.mApp.subLayers;
		colortable = MyApplication.mApp.colortable;
	}

	@Override
	public boolean onLongPress(float x, float y) {
		// TODO Auto-generated method stub
		Point screenPoint = mapView.toMapPoint(x, y);

		if (MyApplication.mApp.singleHighlightGraphicsLayer != null) {
			MyApplication.mApp.singleHighlightGraphicsLayer.removeAll();
		}
		// String sql =
		// "SELECT PK_UID ID,项目名称 NAME,用地性质 SUBNAME,ST_AsText(Geometry) GEOMETRY from PGIS_ydxkz t where where ST_Intersects(t.Geometry,ST_Buffer(ST_GeometryFromText('POINT(507456.215904 3513251.910686)',2437),100))=1";

		// mapHelper.DrawCircle(mapView, screenPoint, 100, 25,
		// Color.YELLOW);

		new getRimAsyncTask().execute(screenPoint);
		return false;

	}

	class getRimAsyncTask extends AsyncTask<Point, Void, List<TitleBean>> {
		ProgressDialog pdialog;

		@Override
		protected List<TitleBean> doInBackground(Point... params) {
			// TODO Auto-generated method stub
			// getRimData(params[0]);

			List<TitleBean> result = getRimData(params[0]);
			if (result == null || result.size() == 0)
				return null;
			// rimGraphicsLayer.removeAll();

			long startTime = System.currentTimeMillis();
			for (int ii = 0; ii < result.size(); ii++) {
				TitleBean titleBean = result.get(ii);

				if (titleBean.getGeoType().equals(GeoType.POINT.name())) {
					for (int i = 0; i < titleBean.getGroupLayers().size(); i++) {
						if (!mapHelper.addPointToGraphicsLayer(titleBean
								.getGroupLayers().get(i), colortable
								.get(titleBean.getName()), graphics)) {
							continue;
						}
					}
				} else if (titleBean.getGeoType().equals(
						GeoType.POLYLINE.name())) {
					for (int i = 0; i < titleBean.getGroupLayers().size(); i++) {
						if (!mapHelper.addPolylineToGraphicsLayer(titleBean
								.getGroupLayers().get(i), colortable
								.get(titleBean.getName()), graphics)) {
							continue;
						}
					}
				} else if (titleBean.getGeoType()
						.equals(GeoType.POLYGON.name())) {
					for (int i = 0; i < titleBean.getGroupLayers().size(); i++) {

						mapHelper.addPolygonToGraphicsLayer1(titleBean
								.getGroupLayers().get(i), colortable
								.get(titleBean.getName()), graphics);
					}
				}

			}
			LogUtil.LooLog().d("graphics个数---" + graphics.size());
			rimGraphicsLayer.addGraphics((Graphic[]) graphics
					.toArray(new Graphic[graphics.size()]));
			mapView.addLayer(rimGraphicsLayer);
			long endTime = System.currentTimeMillis();
			float seconds = (endTime - startTime) / 1000F;
			LogUtil.LooLog().d("绘制花费总时间：" + seconds + " s");
			// mapHelper.clearAllGraphicsLayer();

			return result;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(mContext);
			pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pdialog.setMessage("正在查询...");
			pdialog.show();
			graphics.clear();
			Constants.HANDLER_MAIN
					.sendEmptyMessage(Constants.WHAT_CLEAR_ALL_GRAPGICSLAYER);
		}

		@Override
		protected void onPostExecute(List<TitleBean> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null && result.size() != 0) {
				ll_main_right_title.setVisibility(View.VISIBLE);
				popRightTitleManager.init(result);
			} else {
				ToastUtil.makeToastInBottom("没有匹配的数据");
			}
			if (pdialog.isShowing()) {
				pdialog.dismiss();
				result = null;
			}

		}

	}

	private List<TitleBean> getRimData(Point point) {
		if (subLayers == null || subLayers.size() == 0)
			return null;
		List<TitleBean> titleBeans = new ArrayList<TitleBean>();
		TitleBean tlBean = null;

		for (int i = 0; i < subLayers.size(); i++) {
			if (i == 2)
				break;
			try {
				long startTime = System.currentTimeMillis();
				List<AddressBean> list = MyApplication.mApp.sdbHelper
						.getResultBySql(toSql(subLayers.get(i), point),
								subLayers.get(i).getTableName(),
								subLayers.get(i).getName());

				long endTime = System.currentTimeMillis();
				float seconds = (endTime - startTime) / 1000F;
				LogUtil.LooLog().d(
						subLayers.get(i).getName() + "查询总时间：" + seconds + " s");
				if (list.size() > 0) {
					tlBean = new TitleBean();
					tlBean.setName(subLayers.get(i).getName());
					tlBean.setGroupLayers(list);
					tlBean.setGeoType(subLayers.get(i).getGeoType());
					titleBeans.add(tlBean);
				}

			} catch (jsqlite.Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return titleBeans;
	}

	/**
	 * 组合sql语句
	 * 
	 * @param subLayer
	 * @return
	 */
	public String toSql(SubLayer subLayer, Point point) {

		// mapHelper.getPolygonGeometry(x, y, 100);

		return "SELECT PK_UID ID,"
				+ subLayer.getTitleField()
				+ " NAME,"
				+ subLayer.getSubTitleField()
				+ " SUBNAME,"
				+ subLayer.getSymbol()
				+ " SYMBOL, ST_AsText(Geometry) GEOMETRY ,ST_AsText(ST_Centroid(Geometry)) CENTROID FROM (SELECT * FROM "
				+ subLayer.getTableName()
				+ " t WHERE ST_Intersects(t.Geometry,"
				+ mapHelper.getPolygonGeometry(point.getX(), point.getY(),
						mapHelper.getScaleBean(mapView.getScale()).getBuffer())
				+ ")=1 AND ST_Intersects(t.Geometry,ST_Buffer(ST_GeometryFromText('POINT("
				+ point.getX() + " " + point.getY() + ")',2437),"
				+ mapHelper.getScaleBean(mapView.getScale()).getBuffer()
				+ "))=1)  ORDER BY NAME ";
	}

}
