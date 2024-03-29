package com.android.smartmobile.cz.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

import com.android.smartmobile.cz.bean.AppConfigInfo;
import com.android.smartmobile.cz.bean.CatalogBean;
import com.android.smartmobile.cz.bean.ExtentBean;
import com.android.smartmobile.cz.bean.GroupLayer;
import com.android.smartmobile.cz.bean.ImageBean;
import com.android.smartmobile.cz.bean.LODInfo;
import com.android.smartmobile.cz.bean.LayerBean;
import com.android.smartmobile.cz.bean.LevelBean;
import com.android.smartmobile.cz.bean.MapItem;
import com.android.smartmobile.cz.bean.OriginPoint;
import com.android.smartmobile.cz.bean.QueryField;
import com.android.smartmobile.cz.bean.SubLayer;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.util.ToastUtil;

/**
 * 
 * @ClassName:PullParseService
 * @Description: xml pull解析
 * @author
 * @date 2013年7月31日 下午1:15:53
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 */
public class PullParseConfigMapService {

	public static PullParseConfigMapService parseServiceSingleton = null;

	public static PullParseConfigMapService getInstance() {
		if (parseServiceSingleton == null) {
			synchronized (PullParseConfigMapService.class) {
				if (parseServiceSingleton == null) {
					parseServiceSingleton = new PullParseConfigMapService();
				}
			}
		}
		return parseServiceSingleton;
	}

	/**
	 * 
	 */
	public PullParseConfigMapService() {
		// TODO Auto-generated constructor stub

	}

	protected static InputStream getInputStreamFromAssets() {
		InputStream inputStream = null;
		try {
			inputStream = MyApplication.mApp.mContext.getAssets().open(
					Constants.CONFIG_MAP_PATH);
			if (inputStream != null) {
				return inputStream;
			} else {
				ToastUtil.makeToastInBottom("配置文件不存在");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return inputStream;
	}

	/**
	 * 获得所有图层
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static List<CatalogBean> getCatalogs() throws Exception {
		List<CatalogBean> lCatalogs = null;
		List<GroupLayer> lGroupLayers = null;
		CatalogBean catalog = null;
		GroupLayer groupLayer = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(getInputStreamFromAssets(), "UTF-8");

		int event = parser.getEventType();// 产生第一个事件

		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
				lCatalogs = new ArrayList<CatalogBean>();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("catalog".equals(parser.getName())) {// 判断开始标签元素是否是book
					catalog = new CatalogBean();
					String name = parser.getAttributeValue(0);
					if (!name.equalsIgnoreCase("")) {
						catalog.setName(name);// 得到modules标签的属性值
					}
					lGroupLayers = new ArrayList<GroupLayer>();// 初始化lModules集合
				}

				if (catalog != null) {
					if ("groupLayer".equals(parser.getName())) {// 判断开始标签元素是否是name
						groupLayer = new GroupLayer();
						String name = parser.getAttributeValue(0);
						String aliasName = parser.getAttributeValue(1);
						String type = parser.getAttributeValue(2);
						String visibleLayers = parser.getAttributeValue(3);

						String url = parser.nextText();

						groupLayer.setName(name);
						groupLayer.setAliasName(aliasName);
						groupLayer.setType(type);
						groupLayer.setVisibleLayers(visibleLayers);
						groupLayer.setUrl(url);
						lGroupLayers.add(groupLayer);//
						groupLayer = null;
					}

				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件

				if ("catalog".equals(parser.getName())) {// 判断结束标签元素是否是book
					catalog.setGroupLayers(lGroupLayers);
					lCatalogs.add(catalog);// 将book添加到books集合
					catalog = null;
				}
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}

		return lCatalogs;
	}

	public static List<MapItem> getMapsgement() throws XmlPullParserException,
			IOException {
		MapItem mapItem = null;
		List<MapItem> lItems = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(getInputStreamFromAssets(), "UTF-8");

		int event = parser.getEventType();// 产生第一个事件

		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
				lItems = new ArrayList<MapItem>();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("mapitem".equals(parser.getName())) {//
					mapItem = new MapItem();
					String name = parser.getAttributeValue(0);
					String type = parser.getAttributeValue(1);
					String url = parser.getAttributeValue(2);
					mapItem.setName(name);
					mapItem.setType(type);
					mapItem.setUrl(url);
				}
				if (mapItem != null) {

				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("mapitem".equals(parser.getName())) {
					lItems.add(mapItem);
					mapItem = null;
				}
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}

		return lItems;
	}

	public static AppConfigInfo getAppConfigInfo()
			throws XmlPullParserException, IOException {
		AppConfigInfo appConfigInfo = null;

		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(getInputStreamFromAssets(), "UTF-8");

		int event = parser.getEventType();// 产生第一个事件

		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
				appConfigInfo = new AppConfigInfo();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("ApplicationTitle".equals(parser.getName())) {//
					appConfigInfo.setApplicationTitle(parser.nextText());
				}
				if ("maplogo".equals(parser.getName())) {//
					appConfigInfo.setMaplogo(parser.nextText());
				}
				if ("InitExtent".equals(parser.getName())) {//
					String xmin = parser.getAttributeValue(0);
					String ymin = parser.getAttributeValue(1);
					String xmax = parser.getAttributeValue(2);
					String ymax = parser.getAttributeValue(3);
					String spatialReference = parser.getAttributeValue(4);

					ExtentBean extent = new ExtentBean();
					extent.setXmin(xmin);
					extent.setYmin(ymin);
					extent.setXmax(xmax);
					extent.setYmax(ymax);
					extent.setSpatialReference(spatialReference);

					appConfigInfo.setExtent(extent);
				}

				if ("resolution".equals(parser.getName())) {//
					appConfigInfo.setResolution(parser.nextText());
				}

				if ("scale".equals(parser.getName())) {//
					appConfigInfo.setScale(parser.nextText());
				}

				if ("serviceUrl".equals(parser.getName())) {//
					appConfigInfo.setServiceUrl(parser.nextText());
				}
				if ("exploreUrl".equals(parser.getName())) {//
					appConfigInfo.setExploreUrl(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件

				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}

		return appConfigInfo;
	}

	public static LayerBean getLayerByName(String name)
			throws XmlPullParserException, IOException {
		Map<String, LayerBean> map = getLayers();
		Set<String> set = map.keySet();
		for (String s : set) {
			if (s.equalsIgnoreCase(name)) {
				return map.get(s);
			}
		}
		return null;
	}

	public static Map<String, LayerBean> getLayers() throws XmlPullParserException,
			IOException {
		LayerBean layer = null;
		Map<String, LayerBean> map = null;
		String name = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(getInputStreamFromAssets(), "UTF-8");

		int event = parser.getEventType();// 产生第一个事件

		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
				map = new HashMap<String, LayerBean>();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件

				if ("layerParamDefine".equals(parser.getName())) {//

					layer = new LayerBean();

				}

				if (layer != null) {
					if ("layer".equals(parser.getName())) {//

						name = parser.getAttributeValue(0);
						layer.setName(name);
					}

					if ("downloadable".equals(parser.getName())) {//
						layer.setDownloadable(parser.nextText());
					}

					if ("SSID".equals(parser.getName())) {//
						layer.setSSID(parser.nextText());
					}

					if ("unit".equals(parser.getName())) {//
						layer.setUnit(parser.nextText());
					}

					if ("InitialExtent".equals(parser.getName())) {//
						String xmin = parser.getAttributeValue(0);
						String ymin = parser.getAttributeValue(1);
						String xmax = parser.getAttributeValue(2);
						String ymax = parser.getAttributeValue(3);
						String spatialReference = parser.getAttributeValue(4);

						ExtentBean initialExtent = new ExtentBean();
						initialExtent.setXmin(xmin);
						initialExtent.setYmin(ymin);
						initialExtent.setXmax(xmax);
						initialExtent.setYmax(ymax);
						initialExtent.setSpatialReference(spatialReference);

						layer.setInitialExtent(initialExtent);
					}

					if ("FullExtent".equals(parser.getName())) {//
						String xmin = parser.getAttributeValue(0);
						String ymin = parser.getAttributeValue(1);
						String xmax = parser.getAttributeValue(2);
						String ymax = parser.getAttributeValue(3);
						String spatialReference = parser.getAttributeValue(4);

						ExtentBean fullExtent = new ExtentBean();
						fullExtent.setXmin(xmin);
						fullExtent.setYmin(ymin);
						fullExtent.setXmax(xmax);
						fullExtent.setYmax(ymax);
						fullExtent.setSpatialReference(spatialReference);

						layer.setFullExtent(fullExtent);
					}

					if ("Image".equals(parser.getName())) {//
						ImageBean image = new ImageBean();
						String dpi = parser.getAttributeValue(0);
						String width = parser.getAttributeValue(1);
						String format = parser.getAttributeValue(2);
						String compressionQuality = parser.getAttributeValue(3);
						image.setDpi(dpi);
						image.setWidth(width);
						image.setFormat(format);
						image.setCompressionQuality(compressionQuality);
						layer.setImage(image);
					}

					if ("OriginPoint".equals(parser.getName())) {//
						OriginPoint originPoint = new OriginPoint();
						String x = parser.getAttributeValue(0);
						String y = parser.getAttributeValue(1);
						String spatialReference = parser.getAttributeValue(2);
						originPoint.setX(x);
						originPoint.setY(y);
						originPoint.setSpatialReference(spatialReference);
						layer.setOriginPoint(originPoint);
					}

					if ("LODInfo".equals(parser.getName())) {//
						LODInfo lodInfo = new LODInfo();
						String levels = parser.getAttributeValue(0);
						String initialScale = parser.getAttributeValue(1);
						String initialResolution = parser.getAttributeValue(2);
						String miniLevel = parser.getAttributeValue(3);
						lodInfo.setLevels(levels);
						lodInfo.setInitialScale(initialScale);
						lodInfo.setInitialResolution(initialResolution);
						lodInfo.setMiniLevel(miniLevel);
						layer.setLodInfo(lodInfo);
					}
					if ("level".equals(parser.getName())) {//
						LevelBean level = new LevelBean();
						String minlevel = parser.getAttributeValue(0);
						String maxlevel = parser.getAttributeValue(1);
						String url = parser.nextText();
						level.setMinlevel(minlevel);
						level.setMaxlevel(maxlevel);
						level.setUrl(url);
						layer.setLevel(level);
					}
					if ("tpk".equals(parser.getName())) {//
						layer.setTpk(parser.nextText());
					}
				}

				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if (layer != null) {
					if ("layer".equals(parser.getName())) {//
						map.put(name, layer);
						layer = new LayerBean();
					}

					if ("layerParamDefine".equals(parser.getName())) {//
						layer = null;
					}
				}

				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return map;

	}

	public static List<SubLayer> getSubLayers() throws XmlPullParserException,
			IOException {

		List<SubLayer> subLayers = null;
		SubLayer subLayer = null;
		List<QueryField> queryFields = null;
		QueryField queryField = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(getInputStreamFromAssets(), "UTF-8");

		int event = parser.getEventType();// 产生第一个事件

		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
				subLayers = new ArrayList<SubLayer>();
				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件

				if ("subLayer".equals(parser.getName())) {//
					subLayer = new SubLayer();
					String name = parser.getAttributeValue(0);
					String titleField = parser.getAttributeValue(1);
					String subTitleField = parser.getAttributeValue(2);
					String canQuery = parser.getAttributeValue(3);
					String canStatistics = parser.getAttributeValue(4);
					String geoType = parser.getAttributeValue(5);

					subLayer.setName(name);
					subLayer.setTitleField(titleField);
					subLayer.setSubTitleField(subTitleField);
					subLayer.setCanQuery(canQuery);
					subLayer.setCanStatistics(canStatistics);
					subLayer.setGeoType(geoType);

				}

				if (subLayer != null) {
					if ("url".equals(parser.getName())) {//
						String url = parser.nextText();
						String tableName = url.substring(
								url.lastIndexOf("/") + 1, url.lastIndexOf("."));
						subLayer.setUrl(url);
						subLayer.setTableName(tableName);
					}

					if ("symbol".equals(parser.getName())) {//
						String symbol = parser.nextText();
						subLayer.setSymbol(symbol);
					}

					// if ("queryFields".equals(parser.getName())) {//
					// queryFields=new ArrayList<QueryField>();
					// }
					//
					//
					// if (queryFields != null) {
					// if ("queryField".equals(parser.getName())) {
					// queryField=new QueryField();
					// String fieldName = parser.getAttributeValue(0);
					// String aliasName = parser.getAttributeValue(1);
					// String AGSFieldType = parser.getAttributeValue(2);
					// String canEnumable = parser.getAttributeValue(3);
					// String defaultOperator = parser
					// .getAttributeValue(4);
					// queryField.setFieldName(fieldName);
					// queryField.setAliasName(aliasName);
					// queryField.setAGSFieldType(AGSFieldType);
					// queryField.setCanEnumable(canEnumable);
					// queryField.setDefaultOperator(defaultOperator);
					// }
					// }

				}

				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("subLayer".equals(parser.getName())) {
					subLayers.add(subLayer);
					subLayer = null;
				}
				// if ("queryFields".equals(parser.getName())) {
				// subLayer.setQueryFields(queryFields);
				// }
				// if ("queryField".equals(parser.getName())) {
				// queryFields.add(queryField);
				// queryField=null;
				// }
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		return subLayers;

	}

}
