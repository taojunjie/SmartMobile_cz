package com.android.smartmobile.cz.bean;

public class AppConfigInfo {
	private String ApplicationTitle = null;
	private String serviceUrl = null;
	private String maplogo = null;
	private ExtentBean extent;
	private String resolution = null;
	private String scale = null;
	private String exploreUrl = null;

	public String getApplicationTitle() {
		return ApplicationTitle;
	}

	public void setApplicationTitle(String applicationTitle) {
		ApplicationTitle = applicationTitle;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getMaplogo() {
		return maplogo;
	}

	public void setMaplogo(String maplogo) {
		this.maplogo = maplogo;
	}

	public ExtentBean getExtent() {
		return extent;
	}

	public void setExtent(ExtentBean extent) {
		this.extent = extent;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getExploreUrl() {
		return exploreUrl;
	}

	public void setExploreUrl(String exploreUrl) {
		this.exploreUrl = exploreUrl;
	}

	
	
}
