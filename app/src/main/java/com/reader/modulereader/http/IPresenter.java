package com.reader.modulereader.http;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2016/9/21 09:14  <br/><br/>
 * 描述:	     Presenter顶级接口
 */
public interface IPresenter {
	/**
	 * 加载网络数据
	 */
	void loadDataFromServer();

	void detachView();
}
