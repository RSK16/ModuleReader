package com.reader.modulereader;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.reader.modulereader.utils.EventBusUtils;
import com.reader.modulereader.utils.MessageEvent;

/**
 * Created by 赵康 on 2017/12/24.
 */

public class MyLocationListener implements LocationListener {
    @Override// 位置的改变
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if (location != null) {
            double latitude = location.getLatitude();// 维度
            double longitude = location.getLongitude();// 经度
            EventBusUtils.post(new MessageEvent(longitude+"",latitude+""));
            //显示当前坐标
//            ToastUtil.toastS("real_location:(" + latitude + "," + longitude + ")");
        } else {
//            ToastUtil.toastS("real_location:(null)");
        }
    }

    @Override// gps卫星有一个没有找到
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
//        ToastUtil.toastS("statusChanged=)"+ status);
    }

    @Override// 某个设置被打开
    public void onProviderEnabled(String provider) {
//        ToastUtil.toastS("real_location:(onProviderEnabled)");
        // TODO Auto-generated method stub
    }

    @Override// 某个设置被关闭
    public void onProviderDisabled(String provider) {
//        ToastUtil.toastS("real_location:(onProviderDisabled)");
        // TODO Auto-generated method stub
    }


}
