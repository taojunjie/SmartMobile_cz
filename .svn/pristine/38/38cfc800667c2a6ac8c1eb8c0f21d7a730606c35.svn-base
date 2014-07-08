package com.android.smartmobile.cz.map; 

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.finals.Constants;
import com.esri.android.map.GroupLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnZoomListener;
import com.smartmobile.sdk.map.MapServiceSpatialType;
import com.smartmobile.sdk.map.TianDiTuTiledMapServiceLayer;
import com.smartmobile.sdk.map.TianDiTuTiledMapServiceType;

/**
 * 
* @ClassName:   VectorPhotoLayer
* @Description: 地图切换矢量和影像
* @author        
* @date         2013-7-31 上午9:22:35
* @Company:     www.shdci.com 
* @Copyright:   Copyright (c) 2013  All rights reserved.
 */
public class VectorPhotoLayer {
	
	TianDiTuTiledMapServiceLayer t_vec;
	TianDiTuTiledMapServiceLayer t_cva;
	TianDiTuTiledMapServiceLayer t_vec_c;
	TianDiTuTiledMapServiceLayer t_cva_c;
	
	public VectorPhotoLayer() { 
		 
	}

	static VectorPhotoLayer mMapGouplayer;

	public static VectorPhotoLayer getInstance() {
		if (mMapGouplayer == null) {
			synchronized (VectorPhotoLayer.class) {
				if (mMapGouplayer == null) {
					mMapGouplayer = new VectorPhotoLayer();
				}
			}
		}
		return mMapGouplayer;
	}
  

	public void showPhotoMap(MapView mapView) {
		Layer[] layers = mapView.getLayers();
		boolean containmap = false;

		for (Layer layer : layers) {
			if (layer instanceof GroupLayer) {
				GroupLayer mapGroupLayer = (GroupLayer) layer;
				mapView.removeLayer(mapGroupLayer);
				GroupLayer photoMapGroupLayer = new GroupLayer();
				photoMapGroupLayer.setName("photoMapGroupLayer");
				t_vec = new TianDiTuTiledMapServiceLayer(
						TianDiTuTiledMapServiceType.IMG_C,
						MapServiceSpatialType.WKID_WGS84,
						Constants.MapCachePath);
				photoMapGroupLayer.addLayer(t_vec);

				t_cva = new TianDiTuTiledMapServiceLayer(
						TianDiTuTiledMapServiceType.CIA_C,
						MapServiceSpatialType.WKID_WGS84,
						Constants.MapCachePath);
				photoMapGroupLayer.addLayer(t_cva);
				mapView.addLayer(photoMapGroupLayer, 0);
				containmap = true;
				break;
			}
		}
		if (!containmap) {
			GroupLayer photoMapGroupLayer = new GroupLayer();

			t_vec = new TianDiTuTiledMapServiceLayer(
					TianDiTuTiledMapServiceType.IMG_C,
					MapServiceSpatialType.WKID_WGS84, Constants.MapCachePath);
			photoMapGroupLayer.addLayer(t_vec);

			t_cva = new TianDiTuTiledMapServiceLayer(
					TianDiTuTiledMapServiceType.CIA_C,
					MapServiceSpatialType.WKID_WGS84, Constants.MapCachePath);
			photoMapGroupLayer.addLayer(t_cva);

			photoMapGroupLayer.setName("photoMapGroupLayer");
			mapView.addLayer(photoMapGroupLayer, 0);
		}
		mapView.setOnZoomListener(new OnZoomListener() {

			@Override
			public void preAction(float paramFloat1, float paramFloat2,
					double paramDouble) {
				// TODO Auto-generated method stub
				// 缩放后
				// map_tidiOld2.clearTiles();
				// map_tidiOld2.refresh();
			}

			@Override
			public void postAction(float paramFloat1, float paramFloat2,
					double paramDouble) {
				// TODO Auto-generated method stub
				// 缩放前 防止标注重叠
				// map_tidiOld2.clearTiles();
				if(t_cva.getCurrentLevel()<18){
					t_cva.refresh();
				}
				 
			}
		});

	}


	public void showVectorMap(MapView mapView) {
		Layer[] layers = mapView.getLayers();
		boolean containmap = false;
		for (Layer layer : layers) {
			if (layer instanceof GroupLayer) {
				GroupLayer mapGroupLayer = (GroupLayer) layer;
				mapView.removeLayer(mapGroupLayer);
				GroupLayer vectorMapGroupLayer = new GroupLayer();
				vectorMapGroupLayer.setName("vectorMapGroupLayer");

				t_vec_c = new TianDiTuTiledMapServiceLayer(
						TianDiTuTiledMapServiceType.VEC_C,
						MapServiceSpatialType.WKID_WGS84,
						Constants.MapCachePath);
				vectorMapGroupLayer.addLayer(t_vec_c);

				t_cva_c = new TianDiTuTiledMapServiceLayer(
						TianDiTuTiledMapServiceType.CVA_C,
						MapServiceSpatialType.WKID_WGS84,
						Constants.MapCachePath);
				vectorMapGroupLayer.addLayer(t_cva_c);
				
				mapView.addLayer(vectorMapGroupLayer, 0);
				containmap = true;
				break;
			}
		}
		if (!containmap) {
			GroupLayer vectorMapGroupLayer = new GroupLayer();
			t_vec_c = new TianDiTuTiledMapServiceLayer(
					TianDiTuTiledMapServiceType.VEC_C,
					MapServiceSpatialType.WKID_WGS84, Constants.MapCachePath);
			vectorMapGroupLayer.addLayer(t_vec_c);
			t_cva_c = new TianDiTuTiledMapServiceLayer(
					TianDiTuTiledMapServiceType.CVA_C,
					MapServiceSpatialType.WKID_WGS84, Constants.MapCachePath);
			vectorMapGroupLayer.addLayer(t_cva_c);
			vectorMapGroupLayer.setName("vectorMapGroupLayer");
			mapView.addLayer(vectorMapGroupLayer, 0);
		}
		mapView.setOnZoomListener(new OnZoomListener() {

			@Override
			public void preAction(float paramFloat1, float paramFloat2,
					double paramDouble) {
				// TODO Auto-generated method stub
				// 缩放后
				// map_tidiOld2.clearTiles();
				// map_tidiOld2.refresh();
			}

			@Override
			public void postAction(float paramFloat1, float paramFloat2,
					double paramDouble) {
				// TODO Auto-generated method stub
				// 缩放前 防止标注重叠
				// map_tidiOld2.clearTiles();
				if(t_cva_c.getCurrentLevel()<18){
					t_cva_c.refresh();
				}
			}
		});
	}
	
}
