/**
 * 
 */
package com.android.smartmobile.cz.view.pop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jsqlite.Exception;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.R.color;
import com.android.smartmobile.cz.adapter.GridviewAdapter;
import com.android.smartmobile.cz.bean.LegendBean;
import com.android.smartmobile.cz.bean.SubLayer;
import com.android.smartmobile.cz.db.SDBHelper;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.interf.InitActivity;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.SymbolUtil;
import com.android.smartmobile.cz.util.ToastUtil;
import com.android.smartmobile.cz.view.ColorPickerDialog;
import com.android.smartmobile.cz.view.pop.PopColorManager.OnColorChangedListener;
import com.esri.android.map.MapView;
import com.esri.core.map.Legend;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ExpandableListView.OnChildClickListener;
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
public class PopLegendManager implements InitActivity {
	private Context mContext;
	private MapView mapView = null;
	private GridView gv_legend;
	private List<LegendBean> mList;
	private GridviewAdapter gridviewAdapter;
	private Button btn_pop_legend_close;
	private LinearLayout ll_main_legend;
	// private ColorPickerDialog dialog;
	private SDBHelper sdbHelper;
	private SymbolUtil symbolUtil;
	private PopColorManager popColorManager;

	/**
	 * 
	 * @param context
	 * @param mainMapView
	 */
	public PopLegendManager(Context context, MapView mainMapView,
			LinearLayout ll_main_legend) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mapView = mainMapView;
		this.ll_main_legend = ll_main_legend;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.smartmobile.cz.interf.InitActivity#initData()
	 */
	@Override
	public void initData() {

		mList = MyApplication.mApp.legendBeans;
		sdbHelper = new SDBHelper();
		symbolUtil = new SymbolUtil(mContext);
	}

	@Override
	public void initView() {
		initData();
		// TODO Auto-generated method stub
		// 获取自定义布局文件pop.xml的视图
		View popupWindow_view = (RelativeLayout) ll_main_legend
				.findViewById(R.id.view_legend_content);
		gv_legend = (GridView) popupWindow_view.findViewById(R.id.gv_legend);
		gridviewAdapter = new GridviewAdapter(mContext, mList);
		gv_legend.setAdapter(gridviewAdapter);
		btn_pop_legend_close = (Button) popupWindow_view
				.findViewById(R.id.btn_pop_legend_close);
		btn_pop_legend_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ll_main_legend.setVisibility(View.GONE);
			}
		});
		gv_legend.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					final int position, long arg3) {
				// TODO Auto-generated method stub

				final ImageView imageView = (ImageView) view
						.findViewById(R.id.txt_gv_item_image);
				ToastUtil.makeToastInBottom(mList.get(position).getName());
				popColorManager = new PopColorManager(mContext,
						new OnColorChangedListener() {

							@Override
							public void colorChanged(int color) {
								mList.get(position).setInitColor(color);
								imageView.setBackgroundColor(color);

								int[] is = MyApplication.mApp.keyGraphicsLayer
										.getGraphicIDs();

								if (is == null || is.length == 0)
									return;
								LogUtil.LooLog().d(
										"Graphic个数为-----------" + is.length);
								for (int i = 0; i < is.length; i++) {
									String symbol = MyApplication.mApp.keyGraphicsLayer
											.getGraphic(is[i])
											.getAttributeValue("SYMBOL")
											.toString();
									if (symbol.endsWith(mList.get(position)
											.getName())) {
										MyApplication.mApp.keyGraphicsLayer
												.updateGraphic(
														is[i],
														symbolUtil
																.getPolygonAllSymbol(color));
									}
								}

							}
						}, mList.get(position).getInitColor());
				popColorManager.initView();
				int[] location = new int[2];
				imageView.getLocationOnScreen(location);
				popColorManager.getPopupWindow().showAtLocation(imageView,
						Gravity.NO_GRAVITY, location[0] + imageView.getWidth(),
						location[1]);

			}
		});

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

}
