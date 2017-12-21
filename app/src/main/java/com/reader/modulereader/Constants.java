package com.reader.modulereader;

import java.io.File;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2017/11/30 15:13   <br/><br/>
 * 描述:	      ${TODO}
 */
public interface Constants {
	int SUCCESS = 0;
	int ERROR = 1;
	int EMPTY = 2;
	int LOADING = 3;
	long SHOW_VIEW_OFFSET = 500;
	String LOG_DIR_NAME = "dd_error_log";
	String LOGIN_SHOP_ID = "login_shop_id";
	String SHOP_ID = "shop_id";
	String SHOP_KEY = "shop_key";
	File PATH_CACHE = new File(MyApplication.getInstance().getCacheDir(), "dd_cache");
	String BASE_URL = "http://dev.diandianwaimai.com:8887/dd_next_pos/";
	/**
	 * 自动提示更新时差
	 */
	long UPDATE_TIME = 60 * 60 * 8 * 1000;
	/**
	 * 登录名
	 */
	String LOGIN_NAME = "login_name";
	String SHOP_NAME = "shop_name";
	String TOKEN = "token";
	String IS_LOGIN = "is_login";
	String FOOD_CACHE = "food_cache";
	String FOOD_CACHE_NAME = "pos_food_cache";

	interface SP {
		String SP_FILE_NAME = "dd_pos";
	}
}
