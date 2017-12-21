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

		Cache cache = new Cache(Constants.PATH_CACHE, CACHE_MAX_SIZE);

		Interceptor cacheInterceptor = chain -> {
			Request request = chain.request();
			if (!NetworkUtils.isConnected()) {
				request = request.newBuilder()
						.cacheControl(CacheControl.FORCE_CACHE)
						.build();
			}
			Response response = chain.proceed(request);
			if (!NetworkUtils.isConnected()) {
				int maxAge = 0;
				// 有网络时, 不使用缓存, 最大保存时长为0
				response.newBuilder()
						.header("Cache-Control", "public, max-age=" + maxAge)
						.removeHeader("Pragma")
						.build();
			} else {
				// 无网络时，设置缓存超时为2周
				int maxStale = 60 * 60 * 24 * 14;
				response.newBuilder()
						.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
						.removeHeader("Pragma")
						.build();
			}
			return response;
		};

		//每个请求都添加shopId
		Interceptor token = chain -> {

			Request request = chain.request();

			//			String shopId = User.getUserConfig().shop_id;

			HttpUrl originalHttpUrl = request.url();

			HttpUrl.Builder urlBuild = originalHttpUrl.newBuilder();
			/*if (!StringUtil.isEmpty(shopId) && User.getUserConfig().isLogin) {    //判断是否登录过
				urlBuild.addQueryParameter(User.SHOP_ID, shopId); //登录过的话就统一在请求中加入shop_id
				urlBuild.addQueryParameter("version", AppUtils.getAppVersionCode() + ""); //版本标示
				urlBuild.addQueryParameter("system_version", String.valueOf(Build.VERSION.SDK_INT)); //设备系统版本号
			}*/

			HttpUrl url = urlBuild.build();

			LogUtil.i("requestUrl>>>" + url);
		  /*  request = request.newBuilder()
					.addHeader(StringUtil.isEmpty(shopId) ? "" : "shop_id", shopId)
                    .build();*/

			Request.Builder requestBuilder = request.newBuilder()
					.url(url);
			return chain.proceed(requestBuilder.build());
		};
		builder.addNetworkInterceptor(cacheInterceptor);
		builder.addInterceptor(cacheInterceptor);
		builder.addInterceptor(token);
		builder.cache(cache);
		//设置超时
		builder.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
		builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
		builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
		//错误重连
		builder.retryOnConnectionFailure(true);
		okHttpClient = builder.build();
	}

}