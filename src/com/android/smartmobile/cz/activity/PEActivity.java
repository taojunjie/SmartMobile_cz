package com.android.smartmobile.cz.activity;

import org.apache.cordova.Config;
import org.apache.cordova.DroidGap;

import android.os.Bundle;

/**
 * 
 * @ClassName: PEActivity.java
 * @author: Administrator
 * @date: 2013年9月29日 下午5:09:41
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version: 1.0
 * 
 */
public class PEActivity extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set by <content src="index.html" /> in config.xml
		super.loadUrl(Config.getStartUrl());
		// super.loadUrl("file:///android_asset/www/index.html")
	}
}
