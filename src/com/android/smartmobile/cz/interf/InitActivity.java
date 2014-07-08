/**
 * 
 */
package com.android.smartmobile.cz.interf;

/**
 * 
 * @ClassName: InitActivity
 * @Description:
 * @author Administrator
 * @date 2013年7月31日 下午1:50:54
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public interface InitActivity {

	/**
	 * 初始化数据
	 */
	void initData();

	/**
	 * 初始化控件
	 */
	void initView();

	/**
	 * 清理界面
	 */
	void clearView();

}
