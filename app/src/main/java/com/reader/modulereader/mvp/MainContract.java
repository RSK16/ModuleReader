package com.reader.modulereader.mvp;

import com.reader.modulereader.entity.OrderDetails;
import com.reader.modulereader.entity.PayInfo;
import com.reader.modulereader.exception.ApiHttpException;
import com.reader.modulereader.http.IBaseView;
import com.reader.modulereader.http.IPresenter;

/**
 * 作者：zhaokang on 2017/12/23 14:01
 * 邮箱：zhaokang@diandian-tech.com
 * 描述：
 */

public interface MainContract {
    interface IMainView extends IBaseView {

        void getBookSuccess();

        void getBookError(ApiHttpException e);

        void getPayInfosSuccess(PayInfo payInfoList);

        void getPayInfosError(ApiHttpException e);

        void getOrderDetailSuccess(OrderDetails orderDetails);

        void getOrderDetailError(ApiHttpException e);



    }

    interface IMainPresenter extends IPresenter {
        void getBook();

        void getPayInfos();
        /**
         * 获取订单详情
         * @param order_id 订单号
         */
        void getOrderDetail(int order_id);

    }
}
