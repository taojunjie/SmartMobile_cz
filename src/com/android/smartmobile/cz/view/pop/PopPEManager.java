/**
 * 
 */
package com.android.smartmobile.cz.view.pop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.adapter.ExListTypeAdapter;
import com.android.smartmobile.cz.adapter.ResultListAdapter;
import com.android.smartmobile.cz.bean.AddressBean;
import com.android.smartmobile.cz.bean.CatalogBean;
import com.android.smartmobile.cz.bean.SubLayer;
import com.android.smartmobile.cz.db.SDBHelper;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.interf.InitActivity;
import com.android.smartmobile.cz.model.BaseActivity;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.service.PullParseConfigMapService;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.StringUtil;
import com.android.smartmobile.cz.util.ToastUtil;
import com.esri.android.map.Callout;
import com.esri.android.map.CalloutStyle;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.smartmobile.sdk.graphics.PlottingTouchlistener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
public class PopPEManager  implements InitActivity {
	private Context mContext;
	private WebView webView;
	private MapView mapView = null;
	private LinearLayout ll_web;
	private Handler mHandler = new Handler();
	private String url = "";

	/**
	 * 
	 * @param context
	 * @param mainMapView
	 */
	public PopPEManager(Context context, MapView mainMapView, LinearLayout ll_web) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mapView = mainMapView;
		this.ll_web = ll_web;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.smartmobile.cz.interf.InitActivity#initData()
	 */
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		url = MyApplication.mApp.appConfigInfo.getExploreUrl();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		initData();
		RelativeLayout rl_web_content = (RelativeLayout) ll_web
				.findViewById(R.id.view_web_content);

		webView = (WebView) rl_web_content.findViewById(R.id.webview_main);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		// 启用数据库
		webSettings.setDatabaseEnabled(true);
		String dir = mContext.getDir("database", Context.MODE_PRIVATE)
				.getPath();
		// 设置数据库路径
		webSettings.setDatabasePath(dir);
		// 使用localStorage则必须打开
		webSettings.setDomStorageEnabled(true);

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

		});

		// webView.setWebChromeClient(new MyWebChromeClient());
		webView.addJavascriptInterface(new Object() {
			public void clickOnAndroid(final int index) {
				mHandler.post(new Runnable() {
					public void run() {
						if (index == 1) {
							webView.loadUrl("javascript:show(1)");
						} else if (index == 2) {
							webView.loadUrl("javascript:show(2)");
						}

					}
				});
			}
		}, "demo");

		if (!StringUtil.isEmpty(url)) {
			webView.loadUrl(url);
		}

	}

	class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onExceededDatabaseQuota(String url,
				String databaseIdentifier, long quota,
				long estimatedDatabaseSize, long totalQuota,
				QuotaUpdater quotaUpdater) {
			// TODO Auto-generated method stub
			super.onExceededDatabaseQuota(url, databaseIdentifier, quota,
					estimatedDatabaseSize, totalQuota, quotaUpdater);
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				Callback callback) {
			// TODO Auto-generated method stub
			super.onGeolocationPermissionsShowPrompt(origin, callback);
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {
			// 构建一个Builder来显示网页中的对话框
			Builder builder = new Builder(mContext);
			builder.setTitle("Alert");
			builder.setMessage(message);
			builder.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							result.confirm();
						}
					});
			builder.setCancelable(false);
			builder.create();
			builder.show();
			return true;
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

}