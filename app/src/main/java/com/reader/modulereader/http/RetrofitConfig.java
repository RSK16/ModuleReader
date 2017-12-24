package com.reader.modulereader.http;


import com.blankj.utilcode.util.NetworkUtils;
import com.reader.modulereader.Constants;
import com.reader.modulereader.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2016/9/21 10:44   <br/><br/>
 * 描述:	      ${TODO}
 */
public class RetrofitConfig {

	public static final int TIMEOUT = 10000;
	public static final int READ_TIMEOUT = 20000;
	public static final int WRITE_TIMEOUT = 20000;

	/**
	 * 缓存最大值
	 */
	public static final int CACHE_MAX_SIZE = 1024 * 1024 * 30;

	private OkHttpClient okHttpClient;

	public RetrofitConfig() {
		init();
	}

	private void init() {
		initOkhttp();
	}

	public ApiService getApiService() {
		return new Retrofit.Builder()
				.baseUrl(Constants.BASE_URL)
				.client(okHttpClient)
				.addConverterFactory(GsonConverterFactory.create(GsonAdapter.getGson()))
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.build()
				.create(ApiService.class);
	}

	private void initOkhttp() {

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		//设置超时
		builder.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
		builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
		builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
		//错误重连
		builder.retryOnConnectionFailure(true);
		okHttpClient = builder.build();
	}


}