/**
 * 
 */
package com.android.smartmobile.cz.finals;

import java.util.List;

import com.android.smartmobile.cz.bean.CatalogBean;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.widget.LinearLayout;

/**
 * 
 * @ClassName: Constants
 * @Description:
 * @author Administrator
 * @date 2013年7月31日 下午5:14:46
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class Constants {
	/**************************** xml *****************************************************/
//	 public static String CONFIG_PATH =
//	 "mnt/sdcard/SmartMobile_cz/data/xml/config.xml";
//	 public static String CONFIG_MAP_PATH =
//	 "mnt/sdcard/SmartMobile_cz/data/xml/config_map.xml";
	public static String CONFIG_PATH = "config.xml";
	public static String CONFIG_MAP_PATH = "config_map.xml";
	public static String GHField_PATH = "GHField.plist";
	public static String SDCachePath = "mnt/sdcard/SmartMobile_cz";
	public static String MapCachePath = SDCachePath + "/cache/layers";
	public static String TPKCachePath = SDCachePath + "/cache/tpk";
	public static String SDBPath = SDCachePath + "/data/db/db.sqlite";
//	public static String SDBPath = SDCachePath + "/data/db/test_guotu.sqlite";
	public static String DBPath = SDCachePath + "/data/db/db.db";

	public static String IMGPath = SDCachePath + "/data/image/";
	public static String ScreenImagePath = SDCachePath
			+ "/data/image/screen_image";
	// Environment.getExternalStorageDirectory()

	public static Context CONTEXT;
	public static int MAIN_LEFT_MENU_WIDTH;
	public static int MAIN_RIGHT_MENU_WIDTH;
	public static int WINDOW_WIDTH;
	public static int WINDOW_HIGHT;

	public static int MapType = 0;

	/**************************** GPS ************************************************************************/
	public static int GpsScanType = 0;// 1:tiam;2:meter
	public static int GPSScanSpan = 30000;// 1000*10

	/******************************* Titlecache **********************************************************/
	public static double offset_x;
	public static double offset_y;
	public static int TileCacheType = 1;
	public static LinearLayout ll_right_menu;
	public static LinearLayout ll_left_menu;
	public static LinearLayout ll_main_list_left;
	public static List<CatalogBean> catalogs = null;
	/******************************* Handle what **********************************************************/
	public static Handler HANDLER_TOOL;
	public static Handler HANDLER_MAIN;
	public static Handler HANDLER_LEFT_LIST;
	public static Handler HANDLER_POP_MEDIA;
	public static final int REFRESH_PLOTTING = 1;
	public static final int WHAT_REFRESH_ITEM_CLICK = 2;
	public static final int WHAT_REFRESH_SCALE_LEVER = 3;
	public static final int WHAT_CLEAR_ALL_GRAPGICSLAYER = 4;
	public static final int WHAT_LEFT_SCALE_VISIBLE = 5;
	public static final int WHAT_LEFT_SCALE_INVISIBLE = 6;
	public static final int WHAT_CONFIG_FINISHED = 8;
	public static final int WHAT_MAIN_TAKE_PHOTO = 9;
	public static final int WHAT_POP_MEDIA_REFRESH = 10;
	public static final int WHAT_LEFT_LIST_CLEAR_ALL_DATA = 11;
	public static final int GPSLocation = 17;
}
