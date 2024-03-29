/**
 * 
 */
package com.android.smartmobile.cz.adapter;

import java.util.List;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.bean.CatalogBean;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.ToastUtil;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * 
 * @ClassName: ExAdapter
 * @Description:
 * @author Administrator
 * @date 2013年8月2日 下午1:44:14
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class ExAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	// private List<List<GroupLayer>> childData;// 小组成员
	private List<CatalogBean> groupData;// 大组成员
	private MapView mapView;

	/**
	 * 
	 */
	public ExAdapter(Context context, List<CatalogBean> catalogs, MapView mapView) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.groupData = catalogs;
		this.mapView = mapView;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub

		return groupData.get(groupPosition).getGroupLayers().get(childPosition)
				.getAliasName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.ex_list_child, null);
		}
		if (childPosition % 2 == 0) {
			view.setBackgroundResource(R.drawable.lv_bg_ou);
		} else {
			view.setBackgroundResource(R.drawable.lv_bg);
		}

		final RelativeLayout ll_child_tool = (RelativeLayout) view
				.findViewById(R.id.ll_child_tool);
		final LinearLayout ll_child_tool_isvisible = (LinearLayout) view
				.findViewById(R.id.ll_child_tool_isvisible);

		final Button btn_child_isvisible = (Button) ll_child_tool_isvisible
				.findViewById(R.id.btn_child_isvisible);

		final Button btn_child_isvisible_p = (Button) ll_child_tool_isvisible
				.findViewById(R.id.btn_child_isvisible_p);
		final String url = groupData.get(groupPosition).getGroupLayers()
				.get(childPosition).getUrl();

//		final ArcGISDynamicMapServiceLayer dynamicMapServiceLayer = new ArcGISDynamicMapServiceLayer(
//				url);
		
		final ArcGISLocalTiledLayer dynamicMapServiceLayer=new ArcGISLocalTiledLayer(url);
		
		TextView txt_child_name = (TextView) view
				.findViewById(R.id.txt_child_name);
		Button btn_child_location = (Button) view
				.findViewById(R.id.btn_child_location);
		if (groupData.get(groupPosition).getGroupLayers().get(childPosition)
				.isVisible()) {
			btn_child_isvisible.setVisibility(View.GONE);
			btn_child_isvisible_p.setVisibility(View.VISIBLE);
		} else {
			btn_child_isvisible.setVisibility(View.VISIBLE);
			btn_child_isvisible_p.setVisibility(View.GONE);
		}

		OnClickListener onclick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.ll_child_tool_isvisible:
					if (btn_child_isvisible_p.getVisibility() == View.VISIBLE) {
						btn_child_isvisible_p.setVisibility(View.GONE);
						btn_child_isvisible.setVisibility(View.VISIBLE);

						if (dynamicMapServiceLayer != null
								&& dynamicMapServiceLayer.isVisible()) {
							dynamicMapServiceLayer.setVisible(false);
							groupData.get(groupPosition).getGroupLayers()
									.get(childPosition).setVisible(false);
						}

					} else {
						btn_child_isvisible.setVisibility(View.GONE);
						btn_child_isvisible_p.setVisibility(View.VISIBLE);
						if (dynamicMapServiceLayer != null
								&& !dynamicMapServiceLayer.isVisible()) {
							dynamicMapServiceLayer.setVisible(true);
						} else {
							mapView.addLayer(dynamicMapServiceLayer);
							LogUtil.LooLog().d("tpk--"+url);
						}
						groupData.get(groupPosition).getGroupLayers()
								.get(childPosition).setVisible(true);
					}
					break;
				case R.id.btn_child_isvisible_p:
					btn_child_isvisible_p.setVisibility(View.GONE);
					btn_child_isvisible.setVisibility(View.VISIBLE);

					if (dynamicMapServiceLayer != null
							&& dynamicMapServiceLayer.isVisible()) {
						dynamicMapServiceLayer.setVisible(false);
						groupData.get(groupPosition).getGroupLayers()
								.get(childPosition).setVisible(false);
					}
					break;
				case R.id.btn_child_isvisible:
					btn_child_isvisible.setVisibility(View.GONE);
					btn_child_isvisible_p.setVisibility(View.VISIBLE);
					if (dynamicMapServiceLayer != null
							&& !dynamicMapServiceLayer.isVisible()) {
						dynamicMapServiceLayer.setVisible(true);
					} else {
						mapView.addLayer(dynamicMapServiceLayer);
					}
					groupData.get(groupPosition).getGroupLayers()
							.get(childPosition).setVisible(true);
					break;
				case R.id.txt_child_name:
					if (ll_child_tool.getVisibility() == View.VISIBLE) {
						ll_child_tool.setVisibility(View.GONE);
					} else {
						ll_child_tool.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.btn_child_location:

					break;
				default:
					break;
				}

			}
		};

		btn_child_isvisible_p.setOnClickListener(onclick);
		btn_child_isvisible.setOnClickListener(onclick);
		ll_child_tool_isvisible.setOnClickListener(onclick);

		txt_child_name.setText(groupData.get(groupPosition).getGroupLayers()
				.get(childPosition).getName());// 设置大组成员名称

		SeekBar seekbar_child = (SeekBar) view.findViewById(R.id.seekbar_child);
		seekbar_child.setProgress(100);

		seekbar_child.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (!groupData.get(groupPosition).getGroupLayers()
						.get(childPosition).isVisible()) {
					// ToastUtil.makeToastInBottom("请先开启图层");
					return;
				} else {
					// TODO Auto-generated method stub
					float progress_f = (float) ((seekBar.getProgress()) * (0.01));
					// txt_child_seekbar.setText(progress_f + "");
					mapView.getLayerByURL(url).setOpacity(progress_f);
				}

			}
		});

		btn_child_location.setOnClickListener(onclick);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return groupData.get(groupPosition).getGroupLayers().size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupData.get(groupPosition).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groupData.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.ex_list_groups, null);
		}

		TextView title = (TextView) view.findViewById(R.id.txt_group_name);
		// title.setPadding(40, 0, 0, 0);
		if (isExpanded) {
			title.setBackgroundResource(R.drawable.spinner_bg_p);
		} else {
			title.setBackgroundResource(R.drawable.spinner_bg);
		}
		title.setText(getGroup(groupPosition).toString());// 设置大组成员名称
		return view;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
