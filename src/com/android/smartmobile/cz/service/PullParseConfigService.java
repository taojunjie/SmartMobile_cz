package com.android.smartmobile.cz.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.android.smartmobile.cz.bean.CatalogBean;
import com.android.smartmobile.cz.bean.GroupLayer;
import com.android.smartmobile.cz.bean.ModuleBean;
import com.android.smartmobile.cz.bean.ModulesBean;
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
public class PullParseConfigService {

	public static PullParseConfigService parseServiceSingleton = null;

	public static PullParseConfigService getInstance() {
		if (parseServiceSingleton == null) {
			synchronized (PullParseConfigService.class) {
				if (parseServiceSingleton == null) {
					parseServiceSingleton = new PullParseConfigService();
				}
			}
		}
		return parseServiceSingleton;
	}

	/**
	 * 
	 */
	public PullParseConfigService() {
		// TODO Auto-generated constructor stub

	}

	protected static InputStream getInputStreamFromAssets() {
		InputStream inputStream = null;
		try {
			inputStream = MyApplication.mApp.mContext.getAssets()
					.open(Constants.CONFIG_PATH);
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
	 * 得到所有模板
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static ModulesBean getModules() throws Exception {
		ModulesBean modules = null;
		List<ModuleBean> lModules = null;
		ModuleBean module = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(getInputStreamFromAssets(), "UTF-8");

		int event = parser.getEventType();// 产生第一个事件

		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件

				break;
			case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
				if ("modules".equals(parser.getName())) {// 判断开始标签元素是否是book
					modules = new ModulesBean();
					String attString0 = parser.getAttributeValue(0);
					String attString1 = parser.getAttributeValue(1);
					if (!attString0.equalsIgnoreCase("")) {
						modules.setWidth(Integer.parseInt(attString0));// 得到modules标签的属性值
					} else if (!attString1.equalsIgnoreCase("")) {
						modules.setHeight(Integer.parseInt(attString1));
					}
					lModules = new ArrayList<ModuleBean>();// 初始化lModules集合
				}

				if ("module".equals(parser.getName())) {// 判断开始标签元素是否是book
					module = new ModuleBean();
				}
				if (module != null) {
					if ("title".equals(parser.getName())) {// 判断开始标签元素是否是name
						module.setTitle(parser.nextText());
					}
					if ("img_nomal".equals(parser.getName())) {// 判断开始标签元素是否是name
						module.setImg_nomal(parser.nextText());
					}
					if ("img_press".equals(parser.getName())) {// 判断开始标签元素是否是name
						module.setImg_press(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
				if ("module".equals(parser.getName())) {// 判断结束标签元素是否是book
					lModules.add(module);// 将book添加到books集合
					module = null;
				}
				break;
			}
			event = parser.next();// 进入下一个元素并触发相应事件
		}
		modules.setList(lModules);

		return modules;
	}

	
}
