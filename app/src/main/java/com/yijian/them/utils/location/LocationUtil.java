package com.yijian.them.utils.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationUtil implements AMapLocationListener {
    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    private OnLocationListener onLocationListener;
    private OnLocationAddressListener onLocationAddressListener;
    private static LocationUtil locationUtil;

//    public static LocationUtil getLocationUtil(Context mContext) {
//        if (locationUtil == null) {
//            locationUtil = new LocationUtil();
//            locationUtil.init(mContext);
//        }
//        return locationUtil;
//    }

    public LocationUtil(Context mContext) {
        init(mContext);
    }

    public void setOnLocationAddressListener(OnLocationAddressListener onLocationAddressListener) {
        this.onLocationAddressListener = onLocationAddressListener;
    }

    public void setOnLocationListener(OnLocationListener onLocationListener) {
        this.onLocationListener = onLocationListener;
    }

    public void init(Context mContext) {
        mlocationClient = new AMapLocationClient(mContext);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = amapLocation.getLatitude();//获取纬度
                double longitude = amapLocation.getLongitude();//获取经度
                String cityCode = amapLocation.getCityCode();//获取城市编码
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                Log.d("定位", "Latitude :" + amapLocation.getLatitude()
                        + ", Longitude :" + amapLocation.getLongitude() + ", cityCode :"
                        + amapLocation.getCityCode());

                if (onLocationListener != null) {
                    onLocationListener.onLocation(latitude, longitude, cityCode);
                }
                if (onLocationAddressListener != null) {
                    String address = amapLocation.getAddress();
                    String poiName = amapLocation.getPoiName();
                    String aoiName = amapLocation.getAoiName();
                    onLocationAddressListener.onLocation(latitude, longitude, cityCode, aoiName, address);
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    public void onceLocation(boolean isLocation) {
        mLocationOption.setOnceLocation(isLocation);
    }

    public void startLocation() {
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位
        mlocationClient.startLocation();
    }

    public interface OnLocationListener {
        void onLocation(double latitude, double longitude, String cityCode);
    }

    public interface OnLocationAddressListener {
        void onLocation(double latitude, double longitude, String cityCode, String name, String address);
    }
}
