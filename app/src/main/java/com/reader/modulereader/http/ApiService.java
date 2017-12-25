package com.reader.modulereader.http;


import com.reader.modulereader.entity.BaseCourse;
import com.reader.modulereader.entity.Course;
import com.reader.modulereader.entity.Notice;
import com.reader.modulereader.entity.Response;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2017/12/4 15:42   <br/><br/>
 * 描述:	      ${TODO}
 */

public interface ApiService {

	//	获取课程信息
	String GET_COURSE_DETAILS = "GetCourseJsonServlet";

	//获取通知信息（返回值为json）
	String GET_NOTICE_DETAILS = "GetNoticeJsonServlet";

	//添加经度和纬度
	String ADD_LNGLAT = "AddlnglatJsonServlet";

	//获取课程信息（返回值为json）
	String GET_BASE_COURSE_DETAILS = "GetBaseCourseJsonServlet";

	@GET(GET_COURSE_DETAILS)
	Observable<Course> getCourseJsonServlet();

	@GET(GET_NOTICE_DETAILS)
	Observable<Notice> getNoticeJsonServlet();

	@FormUrlEncoded
	@POST(ADD_LNGLAT)
	Observable<Response> addlnglatJsonServlet(@Field("lng") String lng, @Field("lat") String lat);


	@GET(GET_BASE_COURSE_DETAILS)
	Observable<BaseCourse> getBaseCourseJsonServlet();

}
