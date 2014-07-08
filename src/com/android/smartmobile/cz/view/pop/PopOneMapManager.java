/**
 * 
 */
package com.android.smartmobile.cz.view.pop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.adapter.ResultListAdapter;
import com.android.smartmobile.cz.bean.AddressBean;
import com.android.smartmobile.cz.bean.GeoType;
import com.android.smartmobile.cz.bean.LegendBean;
import com.android.smartmobile.cz.bean.ScaleBean;
import com.android.smartmobile.cz.bean.SubLayer;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.interf.InitActivity;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.MapHelper;
import com.android.smartmobile.cz.util.StringUtil;
import com.android.smartmobile.cz.util.ToastUtil;
import com.android.smartmobile.cz.view.pop.PopGestureManager;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnPanListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

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
public class PopOneMapManager implements InitActivity, OnClickListener {
	private Context mContext;
	private MapView mapView = null;
	private MapHelper mapHelper = null;
	private EditText edit_search;
	private Button btn_list_close, btn_list_search;
	private TextView txt_left_list_top;
	private ListView mListView;
	private String[] presidents;
	private ResultListAdapter resultListAdapter;
	private LinearLayout ll_main_list_left;
	private RelativeLayout main_list_left_content;
	private List<AddressBean> addressBeans;
	private List<SubLayer> subLayers = null;
	private PopGestureManager popGestureManager;
	private Spinner mSpinner;
	private SubLayer currentSubLayer;
	private GraphicsLayer keyGraphicsLayer;
	private String cum = "";
	private boolean isPan = false;
	private boolean isFilter = false;
	private List<AddressBean> loadAddress;
	private List<LegendBean> legendBeans;
	public Hashtable<String, List<LegendBean>> lHashtable;
	private Hashtable<String, Integer> hashtable;
	// 设置一个最大的数据条数，超过即不再加载
	private int MaxDateNum;
	private boolean isLoad = false;
	// 最后可见条目的索引
	private int lastVisibleIndex;
	private View moreView;
	private View left_list_lv_top;
	private static final int LOAD_ALL_DATA = 1;
	private List<Graphic> graphics = new ArrayList<Graphic>();
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_ALL_DATA:
				mListView.removeFooterView(moreView);
				resultListAdapter.notifyDataSetInvalidated();// 通知adapter数据有变化
				mListView.setSelection(resultListAdapter.getCount());
				ToastUtil.makeToastInBottom("数据全部加载完成，没有更多数据！");
				isLoad = true;
				break;
			case Constants.WHAT_LEFT_LIST_CLEAR_ALL_DATA:
				if (null != resultListAdapter) {
					loadAddress.clear();
					txt_left_list_top.setText("");
					resultListAdapter.notifyDataSetChanged();
					isFilter = false;
				}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 
	 * @param context
	 * @param mainMapView
	 */
	public PopOneMapManager(Context context, MapView mainMapView,
			LinearLayout ll_main_list_left, GraphicsLayer keyGraphicsLayer) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mapView = mainMapView;
		this.ll_main_list_left = ll_main_list_left;
		this.keyGraphicsLayer = keyGraphicsLayer;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.smartmobile.cz.interf.InitActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		popGestureManager = new PopGestureManager(mContext, mapView);
		mapHelper = new MapHelper(mapView, mContext);
		addressBeans = new ArrayList<AddressBean>();
		loadAddress = new ArrayList<AddressBean>();
		subLayers = MyApplication.mApp.subLayers;
		currentSubLayer = subLayers.get(0);
		presidents = new String[subLayers.size()];
		for (int i = 0; i < subLayers.size(); i++) {
			presidents[i] = subLayers.get(i).getName();
		}
		LogUtil.LooLog().d(subLayers.size());
		lHashtable = MyApplication.mApp.lHashtable;
		hashtable = MyApplication.mApp.colortable
				.get(currentSubLayer.getName());
		Constants.HANDLER_LEFT_LIST = mHandler;
	}

	class addGraphicsAsyncTask extends AsyncTask<List<AddressBean>, Void, Void> {

		ScaleBean bean;
		HashMap<String, Integer> hashMap;
		int[] ids;

		@Override
		protected Void doInBackground(List<AddressBean>... params) {
			// TODO Auto-generated method stub
			long startTime_hz = System.currentTimeMillis();
			if (currentSubLayer.getGeoType().equals(GeoType.POLYGON.name())) {
				// LogUtil.LooLog().d("当前比例尺下允许面积阀值---" + bean.getArea());
				LogUtil.LooLog().d("抽稀前总个数---" + params[0].size());
				Envelope localEnvelope = mapHelper.getCurrentExtend();

				// LogUtil.LooLog().d(
				// "Xmax:" + localEnvelope.getXMax() + "--Xmin:"
				// + localEnvelope.getXMin() + "--Ymax:"
				// + localEnvelope.getYMax() + "--Ymin:"
				// + localEnvelope.getYMin());

				if (params[0].size() > 0) {

					for (int i = 0; i < params[0].size(); i++) {

						Point localPoint = mapHelper.getPoint((params[0].get(i)
								.getCentroid()));
						// LogUtil.LooLog().d(
						// "当前数据面积值---" + params[0].get(i).getArea()
						// + "X---" + localPoint.getX()
						// + "---y---" + localPoint.getY());
						// 抽吸一：是否在当前范围+面积大于当前比例尺下的阀值
						if ((localPoint.getX() < localEnvelope.getXMax())
								&& (localPoint.getX() > localEnvelope.getXMin())
								&& (localPoint.getY() < localEnvelope.getYMax())
								&& (localPoint.getY() > localEnvelope.getYMin())) {
							// 如果在当前范围+面积大于当前比例尺下的阀值则

							if (hashMap != null) {
								if (!hashMap.containsKey(params[0].get(i)
										.getId())) {

									if (params[0].get(i).getGeometry()
											.contains("MULTIPOLYGON")) {
										mapHelper.addPolygonToGraphicsLayer1(
												params[0].get(i), hashtable,
												graphics);
									} else {
										mapHelper.addPolygonToGraphicsLayer(
												params[0].get(i), hashtable,
												graphics);
									}

								} else {
									LogUtil.LooLog().d(
											"重复对象id--"
													+ params[0].get(i).getId());
								}

							} else {
								if (params[0].get(i).getGeometry()
										.contains("MULTIPOLYGON")) {
									mapHelper.addPolygonToGraphicsLayer1(
											params[0].get(i), hashtable,
											graphics);
								} else {
									mapHelper.addPolygonToGraphicsLayer(
											params[0].get(i), hashtable,
											graphics);
								}

							}

						} else {
							// 不在范围的直接移除

							if (hashMap != null) {
								if (hashMap.containsKey(params[0].get(i)
										.getId())) {
									keyGraphicsLayer.removeGraphic(hashMap
											.get(params[0].get(i).getId()));
									LogUtil.LooLog().d(
											"移除对象id--"
													+ params[0].get(i).getId());
								}
							}

						}
					}

					/*
					 * if (ids != null && ids.length > 0) { for (int j = 0; j <
					 * ids.length; j++) { if (repeatList.contains(ids[j]))
					 * continue; keyGraphicsLayer.removeGraphic(ids[j]); } }
					 */

				} else {
					for (int i = 0; i < params[0].size(); i++) {
						if (params[0].get(i).getGeometry()
								.contains("MULTIPOLYGON")) {
							mapHelper.addPolygonToGraphicsLayer1(
									params[0].get(i), hashtable, graphics);
						} else {
							mapHelper.addPolygonToGraphicsLayer(
									params[0].get(i), hashtable, graphics);
						}

					}
				}

			}
			keyGraphicsLayer.addGraphics((Graphic[]) graphics
					.toArray(new Graphic[graphics.size()]));
			long endTime = System.currentTimeMillis();
			float seconds = (endTime - startTime_hz) / 1000F;

			LogUtil.LooLog().d("绘制话费总时间 " + seconds + " s");
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			bean = mapHelper.getScaleBean(mapView.getScale());
			ids = keyGraphicsLayer.getGraphicIDs();

			hashMap = new HashMap<String, Integer>();
			if (ids != null && ids.length > 0) {
				LogUtil.LooLog().d("keyGraphicsLayer中id总量----" + ids.length);
				for (int j = 0; j < ids.length; j++) {
					Graphic graphic = keyGraphicsLayer.getGraphic(ids[j]);
					if (graphic != null) {
						String id = (String) graphic.getAttributeValue("ID");
						if (!StringUtil.isEmpty(id)) {
							hashMap.put(id, ids[j]);
							// LogUtil.LooLog().d("已有id----" + id);
						}
					}
				}

				LogUtil.LooLog().d("hashMap中总量----" + hashMap.size());
			}

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			graphics.clear();
			hashMap.clear();
			ids = null;
			
		}

	}

	class GetDataAsyncTask extends AsyncTask<SubLayer, Void, List<AddressBean>> {
		ProgressDialog pdialog;

		@Override
		protected void onPostExecute(List<AddressBean> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pdialog.isShowing()) {
				pdialog.dismiss();
			}

			if (result != null && result.size() != 0) {
				txt_left_list_top.setText("查询到" + result.size() + "条结果");
				addressBeans = result;
				MaxDateNum = addressBeans.size();

				if (MaxDateNum < 15) {
					for (int i = 0; i < MaxDateNum; i++) {
						loadAddress.add(addressBeans.get(i));
					}
					ToastUtil.makeToastInBottom("数据全部加载完成，没有更多数据！");

				} else {
					for (int i = 0; i < 15; i++) {
						loadAddress.add(addressBeans.get(i));
					}

				}
				resultListAdapter.notifyDataSetChanged();

			} else {
				keyGraphicsLayer.removeAll();
				if (mapView.getCallout().isShowing()) {
					mapView.getCallout().hide();
				}

				txt_left_list_top.setText("查询到0条结果");
				ToastUtil.makeToastInBottom("没有匹配的数据");
			}

			result = null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(mContext);
			pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pdialog.setMessage("正在查询...");
			pdialog.show();
			loadAddress.clear();
			graphics.clear();
			Constants.HANDLER_MAIN
					.sendEmptyMessage(Constants.WHAT_CLEAR_ALL_GRAPGICSLAYER);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected List<AddressBean> doInBackground(SubLayer... params) {
			// TODO Auto-generated method stub

			List<AddressBean> list = getData(params[0]);

			if (list == null || list.size() == 0)
				return null;
			LogUtil.LooLog().d("查询到目标" + list.size() + "个");

			if (params[0].getGeoType().equals(GeoType.POINT.name())) {
				for (int i = 0; i < list.size(); i++) {
					mapHelper.addPointToGraphicsLayer(list.get(i), hashtable,
							graphics);
				}
			} else if (params[0].getGeoType().equals(GeoType.POLYLINE.name())) {
				for (int i = 0; i < list.size(); i++) {
					if (!mapHelper.addPolylineToGraphicsLayer(list.get(i),
							hashtable, graphics))
						continue;
				}
			} else if (params[0].getGeoType().equals(GeoType.POLYGON.name())) {

				if (mapView.getScale() >= 20000) {
					mHandler.sendEmptyMessage(Constants.WHAT_REFRESH_ITEM_CLICK);
					keyGraphicsLayer.removeAll();
				} else {
					new addGraphicsAsyncTask().execute(list);
				}
			}

			return list;
		}

	}

	/**
	 * 组合sql语句
	 * 
	 * @param subLayer
	 * @param cum
	 * @return
	 */
	public String toSql(SubLayer subLayer, String cum) {
		String sql = "SELECT PK_UID ID,"
				+ subLayer.getTitleField()
				+ " NAME,"
				+ subLayer.getSubTitleField()
				+ " SUBNAME,"
				+ subLayer.getSymbol()
				+ " SYMBOL, ST_AsText(Geometry) GEOMETRY ,ST_Area(Geometry) AREA,ST_AsText(ST_Centroid(Geometry)) CENTROID"
				+ " FROM ( SELECT * FROM " + subLayer.getTableName()
				+ " WHERE " + subLayer.getTitleField() + " LIKE '%"
				+ cum.replace(" ", "%") + "%' OR "
				+ subLayer.getSubTitleField() + " LIKE '%"
				+ cum.replace(" ", "%") + "%') ORDER BY ID";

		sql += " LIMIT " + limit;
		return sql;
	}

	/**
	 * 根据查询条件查询
	 * 
	 * @param condition
	 * @return
	 */
	List<AddressBean> getData(SubLayer s) {

		List<AddressBean> list = null;

		try {

			list = MyApplication.mApp.sdbHelper.getResultBySql(toSql(s, cum),
					s.getTableName(), s.getName());

			if (list == null || list.size() == 0)
				return null;

		} catch (jsqlite.Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			LogUtil.LooLog().e(e.getMessage());
			return null;
		}
		return list;

	}

	@Override
	public void initView() {

		initData();
		// 实例化底部布局
		moreView = LayoutInflater.from(mContext).inflate(R.layout.moredata,
				null);
		// ListView顶部部View
		left_list_lv_top = LayoutInflater.from(mContext).inflate(
				R.layout.left_list_lv_top, null);
		txt_left_list_top = (TextView) left_list_lv_top
				.findViewById(R.id.txt_left_list_top);

		main_list_left_content = (RelativeLayout) ll_main_list_left
				.findViewById(R.id.main_list_left_content);

		// pop.xml视图里面的控件
		mListView = (ListView) main_list_left_content
				.findViewById(R.id.lv_list);

		mListView.setDivider(null);
		mListView.setFastScrollEnabled(false);
		// mListView.addFooterView(moreView);
		mListView.addHeaderView(left_list_lv_top);
		resultListAdapter = new ResultListAdapter(mContext, loadAddress);
		mListView.setAdapter(resultListAdapter);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				// 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == resultListAdapter.getCount()) {
					// 当滑到底部时自动加载

					if (!isLoad) {
						mListView.addFooterView(moreView);
						resultListAdapter.notifyDataSetInvalidated();
						mListView.setSelection(resultListAdapter.getCount());
						// resultListAdapter.notifyDataSetChanged();
						mHandler.postDelayed(new Runnable() {

							@Override
							public void run() {
								loadMoreDate();
							}
						}, 2000);
					}

				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				// 计算最后可见条目的索引
				lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
				//
				// 所有的条目已经和最大条数相等，则移除底部的View
				// if (totalItemCount == MaxDateNum + 1) {
				// mListView.removeFooterView(moreView);
				// Toast.makeText(mContext, "数据全部加载完成，没有更多数据！",
				// Toast.LENGTH_LONG).show();
				// }
			}
		});

		// 列表按钮
		// btn_list_type = (Button) main_list_left_content
		// .findViewById(R.id.btn_list_type);
		// btn_list_type
		// .setText(catalogs.get(0).getGroupLayers().get(0).getName());

		mSpinner = (Spinner) main_list_left_content
				.findViewById(R.id.spinner_list);
		btn_list_search = (Button) main_list_left_content
				.findViewById(R.id.btn_list_search);

		// 关闭按钮
		btn_list_close = (Button) main_list_left_content
				.findViewById(R.id.btn_list_close);
		btn_list_search.setOnClickListener(this);
		edit_search = (EditText) main_list_left_content
				.findViewById(R.id.edit_list);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0)
					return;
				// ToastUtil.makeToastInCenter(arg2+"");

				Constants.HANDLER_MAIN
						.sendEmptyMessage(Constants.WHAT_REFRESH_ITEM_CLICK);

				if (loadAddress.get(position - 1).getGeometry()
						.contains("MULTIPOLYGON")) {
					mapHelper.addMultiPolygonToGraphicsLayer(
							loadAddress.get(position - 1), keyGraphicsLayer,
							hashtable);
				} else {
					mapHelper.addPolygonToGraphicsLayer2(
							loadAddress.get(position - 1), keyGraphicsLayer,
							hashtable);
				}

				mapHelper.showCalloutView((AddressBean) loadAddress
						.get(position - 1));
				// TODO Auto-generated method stub
				// mapHelper.showCalloutView(loadAddress.get(position - 1));
			}
		});

		btn_list_close.setOnClickListener(this);

		// edit_search.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		// // TODO Auto-generated method stub
		// if (StringUtil.isEmpty(s.toString()))
		// return;
		// cum = s.toString();
		// // 异步查询
		// new GetDataAsyncTask().execute(currentSubLayer);
		//
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, presidents);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (mapView.getCallout().isShowing())
					mapView.getCallout().hide();
				MyApplication.mApp.singleHighlightGraphicsLayer.removeAll();
				currentSubLayer = subLayers.get(arg2);
				legendBeans = lHashtable.get(currentSubLayer.getName());
				MyApplication.mApp.legendBeans = legendBeans;
				hashtable = MyApplication.mApp.colortable.get(currentSubLayer
						.getName());
				edit_search.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		mapView.setOnPanListener(new OnPanListener() {

			@Override
			public void prePointerUp(float fromx, float fromy, float tox,
					float toy) {
				// TODO Auto-generated method stub

			}

			@Override
			public void prePointerMove(float fromx, float fromy, float tox,
					float toy) {
				// TODO Auto-generated method stub

			}

			@Override
			public void postPointerUp(float fromx, float fromy, float tox,
					float toy) {
				// TODO Auto-generated method stub
				if (mapView.getScale() >= 20000) {
					keyGraphicsLayer.removeAll();
				} else {
					if (addressBeans.size() <= 0)
						return;
					 new addGraphicsAsyncTask().execute(addressBeans);
				}
			}

			@Override
			public void postPointerMove(float fromx, float fromy, float tox,
					float toy) {
				// TODO Auto-generated method stub

			}
		});

		mapView.setOnZoomListener(new OnZoomListener() {

			@Override
			public void preAction(float pivotX, float pivotY, double factor) {
				// TODO Auto-generated method stub

			}

			@SuppressWarnings("unchecked")
			@Override
			public void postAction(float pivotX, float pivotY, double factor) {
				// TODO Auto-generated method stub
				if (mapView.getScale() >= 20000) {
					keyGraphicsLayer.removeAll();
				} else {
					if (addressBeans.size() <= 0)
						return;
					 new addGraphicsAsyncTask().execute(addressBeans);
				}

			}
		});
	}

	private void loadMoreDate() {
		// List<AddressBean> moreBeans = new ArrayList<AddressBean>();
		int count = resultListAdapter.getCount();
		if (count + 15 < MaxDateNum) {
			// 每次加载15条
			for (int i = count; i < count + 15; i++) {
				loadAddress.add(addressBeans.get(i));
			}

			mListView.removeFooterView(moreView);
			resultListAdapter.notifyDataSetChanged();
		} else {
			// 数据已经不足15条
			for (int i = count; i < MaxDateNum; i++) {
				loadAddress.add(addressBeans.get(i));
			}
			mHandler.sendEmptyMessage(LOAD_ALL_DATA);
		}
		// return moreBeans;

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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_list_close:
			ll_main_list_left.setVisibility(View.GONE);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putBoolean("isScaleShow", true);
			msg.setData(data);
			msg.what = Constants.WHAT_REFRESH_ITEM_CLICK;
			Constants.HANDLER_MAIN.sendMessage(msg);
			break;
		case R.id.btn_list_search:
			isFilter = true;
			isLoad = false;
			// 关闭让软键盘
			((InputMethodManager) mContext
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(edit_search.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			// cum = edit_search.getText().toString();
			// new GetDataAsyncTask().execute(currentSubLayer);
			singleDialog();
			break;

		default:
			break;
		}
	}

	int mSingleChoiceID = -1;
	String[] mItems = new String[] { "100", "200", "500", "800", "1000",
			"1500", "2000", "3000", "4000", "5000", "8000", "10000", "20000",
			"50000", "100000" };
	String limit = "2000";

	void singleDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("单项选择");
		builder.setSingleChoiceItems(mItems, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						mSingleChoiceID = whichButton;

					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (mSingleChoiceID != -1) {
					cum = edit_search.getText().toString();
					limit = mItems[mSingleChoiceID];
					new GetDataAsyncTask().execute(currentSubLayer);
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		builder.create().show();
	}

}
