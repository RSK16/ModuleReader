package com.reader.modulereader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

    private List<String> providerNames = new ArrayList<String>();
    private TextView mText;
    private Button active;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mText = (TextView) findViewById(R.id.provName);
        active = (Button) findViewById(R.id.active);

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation(v);
            }
        });
    }

    //实现GPS的方法
    public void gps() {
        //定义LocationManager对象
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //定义Criteria对象
        Criteria criteria = new Criteria();
        // 定位的精准度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 海拔信息是否关注
        criteria.setAltitudeRequired(false);
        // 对周围的事情是否进行关心
        criteria.setBearingRequired(false);
        // 是否支持收费的查询
        criteria.setCostAllowed(true);
        // 是否耗电
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 对速度是否关注
        criteria.setSpeedRequired(false);

        //得到最好的定位方式
        String provider = locationManager.getBestProvider(criteria, true);
        mText.setText("");
        providerNames = locationManager.getAllProviders();

        String locationProvider;
        if(providerNames.contains(LocationManager.GPS_PROVIDER)){
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        }else if(providerNames.contains(LocationManager.NETWORK_PROVIDER)){
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else{
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return ;
        }
        //获取Location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location!=null){
            //不为空,显示地理位置经纬度
            showLocation(location);
        }
//        Iterator<String> it = providerNames.iterator();
//        while (it.hasNext()) {
//            mText.append(it.next() + "\n");
//        }


        //注册监听
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(locationProvider, 100, 0, new MyLocationListener());
    }

    /**
     * 显示地理位置经度和纬度信息
     * @param location
     */
    private void showLocation(Location location){
        String locationStr = "维度：" + location.getLatitude() +"\n"
                + "经度：" + location.getLongitude();
        mText.setText(locationStr);
    }

    //实现监听接口
    private final class MyLocationListener implements LocationListener {
        @Override// 位置的改变
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            if (location != null) {
                double latitude = location.getLatitude();// 维度
                double longitude = location.getLongitude();// 经度
                //显示当前坐标
                Toast.makeText(Main3Activity.this, "real_location:("+latitude+","+longitude+")", Toast.LENGTH_LONG).show();
            }
        }

        @Override// gps卫星有一个没有找到
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            Toast.makeText(Main3Activity.this, "statusChanged=" + status, Toast.LENGTH_LONG).show();
        }

        @Override// 某个设置被打开
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override// 某个设置被关闭
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

    }

    //按钮点击事件的方法
    public void getLocation(View view) {
        this.gps();
    }
}
