/**
 * 
 */
package com.android.smartmobile.cz.view.pop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.activity.GalleryFolderPhotoActivity;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.interf.InitActivity;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.util.ToastUtil;
import com.android.smartmobile.cz.util.UUIDUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.smartmobile.sdk.graphics.PlottingTouchlistener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * 
 * @ClassName: PopLayer
 * @Description:
 * @author Administrator
 * @date 2013年8月3日 下午19:52:17
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class PopPlotManager implements InitActivity, OnClickListener {
	private Context mContext;
	private ImageView iv_plot_pen1, iv_plot_pen2, iv_plot_pen3, iv_plot_pen4,
			iv_plot_color, iv_plot_pre, iv_plot_next, iv_plot_save,
			iv_plot_review, iv_plot_delete, iv_plot_close;
	private PlottingTouchlistener plottingTouchlistener;
	private MapView mainMapView = null;
	// 声明PopupWindow对象的引用
	private PopupWindow popupWindow = null;
	// 颜色 PopupWindow
	// private PopupWindow popColorSelector;
	private GraphicsLayer temp_graphicsLayer = null;
	private MapOnTouchListener defaultListener = null;
	private Handler mHandler = new Handler();

	public void setPopupWindow(PopupWindow popupWindow) {
		this.popupWindow = popupWindow;
	}

	/**
	 * 
	 * @param context
	 * @param mainMapView
	 */
	public PopPlotManager(Context context, MapView mainMapView,
			GraphicsLayer temp_graphicsLayer) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mainMapView = mainMapView;
		this.temp_graphicsLayer = temp_graphicsLayer;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.smartmobile.cz.interf.InitActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		plottingTouchlistener = new PlottingTouchlistener(mContext,
				mainMapView, Color.RED, 5, temp_graphicsLayer);
		plottingTouchlistener.setType("POLYLINE");
		mainMapView.addLayer(temp_graphicsLayer);
		mainMapView.setOnTouchListener(plottingTouchlistener);
	}

	/***
	 * 获取PopupWindow实例
	 */
	public PopupWindow getPopupWindow() {
		return popupWindow;
	}

	/**
	 * 创建PopupWindow
	 */
	@Override
	public void initView() {

		// TODO Auto-generated method stub

		// 获取自定义布局文件pop.xml的视图
		View popupWindow_view = LayoutInflater.from(mContext).inflate(
				R.layout.pop_plot, null);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view, 1000, 68);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade_web);
		popupWindow.setOutsideTouchable(true);

		initData();
		// graphicsLayer = new GraphicsLayer();

		// // 点击其他地方消失
		// popupWindow_view.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// if (popupWindow != null && popupWindow.isShowing()) {
		// popupWindow.dismiss();
		// popupWindow = null;
		// }
		// return false;
		// }
		//
		// });

		// pop.xml视图里面的控件

		iv_plot_pen1 = (ImageView) popupWindow_view
				.findViewById(R.id.iv_plot_pen1);
		iv_plot_pen2 = (ImageView) popupWindow_view
				.findViewById(R.id.iv_plot_pen2);
		iv_plot_pen3 = (ImageView) popupWindow_view
				.findViewById(R.id.iv_plot_pen3);
		iv_plot_pen4 = (ImageView) popupWindow_view
				.findViewById(R.id.iv_plot_pen4);
		iv_plot_color = (ImageView) popupWindow_view
				.findViewById(R.id.iv_plot_color);
		iv_plot_pre = (ImageView) popupWindow_view
				.findViewById(R.id.iv_plot_pre);
		iv_plot_next = (ImageView) popupWindow_view
				.findViewById(R.id.iv_plot_next);
		iv_plot_save = (ImageView) popupWindow_view
				.findViewById(R.id.iv_plot_save);
		iv_plot_review = (ImageView) popupWindow_view
				.findViewById(R.id.iv_plot_review);
		iv_plot_delete = (ImageView) popupWindow_view
				.findViewById(R.id.iv_plot_delete);
		iv_plot_close = (ImageView) popupWindow_view
				.findViewById(R.id.iv_plot_close);

		iv_plot_pen1.setOnClickListener(this);
		iv_plot_pen2.setOnClickListener(this);
		iv_plot_pen3.setOnClickListener(this);
		iv_plot_pen4.setOnClickListener(this);
		iv_plot_color.setOnClickListener(this);
		iv_plot_pre.setOnClickListener(this);
		iv_plot_next.setOnClickListener(this);
		iv_plot_save.setOnClickListener(this);
		iv_plot_review.setOnClickListener(this);
		iv_plot_delete.setOnClickListener(this);
		iv_plot_close.setOnClickListener(this);

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
		case R.id.iv_plot_pen1:
			resetPlotPaintBackgroud();
			iv_plot_pen1
					.setBackgroundResource(R.drawable.btn_plotting_width_one_p);
			plottingTouchlistener.setSelectwidth(2);
			break;
		case R.id.iv_plot_pen2:
			resetPlotPaintBackgroud();
			iv_plot_pen2
					.setBackgroundResource(R.drawable.btn_plotting_width_two_p);
			plottingTouchlistener.setSelectwidth(10);
			break;
		case R.id.iv_plot_pen3:
			resetPlotPaintBackgroud();
			iv_plot_pen3
					.setBackgroundResource(R.drawable.btn_plotting_width_three_p);
			plottingTouchlistener.setSelectwidth(19);
			break;
		case R.id.iv_plot_pen4:
			resetPlotPaintBackgroud();
			iv_plot_pen4
					.setBackgroundResource(R.drawable.btn_plotting_width_four_p);
			plottingTouchlistener.setSelectwidth(35);
			break;
		case R.id.iv_plot_color:
			// popupWindow.dismiss();

			View viewColorSelector = LayoutInflater.from(mContext).inflate(
					R.layout.pop_color, null);
			final PopupWindow popColorSelector = new PopupWindow(
					viewColorSelector, 80, 230);
			popColorSelector.setFocusable(true);
			popColorSelector.setOutsideTouchable(true);
			popColorSelector
					.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			// popColorSelector.setBackgroundDrawable(new BitmapDrawable());
			int[] location = new int[2];
			iv_plot_color.getLocationOnScreen(location);

			popColorSelector.showAtLocation(Constants.ll_left_menu,
					Gravity.NO_GRAVITY, location[0] - 10, location[1]
							- popColorSelector.getHeight());

			// popColorSelector.showAtLocation(iv_plot_color,
			// Gravity.NO_GRAVITY,
			// location[0], location[1] - popColorSelector.getHeight());

			((ImageView) viewColorSelector.findViewById(R.id.color_red))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							iv_plot_color.setSelected(false);
							iv_plot_color
									.setBackgroundResource(R.drawable.selector_btn_plotting_color_red);
							plottingTouchlistener.mselectColor = Color.RED;
							popColorSelector.dismiss();
						}
					});

			((ImageView) viewColorSelector.findViewById(R.id.color_green))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							iv_plot_color.setSelected(false);
							iv_plot_color
									.setBackgroundResource(R.drawable.selector_btn_plotting_color_green);
							plottingTouchlistener.mselectColor = Color.GREEN;
							popColorSelector.dismiss();
						}
					});
			((ImageView) viewColorSelector.findViewById(R.id.color_purple))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							iv_plot_color.setSelected(false);
							iv_plot_color
									.setBackgroundResource(R.drawable.selector_btn_plotting_color_purple);
							plottingTouchlistener.mselectColor = Color.rgb(
									0xDA, 0x70, 0xD6);
							popColorSelector.dismiss();
						}
					});
			((ImageView) viewColorSelector.findViewById(R.id.color_blue))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							iv_plot_color.setSelected(false);
							iv_plot_color
									.setBackgroundResource(R.drawable.selector_btn_plotting_color_blue);
							plottingTouchlistener.mselectColor = Color.BLUE;
							popColorSelector.dismiss();
						}
					});
			((ImageView) viewColorSelector.findViewById(R.id.color_lakeblue))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							iv_plot_color.setSelected(false);
							iv_plot_color
									.setBackgroundResource(R.drawable.selector_btn_plotting_color_lakeblue);
							plottingTouchlistener.mselectColor = Color.rgb(
									0x50, 0xc8, 0xBE);
							popColorSelector.dismiss();
						}
					});
			((ImageView) viewColorSelector.findViewById(R.id.color_orange))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							iv_plot_color.setSelected(false);
							iv_plot_color
									.setBackgroundResource(R.drawable.selector_btn_plotting_color_orange);
							plottingTouchlistener.mselectColor = Color.rgb(
									0xDD, 0x9E, 0x2D);
							popColorSelector.dismiss();
						}
					});
			break;
		case R.id.iv_plot_pre:
			plottingTouchlistener.preGraphicsLayer();
			break;
		case R.id.iv_plot_next:
			plottingTouchlistener.nextGraphicsLayer();
			break;
		case R.id.iv_plot_save:
			GetandSaveCurrentImage();
			break;
		case R.id.iv_plot_review:
			File folder = new File(Constants.ScreenImagePath);
			if (!folder.exists())
				folder.mkdirs();

			File[] listFiles = folder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					if (filename.endsWith(".jpg") || filename.endsWith(".png")
							|| filename.endsWith(".gif")) {
						return true;
					}
					return false;
				}
			});
			if ((null == listFiles) || listFiles.length < 1) {
				ToastUtil.makeToastInBottom("当前无任何标绘保存的图片，请标绘后再预览");
				return;
			}

			Intent intent = new Intent(mContext,
					GalleryFolderPhotoActivity.class);
			mContext.startActivity(intent);

			break;
		case R.id.iv_plot_delete:
			if (null != temp_graphicsLayer)
				temp_graphicsLayer.removeAll();
			plottingTouchlistener.delGraphicsLayer();

			break;
		case R.id.iv_plot_close:
			if (null != popupWindow) {
				popupWindow.dismiss();
				popupWindow = null;
				defaultListener = new MapOnTouchListener(mContext, mainMapView);
				mainMapView.setOnTouchListener(defaultListener);
				temp_graphicsLayer.removeAll();
			}
			Message message = new Message();
			message.what = Constants.REFRESH_PLOTTING;
			Constants.HANDLER_TOOL.sendMessage(message);
			break;

		default:
			break;
		}
	}

	// 恢复所有的标绘的 笔 粗细大小的背景图片;
	private void resetPlotPaintBackgroud() {
		// ((ImageView) contentView.findViewById(R.id.iv_plot_pen2))
		iv_plot_pen1.setBackgroundResource(R.drawable.btn_plotting_width_one);
		iv_plot_pen2.setBackgroundResource(R.drawable.btn_plotting_width_two);
		iv_plot_pen3.setBackgroundResource(R.drawable.btn_plotting_width_three);
		iv_plot_pen4.setBackgroundResource(R.drawable.btn_plotting_width_four);

	}

	// 地图截屏图片保存
	private void GetandSaveCurrentImage() {

		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("是否保存标绘?");
		builder.setTitle("提示")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						try {
							if (saveMyBitmap(getViewBitmap(mainMapView),
									Constants.ScreenImagePath)) {
								AlertDialog.Builder builder = new Builder(
										mContext);
								builder.setMessage("标绘文件保存成功!");

								builder.setTitle("提示")
										.setPositiveButton(
												"确定",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int which) {
													}
												}).show();
							} else {

							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();

	}

	/**
	 * 把一个View的对象转换成bitmap
	 */
	private Bitmap getViewBitmap(MapView v) {
		v.clearFocus();
		v.setPressed(false);
		// 能画缓存就返回false
		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);
		if (color != 0) {
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = null;
		while (cacheBitmap == null) {
			cacheBitmap = v.getDrawingMapCache(0, 0, v.getWidth(),
					v.getHeight());
		}
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		// Restore the view
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);
		return bitmap;
	}

	public boolean saveMyBitmap(Bitmap bitmap, String bitName)
			throws IOException {
		boolean b = false;
		File file = new File(bitName);
		if (!file.exists()) {
			file.mkdirs();
		}
		File f = new File(bitName +"/"+ UUIDUtil.OnlyID() + ".png");
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			b = false;
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
			fOut.close();
			b = true;
		} catch (IOException e) {
			b = false;
			e.printStackTrace();
		}
		return b;

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
