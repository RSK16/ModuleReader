package com.reader.modulereader;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

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

    //实现定位的方法
    public void location() {
        //定义LocationManager对象
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //注册监听
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, new MyLocationListener());
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
                Toast.makeText(Main3Activity.this, "real_location:(" + latitude + "," + longitude + ")", Toast.LENGTH_LONG).show();
                showLocation(location);
            } else {
                Toast.makeText(Main3Activity.this, "real_location:(null)", Toast.LENGTH_LONG).show();
            }
        }

        @Override// gps卫星有一个没有找到
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            Toast.makeText(Main3Activity.this, "statusChanged=" + status, Toast.LENGTH_LONG).show();
        }

        @Override// 某个设置被打开
        public void onProviderEnabled(String provider) {
            Toast.makeText(Main3Activity.this, "real_location:(onProviderEnabled)", Toast.LENGTH_LONG).show();
            // TODO Auto-generated method stub
        }

        @Override// 某个设置被关闭
        public void onProviderDisabled(String provider) {
            Toast.makeText(Main3Activity.this, "real_location:(onProviderDisabled)", Toast.LENGTH_LONG).show();
            // TODO Auto-generated method stub
        }


    }

    //按钮点击事件的方法
    public void getLocation(View view) {
        this.location();
    }
}
