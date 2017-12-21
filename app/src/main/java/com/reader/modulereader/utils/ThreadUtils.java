package com.reader.modulereader.utils;

import android.os.Looper;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2017/12/1 10:41   <br/><br/>
 * 描述:	      ${TODO}
 */
public class ThreadUtils {
	public static boolean isInMainThread() {
		Looper myLooper = Looper.myLooper();
		Looper mainLooper = Looper.getMainLooper();
		return myLooper == mainLooper;
	}
}
