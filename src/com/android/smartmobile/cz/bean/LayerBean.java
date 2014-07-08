package com.android.smartmobile.cz.bean;

public class LayerBean {

	private String name = null;
	private String downloadable = null;
	private String SSID = null;
	private String unit = null;
	private String resolution = null;
	private String scale = null;
	private ExtentBean initialExtent;
	private ExtentBean fullExtent;
	private ImageBean image;
	private OriginPoint originPoint;
	private LODInfo lodInfo;
	private LevelBean level;
	private String tpk=null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDownloadable() {
		return downloadable;
	}

	public void setDownloadable(String downloadable) {
		this.downloadable = downloadable;
	}

	public String getSSID() {
		return SSID;
	}

	public void setSSID(String sSID) {
		SSID = sSID;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public ExtentBean getInitialExtent() {
		return initialExtent;
	}

	public void setInitialExtent(ExtentBean initialExtent) {
		this.initialExtent = initialExtent;
	}

	public ExtentBean getFullExtent() {
		return fullExtent;
	}

	public void setFullExtent(ExtentBean fullExtent) {
		this.fullExtent = fullExtent;
	}

	public ImageBean getImage() {
		return image;
	}

	public void setImage(ImageBean image) {
		this.image = image;
	}

	public OriginPoint getOriginPoint() {
		return originPoint;
	}

	public void setOriginPoint(OriginPoint originPoint) {
		this.originPoint = originPoint;
	}

	public LODInfo getLodInfo() {
		return lodInfo;
	}

	public void setLodInfo(LODInfo lodInfo) {
		this.lodInfo = lodInfo;
	}

	public LevelBean getLevel() {
		return level;
	}

	public void setLevel(LevelBean level) {
		this.level = level;
	}

	public String getTpk() {
		return tpk;
	}

	public void setTpk(String tpk) {
		this.tpk = tpk;
	}

	
}
