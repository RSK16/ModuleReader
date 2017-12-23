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
	File PATH_CACHE = new File(MyApplication.getInstance().getCacheDir(), "dd_cache");
	String BASE_URL = "http://dev.diandianwaimai.com:8887/dd_next_pos/";

	interface SP {
		String SP_FILE_NAME = "dd_pos";
	}
}
