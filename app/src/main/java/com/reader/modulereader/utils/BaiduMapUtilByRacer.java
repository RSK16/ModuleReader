package com.reader.modulereader.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.reader.modulereader.entity.LocationBean;


/**
 * @ClassName: BaiduMapUtilByRacer
 * @Description: 百度地圖工具類
 * @author 李焕儿
 * @date 2014-10-31 下午2:38:44
 * 
 */
public class BaiduMapUtilByRacer {

	public static LocationClient mLocationClient = null;
	public static LocationClientOption option = null;
	public static LocateListener mLocateListener = null;
	public static MyLocationListenner mMyLocationListenner = null;
	public static int locateTime = 1000;

	public interface LocateListener {
		void onLocateSucceed(LocationBean locationBean);

		void onLocateFiled();

		void onLocating();
	}

	/**
	 * @Title: startLocate
	 * @Description: 定位
	 * @param mContext
	 * @param time
	 *            大於1000既會間隔定位
	 * @param listener
	 * @return void
	 * @throws
	 */
	public static void locateByBaiduMap(Context mContext, int time,
										LocateListener listener) {
		mLocateListener = listener;
		locateTime = time;
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(mContext);
		}
		if (mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
		if (mMyLocationListenner == null) {
			mMyLocationListenner = new MyLocationListenner();
		}
		mLocationClient.registerLocationListener(mMyLocationListenner);
		if (option == null) {
			option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(time);
			option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		}
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}


	public static class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location.getLocType()!=161) {
//				ToastUtil.toastS(location.getLocType()+"");
				return;
			}
			LocationBean mLocationBean = new LocationBean();
			mLocationBean.setLatitude(location.getLatitude());
			mLocationBean.setLongitude(location.getLongitude());
			if (mLocateListener != null) {
				mLocateListener.onLocateSucceed(mLocationBean);
			}
		}

	}

	public static void stopAndDestroyLocate() {
		if (mLocationClient != null) {
			if (mMyLocationListenner != null) {
				mLocationClient
						.unRegisterLocationListener(mMyLocationListenner);
			}
			mLocationClient.stop();
		}
		mMyLocationListenner = null;
		mLocateListener = null;
		mLocationClient = null;
		option = null;
	}

}
