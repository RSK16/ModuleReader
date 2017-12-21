package com.reader.modulereader.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.reader.modulereader.MyApplication;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 作者：zhaokang on 2017/6/16 10:45
 * 邮箱：zhaokang@diandian-tech.com
 * 描述	   获取系统信息
 */
public class SystemUtil {

	/**
	 * 检查WIFI是否连接
	 */
	public static boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) SpUtil.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifiInfo != null;
	}

	/**
	 * 检查手机网络(4G/3G/2G)是否连接
	 */
	public static boolean isMobileNetworkConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) SpUtil.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileNetworkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return mobileNetworkInfo != null;
	}

	/**
	 * 检查是否有可用网络
	 */
	public static boolean isNetworkConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) SpUtil.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getActiveNetworkInfo() != null;
	}

	/**
	 * 保存文字到剪贴板
	 *
	 * @param text
	 */
	public static void copyToClipBoard(String text) {
		ClipData clipData = ClipData.newPlainText("url", text);
		ClipboardManager manager = (ClipboardManager) SpUtil.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
		manager.setPrimaryClip(clipData);
	}

	/**
	 * 获取版本号
	 */
	public static String getVersionName() {
		MyApplication application = MyApplication.getInstance();
		try {
			return "V " + application.getPackageManager().getPackageInfo(application.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取屏幕宽高
	 */
	public static DisplayMetrics getWindowScreen() {

		return SpUtil.getResources().getDisplayMetrics();

	}

	/**
	 * 隐藏软键盘
	 *
	 * @param editText
	 */
	public static void hideSoftInput(EditText editText) {
		InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(
				editText.getWindowToken(), 0);
		editText = null;
	}

	/**
	 * 清除所有通知
	 */
	public static void cleanAllNotification() {
		NotificationManager nm = (NotificationManager) SpUtil.getContext().getSystemService(NOTIFICATION_SERVICE);
		nm.cancelAll();
	}
	/**
	 * 获取软键盘状态
	 *
	 * @param editText
	 */
	public static boolean isHideSoftInputStatus(EditText editText) {
		InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);

		// 获取软键盘的显示状态
		return imm.isActive();
	}

	/**
	 * Dialog中隐藏软键盘
	 * @param activity
	 */
	public static void  HideSoftKeyBoardDialog(Activity activity){
		try{
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
		catch(Exception ex){
		}
	}


}
