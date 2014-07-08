package com.android.smartmobile.cz.bean;

import java.util.List;

public class TitleBean {
	String name = "";
	String geoType = "";
	List<AddressBean> groupLayers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGeoType() {
		return geoType;
	}

	public void setGeoType(String geoType) {
		this.geoType = geoType;
	}

	public List<AddressBean> getGroupLayers() {
		return groupLayers;
	}

	public void setGroupLayers(List<AddressBean> groupLayers) {
		this.groupLayers = groupLayers;
	}

}
