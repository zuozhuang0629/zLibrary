package com.wxzt.kfly.ustils;


import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.util.List;

/**
 * Created by hbwy on 2018/3/8.
 */

public class ZAMapUtil {
    public static final String BAIDU_LBS_TYPE = "bd09ll";

    public static double pi = 3.1415926535897932384626;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    /**
     * 84 to 火星坐标系 (GCJ-02)
     *
     * @param lat
     * @param lon
     * @return
     */
    public static LatLng gps84_To_Gcj02(double lon, double lat) {
        if (outOfChina(lon, lat)) {
            return null;
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new LatLng(mgLat, mgLon);
    }

    /**
     * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
     */
    public static LatLng gcj_To_Gps84(double lon, double lat) {
        LatLng gps = transform(lon, lat);
        double lontitude = lon * 2 - gps.longitude;
        double latitude = lat * 2 - gps.latitude;
        return new LatLng(latitude, lontitude);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param gg_lat
     * @param gg_lon
     */
    public static LatLng gcj02_To_Bd09(double gg_lon, double gg_lat) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new LatLng(bd_lon, bd_lat);
    }

    /**
     * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
     * bd_lat * @param bd_lon * @return
     */
    public static LatLng bd09_To_Gcj02(double bd_lon, double bd_lat) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new LatLng(gg_lon, gg_lat);
    }


    public static boolean outOfChina(double lon, double lat) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    public static LatLng transform(double lon, double lat) {
        if (outOfChina(lon, lat)) {
            return new LatLng(lat, lon);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new LatLng(mgLat, mgLon);
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }


    public static float calDistance(List<LatLng> latLngs) {
        float dis = 0;
        if (null == latLngs || latLngs.size() < 2) {
            return dis;
        }
        for (int index = 1; index < latLngs.size(); index++) {
            LatLng start = latLngs.get(index - 1);
            LatLng end = latLngs.get(index);
            dis += AMapUtils.calculateLineDistance(start, end);
        }
        return dis;
    }

    public static float calDistance(LatLng start, LatLng end) {
        float dis = 0;
        if (null == start || null == end) {
            return dis;
        }

        dis += AMapUtils.calculateLineDistance(start, end);

        return dis;
    }

    public  static float calPolygonArea( List<LatLng> latLngs) {
        if (latLngs != null && latLngs.size() >= 3) {
            double var1 = 0.0D;
            double var3 = 111319.49079327357D;
            int j = latLngs.size();
            for (int i = 0; i < j; i++) {
                LatLng var7 = latLngs.get(i % j);
                LatLng var8 = latLngs.get((i + 1) % j);
                double var9 = var7.longitude * var3 * Math.cos(var7.latitude * 0.017453292519943295D);
                double var11 = var7.latitude * var3;
                double var13 = var8.longitude * var3 * Math.cos(var8.latitude * 0.017453292519943295D);
                double var15 = var8.latitude * var3;
                var1 += var9 * var15 - var13 * var11;
            }
            return (float) Math.abs(var1 / 2.0D);
        } else {
            return 0.0F;
        }
    }
}