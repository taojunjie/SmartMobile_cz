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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

import com.android.smartmobile.cz.bean.CatalogBean;
import com.android.smartmobile.cz.bean.ExtentBean;
import com.android.smartmobile.cz.bean.GroupLayer;
import com.android.smartmobile.cz.bean.ImageBean;
import com.android.smartmobile.cz.bean.LODInfo;
import com.android.smartmobile.cz.bean.LayerBean;
import com.android.smartmobile.cz.bean.LevelBean;
import com.android.smartmobile.cz.bean.ModuleBean;
import com.android.smartmobile.cz.bean.ModulesBean;
import com.android.smartmobile.cz.bean.OriginPoint;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.listener.LooLongPressListener;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.util.LogUtil;
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
public class PullParseGHFieldService {

	public static PullParseGHFieldService parseServiceSingleton = null;

	public static PullParseGHFieldService getInstance() {
		if (parseServiceSingleton == null) {
			synchronized (PullParseGHFieldService.class) {
				if (parseServiceSingleton == null) {
					parseServiceSingleton = new PullParseGHFieldService();
				}
			}
		}
		return parseServiceSingleton;
	}

	/**
	 * 
	 */
	public PullParseGHFieldService() {
		// TODO Auto-generated constructor stub

	}

	protected static InputStream getInputStreamFromAssets() {
		InputStream inputStream = null;
		try {
			inputStream = MyApplication.mApp.mContext.getAssets().open(
					Constants.GHField_PATH);
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

	public static Object getArrByName(String name) {
		Map<String, Object> map = getArrMap();
		Set<String> set = map.keySet();
		for (String s : set) {
			if (s.equalsIgnoreCase(name)) {
				return map.get(s);
			}
		}
		return null;
	}

	public static HashMap<String, Object> getArrMap() {
		SAXParserFactory factorys = SAXParserFactory.newInstance();
		SAXParser saxparser = null;
		try {
			saxparser = factorys.newSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PlistHandler plistHandler = new PlistHandler();
		XMLReader reader = null;
		try {
			reader = saxparser.getXMLReader();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.setContentHandler(plistHandler);
		try {
			reader.parse(new InputSource(getInputStreamFromAssets()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, Object> hash = plistHandler.getMapResult();
		// ArrayList<Object> array = (ArrayList<Object>) plistHandler
		// .getArrayResult();
		LogUtil.LooLog().d(hash.size());
		// LogUtil.LoLoLog().d(array.size());
		return hash;
	}
}
