package com.android.smartmobile.cz.thread;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;
import android.content.Context;
import android.os.AsyncTask;

public class AsyncQueryTask extends AsyncTask<String, Void, FeatureSet> {
	Context mContext;

	public AsyncQueryTask() {
		// TODO Auto-generated constructor stub
	}

	protected void onPreExecute() {
		// 在未查询出结果时显示一个进度条

	}

	protected FeatureSet doInBackground(String... queryParams) {
		if (queryParams == null || queryParams.length <= 1)
			return null;
		// 查询条件和URL参数
		String url = queryParams[0];
		// 查询所需的参数类
		Query query = new Query();
		String whereClause = queryParams[1];
		SpatialReference sr = SpatialReference.create(102100);
		query.setGeometry(new Envelope(-20147112.9593773, 557305.257274575,
				-6569564.7196889, 11753184.6153385));// 设置查询空间范围
		query.setOutSpatialReference(sr);// 设置查询输出的坐标系
		query.setReturnGeometry(true);// 是否返回空间信息
		query.setWhere(whereClause);// where条件

		QueryTask qTask = new QueryTask(url);// 查询任务类
		FeatureSet fs = null;

		try {
			fs = qTask.execute(query);// 执行查询，返回查询结果
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fs;
		}
		return fs;

	}

	protected void onPostExecute(FeatureSet result) {

		String message = "No result comes back";
		if (result != null) {
			Graphic[] grs = result.getGraphics();

			if (grs.length > 0) {
				// gl.addGraphics(grs); // 将查询结果添加到图层上
				message = (grs.length == 1 ? "1 result has " : Integer
						.toString(grs.length) + " results have ")
						+ "come back";
			}

		}

	}

}
