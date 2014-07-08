package com.android.smartmobile.cz.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import jsqlite.Exception;

import com.android.smartmobile.cz.R.color;
import com.android.smartmobile.cz.bean.LegendBean;
import com.android.smartmobile.cz.bean.SubLayer;
import com.android.smartmobile.cz.db.SDBHelper;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.model.MyApplication;
import com.esri.core.map.Legend;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class InitConfigAsyncTask extends
		AsyncTask<List<SubLayer>, Void, Hashtable<String, List<LegendBean>>> {
	ProgressDialog pdialog;
	Context mContext;
	SDBHelper mSdbHelper;

	public InitConfigAsyncTask(Context context, SDBHelper sdbHelper) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mSdbHelper = sdbHelper;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pdialog = new ProgressDialog(mContext);
		pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdialog.setMessage("系统初始化...");
		pdialog.show();
	}

	@Override
	protected Hashtable<String, List<LegendBean>> doInBackground(
			List<SubLayer>... params) {
		if (params[0].size() == 0)
			return null;
		Hashtable<String, List<LegendBean>> hashtable = new Hashtable<String, List<LegendBean>>();
		for (SubLayer subLayer : params[0]) {
			hashtable.put(subLayer.getName(), getLegend(subLayer));
		}
		Hashtable<String, Hashtable<String, Integer>> hashtable2 = getColorTableWithName(hashtable);
		MyApplication.mApp.colortable = hashtable2;
		Constants.HANDLER_MAIN.sendEmptyMessage(Constants.WHAT_CONFIG_FINISHED);
		return hashtable;
	}

	@Override
	protected void onPostExecute(Hashtable<String, List<LegendBean>> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (pdialog.isShowing()) {
			pdialog.dismiss();
		}
		MyApplication.mApp.lHashtable = result;

	}

	public Hashtable<String, Hashtable<String, Integer>> getColorTableWithName(
			Hashtable<String, List<LegendBean>> hashtable) {
		Hashtable<String, Hashtable<String, Integer>> hashtable2 = new Hashtable<String, Hashtable<String, Integer>>();
		for (Iterator it = hashtable.keySet().iterator(); it.hasNext();) {
			// 从ht中取
			String key = (String) it.next();
			hashtable2.put(key, getColorTable(hashtable.get(key)));
		}

		return hashtable2;

	}

	public Hashtable<String, Integer> getColorTable(List<LegendBean> legendBeans) {
		Hashtable<String, Integer> hashtable = new Hashtable<String, Integer>();
		for (LegendBean legendBean : legendBeans) {
			hashtable.put(legendBean.getName(), legendBean.getInitColor());
		}
		return hashtable;

	}

	/**
	 * 通过subLayer 得到图例列表
	 * 
	 * @param subLayer
	 * @return
	 */
	public List<LegendBean> getLegend(SubLayer subLayer) {
		List<LegendBean> mList = new ArrayList<LegendBean>();
		try {
			String cum = "SELECT \"" + subLayer.getSymbol() + "\" FROM \""
					+ subLayer.getTableName() + "\" GROUP BY "
					+ subLayer.getSymbol() + " LIMIT 30";
			List<String> list = mSdbHelper.getResultByCum(cum);
			LegendBean legendBean = null;
			for (int i = 0; i < list.size(); i++) {
				legendBean = new LegendBean();
				legendBean.setName(list.get(i));
				switch (i) {
				case 0:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.coral));
					break;
				case 1:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.cornflowerblue));
					break;
				case 2:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.chartreuse));
					break;
				case 3:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.aquamarine));
					break;
				case 4:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.cadetblue));
					break;
				case 5:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.cyan));
					break;
				case 6:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.bisque));
					break;

				case 7:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.black));
					break;
				case 8:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.blanchedalmond));
					break;
				case 9:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.blueviolet));
					break;
				case 10:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.brown));
					break;
				case 11:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.burlywood));
					break;
				case 12:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darkorchid));
					break;
				case 13:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.chartreuse));
					break;
				case 14:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.chocolate));
					break;
				case 15:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.aliceblue));
					break;
				case 16:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.antiquewhite));
					break;

				case 17:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.azure));
					break;
				case 18:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.crimson));
					break;
				case 19:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.cyan));
				case 20:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darkblue));
					break;
				case 21:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darkcyan));
					break;
				case 22:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darkgoldenrod));
					break;
				case 23:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darkgray));
					break;
				case 24:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darkkhaki));
					break;
				case 25:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darkmagenta));
					break;
				case 26:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darkolivegreen));
					break;

				case 27:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darkorange));
					break;
				case 28:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darkorchid));
					break;
				case 29:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darkred));
					break;

				default:
					legendBean.setInitColor(mContext.getResources().getColor(
							color.darksalmon));
					break;
				}
				mList.add(legendBean);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mList;

	}

}
