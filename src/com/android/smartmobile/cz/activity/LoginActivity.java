package com.android.smartmobile.cz.activity;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.smartmobile.cz.R;
import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.interf.InitActivity;
import com.android.smartmobile.cz.model.BaseActivity;
import com.android.smartmobile.cz.model.MyApplication;
import com.android.smartmobile.cz.service.PlistHandler;
import com.android.smartmobile.cz.service.PullParseGHFieldService;
import com.android.smartmobile.cz.util.LogUtil;
import com.android.smartmobile.cz.util.NetWorkUtils;
import com.android.smartmobile.cz.util.ToastUtil;


public class LoginActivity extends BaseActivity implements InitActivity {
	private static final int WHAT_SUCCESS = 0;
	private static final int WHAT_FAIL = 1;
	private static final int WHAT_NO_NETWORK = 2;
	private EditText edit_login_username, edit_login_password;
	private Button btn_login, btn_exit;
	private Context mContext;
	private SharedPreferences sharedPreferences;
	private ProgressDialog progressDialog;
	private String username;
	private String passwd;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_SUCCESS:
				progressDialog.dismiss();
				startActivity(new Intent(mContext, MainActivity.class));
//				startActivity(new Intent(mContext, PEActivity.class));
				finish();
				break;
			case WHAT_FAIL:
				progressDialog.dismiss();
				ToastUtil.makeToastInBottom("用户名或密码错误");
				break;
			case WHAT_NO_NETWORK:
				progressDialog.dismiss();
				ToastUtil.makeToastInBottom("没有网络，请检查网络设置");
				break;
			}
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		mContext = LoginActivity.this;
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initView();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		sharedPreferences = getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		edit_login_username = (EditText) this
				.findViewById(R.id.edit_login_username);
		edit_login_password = (EditText) this
				.findViewById(R.id.edit_login_password);
		btn_login = (Button) this.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				progressDialog = new ProgressDialog(mContext);
				// progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				// progressDialog.setMessage("正在登录，请稍后...");

				progressDialog.show();
				progressDialog
						.setContentView(R.layout.loading_process_dialog_color);
				// http://140.206.130.18:18182/Services/Debug/MetaDataRest.svc/web/SystemLogin/CheckUser?username={USERNAME}&passwd={PASSWD}
				username = edit_login_username.getText().toString();
				passwd = edit_login_password.getText().toString();
				
				handler.sendEmptyMessage(WHAT_SUCCESS);
				//验证线程
				new Thread(new Runnable_save()).start();
				
				
			}
		});

		btn_exit = (Button) this.findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ToastUtil.makeToastInCenter(getPhoneNumber(mContext));
				// finish();

				/*
				 * try { Map<String, List<String>> map = PullParseGHFieldService
				 * .getArrMap(); LogUtil.LooLog().d(map.size()); } catch
				 * (XmlPullParserException e) { // TODO Auto-generated catch
				 * block e.printStackTrace(); } catch (IOException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */

				HashMap<String, Object> hashMap = (HashMap<String, Object>) PullParseGHFieldService
						.getArrByName("规划工程红线");
				LogUtil.LooLog().d(hashMap.size());
				ArrayList<String>  strings=(ArrayList<String>)hashMap.get("DisplayOrder");
				
				LogUtil.LooLog().d(strings.size());
			}
		});
	}

	public static String getPhoneNumber(Context context) {

		TelephonyManager mTelephonyMgr;

		mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return mTelephonyMgr.getLine1Number();

	}
	
	
	private class Runnable_save implements Runnable {
		@Override
		public void run() {
			FileOutputStream fOut = null;
			if (NetWorkUtils.isNetworkAvailable(mContext)) {
			
				try {
					// 在线验证
					
					if (true) {
						handler.sendEmptyMessage(WHAT_FAIL);
					} else if (true) {
						// 验证成功
						Editor editor = sharedPreferences.edit();
						editor.putString("username", username);
						editor.putString("passwd", passwd);
						
						handler.sendEmptyMessage(WHAT_SUCCESS);

					}

				} catch (java.lang.Exception e) {
					e.printStackTrace();
				} finally {
					if (null != fOut) {
						try {
							fOut.flush();
							fOut.close();
						} catch (Exception e) {
						}
					}
				}

			} else {
				// 没有网络，离线验证
				if (sharedPreferences.contains("username")) {
					String usernameString = sharedPreferences.getString(
							"username", "");
					String passwdString = sharedPreferences.getString("passwd",
							"");
					if (username.equalsIgnoreCase(usernameString)
							&& passwd.equalsIgnoreCase(passwdString)) {
						// 验证成功
						handler.sendEmptyMessage(WHAT_SUCCESS);
					} else {
						// 验证失败
						handler.sendEmptyMessage(WHAT_FAIL);
					}
				} else {
					handler.sendEmptyMessage(WHAT_NO_NETWORK);
				}
			}
		}
	};
	

	@Override
	public void clearView() {
		// TODO Auto-generated method stub

	}

}
