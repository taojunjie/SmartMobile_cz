/**
 * 
 */
package com.android.smartmobile.cz.bean;

import java.util.List;

import android.R.integer;

/**
 * 
 * @ClassName: Module
 * @Description:
 * @author Administrator
 * @date 2013年8月1日 下午1:44:33
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class ModulesBean {
	private int width;
	private int height;
	private List<ModuleBean> list;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public List<ModuleBean> getList() {
		return list;
	}

	public void setList(List<ModuleBean> list) {
		this.list = list;
	}

}
