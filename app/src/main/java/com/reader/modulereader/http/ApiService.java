package com.reader.modulereader.http;


import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2017/12/4 15:42   <br/><br/>
 * 描述:	      ${TODO}
 */

public interface ApiService {
	//	获取打印机列表
	String PRINTER_LIST = "printerConfig/queryPrinterListByShopId";


	/**
	 * 获取打印机列表
	 *
	 * @param shop_id
	 */
	@GET(PRINTER_LIST)
	Observable<Response<List<String>>> getPrinterList(@Query("shop_id") int shop_id);


}
