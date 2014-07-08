/**
 * 
 */
package com.android.smartmobile.cz.view.pop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jsqlite.Exception;
import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.R.color;
import com.android.smartmobile.cz.adapter.GridviewAdapter;
import com.android.smartmobile.cz.adapter.MediaAdapter;
import com.android.smartmobile.cz.bean.AddressBean;
import com.android.smartmobile.cz.bean.LegendBean;
import com.android.smartmobile.cz.bean.MediaBean;
import com.android.smartmobile.cz.bean.SubLayer;
import com.android.smartmobile.cz.db.SDBHelper;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.interf.InitActivity;
import com.android.smartmobile.cz.map.GPSParam;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.util.BitmapUtils;
import com.android.smartmobile.cz.util.MapHelper;
import com.android.smartmobile.cz.util.StringUtil;
import com.android.smartmobile.cz.util.ToastUtil;
import com.android.smartmobile.cz.view.ColorPickerDialog;
import com.baidu.location.LocationClient;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.map.Legend;
import com.esri.core.symbol.PictureMarkerSymbol;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
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
import android.widget.TextView;

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
public class PopMediaManager implements InitActivity, OnClickListener {
	private Context mContext;
	private MapView mapView = null;
	private GridView pop_gv_media;
	private MediaAdapter mediaAdapter;
	private ImageView pop_img_media;
	private TextView txt_pop_media_describe;
	private LinearLayout ll_pop_media_2;
	private LinearLayout ll_main_media;
	private Button btn_media_change_gv, btn_media_delete, btn_media_info,
			btn_media_location, btn_media_camera, btn_pop_media_close;
	private ArrayList<MediaBean> imgPathList;
	private int index = -1;
	private MapHelper mapHelper;
	private LocationClient mLocClient;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Constants.WHAT_POP_MEDIA_REFRESH:
				initData();
				mediaAdapter = new MediaAdapter(mContext, imgPathList);
				pop_gv_media.setAdapter(mediaAdapter);
				pop_gv_media.setVisibility(View.VISIBLE);
				ll_pop_media_2.setVisibility(View.GONE);
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
	public PopMediaManager(Context context, MapView mainMapView,
			LinearLayout ll_main_media) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mapView = mainMapView;
		this.ll_main_media = ll_main_media;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.smartmobile.cz.interf.InitActivity#initData()
	 */
	@Override
	public void initData() {
		mapHelper = new MapHelper(mapView, mContext);
		mLocClient = MyApplication.mApp.mLocationClient;
		Constants.HANDLER_POP_MEDIA = handler;
		imgPathList = new ArrayList<MediaBean>();
		String sqlString = "select * from zd_attach ";
		ArrayList<Map<String, String>> lists = MyApplication.mApp.dbHelper
				.GetResult(sqlString);
		if (lists == null)
			return;
		if (lists.size() > 0) {
			MediaBean mediaBean = null;
			for (int i = 0; i < lists.size(); i++) {
				mediaBean = new MediaBean();
				mediaBean.setFEATID(lists.get(i).get("FEATID"));
				mediaBean.setFILE_NAME(lists.get(i).get("FILE_NAME"));
				mediaBean.setFILE_PATH(lists.get(i).get("FILE_PATH"));
				mediaBean.setFILE_TYPE(lists.get(i).get("FILE_TYPE"));
				mediaBean.setISUPLOAD(lists.get(i).get("ISUPLOAD"));
				mediaBean.setLAYER_NAME(lists.get(i).get("LAYER_NAME"));
				mediaBean.setUPLOAD_DATE(lists.get(i).get("UPLOAD_DATE"));
				mediaBean.setUPLOADER(lists.get(i).get("UPLOADER"));
				mediaBean.setX_Y(lists.get(i).get("X_Y"));
				mediaBean.setDESCRIBE(lists.get(i).get("DESCRIBE"));
				imgPathList.add(mediaBean);
			}
		}

	}

	@Override
	public void initView() {
		initData();
		// TODO Auto-generated method stub
		// 获取自定义布局文件pop.xml的视图
		View popupWindow_view = (RelativeLayout) ll_main_media
				.findViewById(R.id.view_media_content);
		ll_pop_media_2 = (LinearLayout) popupWindow_view
				.findViewById(R.id.ll_pop_media_2);
		pop_gv_media = (GridView) popupWindow_view
				.findViewById(R.id.pop_gv_media);
		pop_img_media = (ImageView) popupWindow_view
				.findViewById(R.id.pop_img_media);
		txt_pop_media_describe = (TextView) popupWindow_view
				.findViewById(R.id.txt_pop_media_describe);
		btn_media_change_gv = (Button) popupWindow_view
				.findViewById(R.id.btn_media_change_gv);

		btn_media_delete = (Button) popupWindow_view
				.findViewById(R.id.btn_media_delete);
		btn_media_info = (Button) popupWindow_view
				.findViewById(R.id.btn_media_info);
		btn_media_location = (Button) popupWindow_view
				.findViewById(R.id.btn_media_location);
		btn_media_camera = (Button) popupWindow_view
				.findViewById(R.id.btn_media_camera);
		btn_pop_media_close = (Button) popupWindow_view
				.findViewById(R.id.btn_pop_media_close);
		btn_media_change_gv.setOnClickListener(this);

		btn_media_delete.setOnClickListener(this);
		btn_media_info.setOnClickListener(this);
		btn_media_location.setOnClickListener(this);
		btn_media_camera.setOnClickListener(this);
		btn_pop_media_close.setOnClickListener(this);
		if (imgPathList.size() == 0) {
			ToastUtil.makeToastInBottom("没有图片");
		}
		mediaAdapter = new MediaAdapter(mContext, imgPathList);
		pop_gv_media.setAdapter(mediaAdapter);

		pop_gv_media.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					final int position, long arg3) {
				// TODO Auto-generated method stub
				index = position;
				pop_gv_media.setVisibility(View.GONE);
				ll_pop_media_2.setVisibility(View.VISIBLE);
				try {
					pop_img_media.setImageBitmap(BitmapUtils
							.readBitMapFromFile(new File(imgPathList.get(
									position).getFILE_PATH())));
					txt_pop_media_describe.setText(imgPathList.get(position)
							.getDESCRIBE());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

	/**
	 * 输入对话框
	 * 
	 * @param context
	 * @param title
	 */
	void inputDialog(Context context, String title, final String featid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		LayoutInflater factory = LayoutInflater.from(mContext);
		final View textEntryView = factory.inflate(
				R.layout.layout_input_dialog, null);
		builder.setTitle(title);
		builder.setView(textEntryView);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				EditText userName = (EditText) textEntryView
						.findViewById(R.id.edit_input_describe);
				String describe = userName.getText().toString();

				String update_sql = "UPDATE zd_attach set DESCRIBE='"
						+ describe + "' WHERE FEATID='" + featid + "'";

				if (MyApplication.mApp.dbHelper.ExecSQL(update_sql)) {
					ToastUtil.makeToastInBottom("添加成功");
					txt_pop_media_describe.setText(describe);
				}

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.create().show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_media_change_gv:
			index = -1;
			handler.sendEmptyMessage(Constants.WHAT_POP_MEDIA_REFRESH);

			break;

		case R.id.btn_media_delete:
			if (index == -1) {
				ToastUtil.makeToastInBottom("请先选中图片");
			} else {

				AlertDialog.Builder builder = new Builder(mContext,
						AlertDialog.THEME_TRADITIONAL);
				builder.setMessage("确定删除!");

				builder.setTitle("提示")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										String update_sql = "DELETE FROM zd_attach WHERE FEATID='"
												+ imgPathList.get(index)
														.getFEATID() + "'";

										if (MyApplication.mApp.dbHelper
												.ExecSQL(update_sql)) {
											ToastUtil.makeToastInBottom("删除成功");
											handler.sendEmptyMessage(Constants.WHAT_POP_MEDIA_REFRESH);
										}
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();

			}
			break;
		case R.id.btn_media_info:
			if (index == -1) {
				ToastUtil.makeToastInBottom("请先选中图片");
			} else {
				inputDialog(mContext, "请对图片添加简单描述", imgPathList.get(index)
						.getFEATID());
			}

			break;
		case R.id.btn_media_location:
			String xy = imgPathList.get(index).getX_Y();
			String[] strings = StringUtil.split(xy);
			Point point = new Point(StringUtil.String2Double(strings[0]),
					StringUtil.String2Double(strings[1]));

			GraphicsLayer graphicsLayer = new GraphicsLayer();
			graphicsLayer.addGraphic(new Graphic(point,
					new PictureMarkerSymbol(mContext.getResources()
							.getDrawable(R.drawable.location_point))));
			mapView.addLayer(graphicsLayer);
			break;
		case R.id.btn_media_camera:
			Constants.HANDLER_MAIN
					.sendEmptyMessage(Constants.WHAT_MAIN_TAKE_PHOTO);
			break;
		case R.id.btn_pop_media_close:
			ll_main_media.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

}
