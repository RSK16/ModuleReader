package com.reader.modulereader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.reader.modulereader.Constants;
import com.reader.modulereader.MyApplication;

/**
 * 创建者     金国栋                  <br/><br/>
 * 创建时间    2016/09/21 10:18        <br/><br/>
 * 描述	     SharedPreferences工具类
 */
public class SpUtil {
	/**
	 * 获取全局上下文对象
	 *
	 * @return Context
	 */
	public static Context getContext() {
		return MyApplication.getInstance();
	}

	/**
	 * 获取Resources
	 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	/**
	 * 存储String类型数据
	 *
	 * @param key
	 * @param value
	 */
	public static void putString(String key, String value) {
		getDefaultSPobj().edit().putString(key, value).apply();
	}

	/**
	 * 存储int类型数据
	 *
	 * @param key
	 * @param value
	 */
	public static void putInt(String key, int value) {
		getDefaultSPobj().edit().putInt(key, value).apply();
	}

	/**
	 * 存储boolean类型数据
	 *
	 * @param key
	 * @param value
	 */
	public static void putBoolean(String key, boolean value) {
		getDefaultSPobj().edit().putBoolean(key, value).apply();
	}

	/**
	 * 存储long类型数据
	 *
	 * @param key
	 * @param value
	 */
	public static void putLong(String key, long value) {
		getDefaultSPobj().edit().putLong(key, value).apply();
	}

	/**
	 * 获取String类型数据
	 *
	 * @param key
	 * @param def 默认值
	 * @return String
	 */
	public static String getString(String key, String def) {
		return getDefaultSPobj().getString(key, def);
	}

	/**
	 * 获取int类型数据
	 *
	 * @param key
	 * @param def 默认值
	 * @return int
	 */
	public static int getInt(String key, int def) {
		return getDefaultSPobj().getInt(key, def);
	}

	/**
	 * 获取boolean类型数据
	 *
	 * @param key
	 * @param def 默认值
	 * @return boolean
	 */
	public static boolean getBoolean(String key, boolean def) {
		return getDefaultSPobj().getBoolean(key, def);
	}

	/**
	 * 获取long类型数据
	 *
	 * @param key
	 * @param def 默认值
	 * @return long
	 */
	public static long getLong(String key, long def) {
		return getDefaultSPobj().getLong(key, def);
	}


	/**
	 * 获取默认SharedPrefrences对象
	 *
	 * @return SharedPreferences
	 */
	public static SharedPreferences getDefaultSPobj() {
		return getContext().getSharedPreferences(Constants.SP.SP_FILE_NAME, Context.MODE_PRIVATE);
	}
}
