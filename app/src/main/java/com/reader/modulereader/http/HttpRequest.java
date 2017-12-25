package com.reader.modulereader.http;


import com.reader.modulereader.entity.BaseCourse;
import com.reader.modulereader.entity.Course;
import com.reader.modulereader.entity.Notice;
import com.reader.modulereader.entity.Response;

import rx.Observable;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2016/9/21 10:56   <br/><br/>
 * 描述:	      网络请求类
 */
public class HttpRequest {

	private static HttpRequest mHttpRequest;
	private final ApiService mApiService;

	private HttpRequest() {
		mApiService = new RetrofitConfig().getApiService();
	}

	public static HttpRequest getInstance() {
		if (mHttpRequest == null) {
			synchronized (HttpRequest.class) {
				if (mHttpRequest == null)
					mHttpRequest = new HttpRequest();
			}
		}
		return mHttpRequest;
	}

	/**
	 * 登录成功后需要调用此方法重置请求配置
	 */
	public static void reSet() {
		mHttpRequest = null;
	}

	public Observable<Course> getCourseJsonServlet() {
		return mApiService.getCourseJsonServlet();
	}

	public Observable<Notice> getNoticeJsonServlet() {
		return mApiService.getNoticeJsonServlet();
	}

	public Observable<Response> addlnglatJsonServlet(String lng,String lat) {
		return mApiService.addlnglatJsonServlet(lng,lat);
	}

	public Observable<BaseCourse> getBaseCourseJsonServlet() {
		return mApiService.getBaseCourseJsonServlet();
	}
}
