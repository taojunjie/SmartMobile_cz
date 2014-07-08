package com.android.smartmobile.cz.activity;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import com.android.smartmobile.cz.finals.Constants;
import com.android.smartmobile.cz.util.LogUtil;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class GalleryFolderPhotoActivity extends Activity implements
		MediaScannerConnectionClient {
	private String SCAN_PATH;
	private static final String FILE_TYPE = "image/*";
	public static final int REQUEST_BROWSER_PHOTO = 11;
	private File folder;

	private MediaScannerConnection conn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// intent.putExtra("path", mProject.getPhotoDirectoryPath());
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (null == bundle)//
		{
			// 这里是 预览popMore界面;;;;;;;;;;;;;;
			folder = new File(Constants.ScreenImagePath);
			File[] listFiles = folder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					if (filename.endsWith(".jpg") || filename.endsWith(".png")
							|| filename.endsWith(".gif")) {
						return true;
					}
					return false;
				}
			});
			Arrays.sort(listFiles, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.valueOf(f2.lastModified()).compareTo(
							f1.lastModified());
				}
			});

			try {
				// SCAN_PATH = listFiles[0].getCanonicalPath();
				SCAN_PATH = listFiles[0].getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
		// 这里是相册....也需要排序
		{
			LogUtil.LooLog().i("come to the camera view here");
			folder = new File(getIntent().getExtras().getString("path"));
			File[] listFiles = folder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					if (filename.endsWith(".jpg") || filename.endsWith(".png")
							|| filename.endsWith(".gif")) {
						return true;
					}
					return false;
				}
			});

			Arrays.sort(listFiles, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.valueOf(f2.lastModified()).compareTo(
							f1.lastModified());
				}
			});
			try {
				SCAN_PATH = listFiles[0].getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
			LogUtil.LooLog().i("...........................");
			LogUtil.LooLog().i(SCAN_PATH);
			LogUtil.LooLog().i("...........................");
		}
		// uriAllFiles= new Uri[allFiles.length];
		// Uri uri= Uri.fromFile(new
		// File(Environment.getExternalStorageDirectory().toString()+"/yourfoldername/"+allFiles[0]));
		System.out.println(" SCAN_PATH  " + SCAN_PATH);
		startScan();
	}

	private void startScan() {
		Log.d("Connected", "success" + conn);
		if (conn != null) {
			conn.disconnect();
		}
		conn = new MediaScannerConnection(this, this);
		conn.connect();
	}

	@Override
	public void onMediaScannerConnected() {
		Log.d("onMediaScannerConnected", "success" + conn);
		conn.scanFile(SCAN_PATH, FILE_TYPE);
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		try {
			Log.d("onScanCompleted", uri + "success" + conn);
			System.out.println("URI " + uri);
			if (uri != null) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				// Intent intent = new Intent(Intent.ACTION_PICK,
				// android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				// intent = null;
				// Intent i = new Intent(Intent.ACTION_PICK,
				// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//
				// 调用android的图库
				// intent = i;
				intent.setData(uri);
				startActivityForResult(intent, REQUEST_BROWSER_PHOTO);
			}
		} finally {
			conn.disconnect();
			conn = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_BROWSER_PHOTO) {
			System.out.println("reques code is  browser photo");
			GalleryFolderPhotoActivity.this.finish();
		}
	}
}