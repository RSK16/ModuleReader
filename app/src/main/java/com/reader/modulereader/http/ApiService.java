package com.reader.modulereader.http;


import com.reader.modulereader.entity.Course;
import com.reader.modulereader.entity.Notice;
import com.reader.modulereader.entity.OrderDetails;
import com.reader.modulereader.entity.PayInfo;
import com.reader.modulereader.entity.Response;
import com.reader.modulereader.utils.StringUtil;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2017/12/4 15:42   <br/><br/>
 * 描述:	      ${TODO}
 */

public interface ApiService {

	String GETPAYINFOS = "paytype/getInfos"; //接口用于获取支付方式

	//	获取订单详情
	String GETORDER_DETAILS = "order/getOrderDetail";

	//	获取课程信息
	String GET_COURSE_DETAILS = "GetCourseJsonServlet";

	//获取通知信息（返回值为json）
	String GET_NOTICE_DETAILS = "GetNoticeJsonServlet";

	//添加经度和纬度
	String ADD_LNGLAT = "AddlnglatJsonServlet";

	/**
	 * 获取商家支付信息
	 * @param shop_id
	 * @return
	 */
	@GET(GETPAYINFOS)
	Observable<PayInfo> getPayInfos(@Query("shop_id") String shop_id);

	/**
	 * 获取订单详情
	 *
	 * @param order_id 订单号
	 */
	@GET(GETORDER_DETAILS)
	Observable<Response<OrderDetails>> getOrderDetail(@Query("order_id") int order_id);


	@GET(GET_COURSE_DETAILS)
	Observable<Course> getCourseJsonServlet();

	@GET(GET_NOTICE_DETAILS)
	Observable<Notice> getNoticeJsonServlet();

	@FormUrlEncoded
	@POST(ADD_LNGLAT)
	Observable<Response> addlnglatJsonServlet(@Field("lng") String lng, @Field("lat") String lat);

}
