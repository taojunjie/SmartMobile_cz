/**
 * 
 */
package com.android.smartmobile.cz.bean;

import java.util.List;

/**
 * 
 * @ClassName: Catalog
 * @Description:
 * @author Administrator
 * @date 2013年8月2日 上午10:53:59
 * @Company: www.shdci.com
 * @Copyright: Copyright (c) 2013 All rights reserved.
 * @version
 */
public class CatalogBean {

	String name = "";
	List<GroupLayer> groupLayers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GroupLayer> getGroupLayers() {
		return groupLayers;
	}

	public void setGroupLayers(List<GroupLayer> groupLayers) {
		this.groupLayers = groupLayers;
	}

}
