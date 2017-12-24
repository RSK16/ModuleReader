package com.reader.modulereader.utils;

/**
 * Created by 赵康 on 2017/12/21.
 */

public class MessageEvent {
    private String lng;
    private String lat;
    private String mMsg;

    public MessageEvent(String msg) {
        mMsg = msg;
    }
    public MessageEvent(String lng,String lat) {
        this.lng = lng;
        this.lat = lat;
    }
    public String getMsg(){
        return mMsg;
    }
    public String getlat(){
        return lat;
    }
    public String getlng(){
        return lng;
    }
}
