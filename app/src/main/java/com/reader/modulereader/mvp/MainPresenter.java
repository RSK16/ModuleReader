package com.reader.modulereader.mvp;

import com.reader.modulereader.entity.OrderDetails;
import com.reader.modulereader.entity.PayInfo;
import com.reader.modulereader.exception.ApiHttpException;
import com.reader.modulereader.http.HttpRequest;
import com.reader.modulereader.http.HttpSubscriber;
import com.reader.modulereader.http.RxPresenter;
import com.reader.modulereader.utils.RxUtil;

/**
 * 作者：zhaokang on 2017/12/23 14:01
 * 邮箱：zhaokang@diandian-tech.com
 * 描述：
 */

public class MainPresenter extends RxPresenter<MainContract.IMainView> implements MainContract.IMainPresenter {

    public MainPresenter(MainContract.IMainView view) {
        super(view);
    }

    @Override
    public void loadDataFromServer() {

    }

    @Override
    public void getBook() {

    }

    @Override
    public void getPayInfos() {
        HttpSubscriber<PayInfo> subscriber = new HttpSubscriber<PayInfo>() {
            @Override
            protected void onError(ApiHttpException exception) {
                view.getPayInfosError(exception);
            }

            @Override
            public void onNext(PayInfo payInfo) {
                if (payInfo.ret_code == 0) {
                    view.getPayInfosSuccess(payInfo);
                } else {
                    onError(new ApiHttpException(payInfo.ret_msg,payInfo.ret_code));
                }
            }
        };
        HttpRequest.getInstance().getPayInfos("20826")
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(subscriber);

        addSubscribe(subscriber);
    }

    @Override
    public void getOrderDetail(int order_id) {
        HttpSubscriber<OrderDetails> httpSubscriber = new HttpSubscriber<OrderDetails>() {
            @Override
            protected void onError(ApiHttpException exception) {
                view.getOrderDetailError(exception);
            }

            @Override
            public void onNext(OrderDetails info) {
                if (info != null && info.ret_code == 0) {
                    view.getOrderDetailSuccess(info);
                } else {
                    view.getOrderDetailError(new ApiHttpException(info.ret_msg, info.ret_code));
                }
            }
        };
        HttpRequest.reSet();
        HttpRequest.getInstance().getOrderDetail(order_id)
                .compose(RxUtil.rxSchedulerHelper())
                .compose(RxUtil.handleResult())
                .subscribe(httpSubscriber);

        addSubscribe(httpSubscriber);
    }
}
