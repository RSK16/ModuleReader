package com.reader.modulereader.utils;

import android.util.Log;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:    2016/09/21 10:15 <br/><br/>
 * 描述:	      日志工具类
 */
public final class LogUtil {

	private static final int DEBUG = 1;
	private static final int INFO = 2;
	private static final int WARN = 3;
	private static final int ERROR = 4;
	private static final int ALL = 5;
	private static final int LOWEST = 0;
	//	private static final int LEVEL = DDApplication.getInstance().isApkDebugable() ? ALL : LOWEST;  //debug模式打印日志， release模式不打印
	private static final int LEVEL = ALL;
	public static final String DD_TAG = "DDTag_BossApp";

	public static void d(String msg, String tag) {
		if (LEVEL >= DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void d(String msg) {
		d(msg, DD_TAG);
	}

	public static void i(String msg, String tag) {
		if (LEVEL >= INFO) {
			Log.i(tag, msg);
		}
	}

	public static void i(String msg) {
		i(msg, DD_TAG);
	}

	public static void w(String msg, String tag) {
		if (LEVEL >= WARN) {
			Log.w(tag, msg);
		}
	}

	public static void w(String msg) {
		w(msg, DD_TAG);
	}

	public static void e(String msg, String tag) {
		if (LEVEL >= ERROR) {
			Log.e(tag, msg);
		}
	}

	public static void e(String msg) {
		e(msg, DD_TAG);
	}
}
