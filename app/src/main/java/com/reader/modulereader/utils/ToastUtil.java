package com.reader.modulereader.utils;

import android.widget.Toast;

import com.reader.modulereader.MyApplication;


/**
 * 创建者     金国栋              <br/><br/>
 * 创建时间   2016/09/22 14:37       <br/><br/>
 * 描述	     吐司工具类
 */
public class ToastUtil {

	/**
	 * 弹出吐司-时间稍短
	 *
	 * @param str
	 */
	public static Toast toastS(String str) {
		Toast toast = Toast.makeText(MyApplication.getInstance(), str, Toast.LENGTH_SHORT);
		toast.show();
		return toast;
	}

	/**
	 * 弹出吐司-时间稍长
	 *
	 * @param str
	 */
	public static Toast toastL(String str) {
		Toast toast = Toast.makeText(MyApplication.getInstance(), str, Toast.LENGTH_LONG);
		toast.show();
		return toast;
	}

	/**
	 * 消失吐司
	 */
	public static void cancelToast(Toast toast) {
		if (toast != null)
			toast.cancel();
	}
}
