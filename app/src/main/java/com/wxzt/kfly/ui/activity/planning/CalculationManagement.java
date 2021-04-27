package com.wxzt.kfly.ui.activity.planning;

import com.amap.api.maps.model.LatLng;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/9/18 17:40
 * Description: 计算管理类
 * History:
 */
public class CalculationManagement {

    /**
     * 控制点
     */
    private List<LatLng> mControlPoint;

    public CalculationManagement() {
        mControlPoint = new ArrayList<>();
    }

    /**
     * 根据参数为中心点  获取半径为0.001的外接矩形
     *
     * @param centerLatLng 中心点
     * @return 矩形四顶点   经纬度   lt  rt  rb  lb
     */
    public List<LatLng> getCenterRect(LatLng centerLatLng) {
        List<LatLng> result = new ArrayList<>();
        result.add(new LatLng(centerLatLng.latitude + 0.001, centerLatLng.longitude - 0.001));
        result.add(new LatLng(centerLatLng.latitude + 0.001, centerLatLng.longitude + 0.001));
        result.add(new LatLng(centerLatLng.latitude - 0.001, centerLatLng.longitude + 0.001));
        result.add(new LatLng(centerLatLng.latitude - 0.001, centerLatLng.longitude - 0.001));

        mControlPoint = result;
        return result;
    }

    /**
     * 计算同步航线
     *
     * @param polygon 同步数据
     * @return 航线
     */
    public List<LatLng> calculationSynchronizeRoute(List<LatLng> polygon,
                                                    int flyHeight,
                                                    float horizontalOverlap) {
        mControlPoint = polygon;
        return calculationRoute(mControlPoint, flyHeight, horizontalOverlap);
    }

    /**
     * 计算航线
     *
     * @param controlPoint      控制点list
     * @param flyHeight         飞行高度
     * @param horizontalOverlap 旁向重叠率
     * @return 任务点
     */
    public List<LatLng> calculationRoute(List<LatLng> controlPoint,
                                         int flyHeight,
                                         float horizontalOverlap) {
        double caLineGap = (getLineGap(flyHeight, 84, horizontalOverlap) / (111000 * cos(0.001)));
        return getLwaypointList(controlPoint, caLineGap);
    }

    /**
     * 刷新航线
     *
     * @param flyHeight
     * @param horizontalOverlap
     * @return
     */
    public List<LatLng> refreshRoute(int flyHeight,
                                     float horizontalOverlap) {
        return calculationRoute(mControlPoint, flyHeight, horizontalOverlap);
    }

    /**
     * @param vertexList 多边形顶点
     * @return
     */
    private List<LatLng> getLwaypointList(List<LatLng> vertexList, double caLineGap) {
        List<LatLng> lwaypointList = new ArrayList<>();
        if (null == vertexList || vertexList.size() == 0) {
            return null;
        }
        //最大值、最小值
        double xmax, xmin;
        xmax = xmin = vertexList.get(0).longitude;
        //斜率
        double slope = 0;
        for (int i = 0; i < vertexList.size(); i++) {
            if (vertexList.get(i).longitude > xmax) {
                xmax = vertexList.get(i).longitude;
            }
            if (vertexList.get(i).longitude < xmin) {
                xmin = vertexList.get(i).longitude;
            }
        }
        double xl = xmin - (xmax - xmin) / 2;
        //创建过各顶点的直线
        List<Double> ylpointList = new ArrayList<Double>();//过各顶点直线的y值集合
        for (int i = 0; i < vertexList.size(); i++) {
            double yv = slope * (xl - vertexList.get(i).longitude) + vertexList.get(i).latitude;
            ylpointList.add(yv);
        }

        if (ylpointList.size() == 0) {
            return null;
        }

        double ylmax, ylmin;
        ylmax = ylmin = ylpointList.get(0);

        //求直线与xl的交点最大值、最小值
        for (int j = 0; j < ylpointList.size(); j++) {
            if (ylpointList.get(j) > ylmax) {
                ylmax = ylpointList.get(j);
            }
            if (ylpointList.get(j) < ylmin) {
                ylmin = ylpointList.get(j);
            }
        }
        int hnum = 1;

        if (ylpointList.size() > 0) {
            hnum = (int) Math.floor((ylmax - ylmin) / caLineGap);//求得航带数
        }

        List<LatLng> crosspointList = new ArrayList<>();//直线跟多边形交点

        for (int sk = 1; sk <= hnum; sk++) {
            for (int n = 0; n < vertexList.size(); n++) {
                if (n == vertexList.size() - 1) {
                    //航线与边的左右端
                    double s1, s2;
                    double ly = slope * (xmin - xl) + ylmax - sk * caLineGap;
                    double ry = slope * (xmax - xl) + ylmax - sk * caLineGap;
                    LatLng left = new LatLng(ly, xmin);
                    LatLng right = new LatLng(ry, xmax);
                    //多边形相邻顶点
                    s1 = crossPoint(left, vertexList.get(0), right);
                    s2 = crossPoint(left, vertexList.get(n), right);
                    if (s1 * s2 <= 0) {
                        double wpx = (s2 * vertexList.get(0).longitude - s1 * vertexList.get(n).longitude) / (s2 - s1);
                        double wpy = (s2 * vertexList.get(0).latitude - s1 * vertexList.get(n).latitude) / (s2 - s1);
                        LatLng lrwaypoint = (new LatLng(wpy, wpx));
                        crosspointList.add(lrwaypoint);
                    }
                } else {
                    double s3, s4;
                    double ly = slope * (xmin - xl) + ylmax - sk * caLineGap;
                    double ry = slope * (xmax - xl) + ylmax - sk * caLineGap;
                    LatLng left = new LatLng(ly, xmin);
                    LatLng right = new LatLng(ry, xmax);
                    s3 = crossPoint(left, vertexList.get(n), right);
                    s4 = crossPoint(left, vertexList.get(n + 1), right);
                    if (s3 * s4 <= 0) {
                        double wpx = (s4 * vertexList.get(n).longitude - s3 * vertexList.get(n + 1).longitude) / (s4 - s3);
                        double wpy = (s4 * vertexList.get(n).latitude - s3 * vertexList.get(n + 1).latitude) / (s4 - s3);
                        LatLng lrwaypoint = (new LatLng(wpy, wpx));
                        crosspointList.add(lrwaypoint);
                    }
                }
            }
            if (crosspointList.size() > 0) {
                if (sk % 2 == 1) {

                    if (crosspointList.get(0).longitude > crosspointList.get(1).longitude) {
                        lwaypointList.add(crosspointList.get(0));
                        lwaypointList.add(crosspointList.get(1));
                    } else {
                        lwaypointList.add(crosspointList.get(1));
                        lwaypointList.add(crosspointList.get(0));
                    }
                    crosspointList.removeAll(crosspointList);
                } else {


                    if (crosspointList.get(0).longitude > crosspointList.get(1).longitude) {
                        lwaypointList.add(crosspointList.get(1));
                        lwaypointList.add(crosspointList.get(0));
                    } else {
                        lwaypointList.add(crosspointList.get(0));
                        lwaypointList.add(crosspointList.get(1));
                    }
                    crosspointList.removeAll(crosspointList);
                }
            }
        }
        return lwaypointList;
    }

    /**
     * @param flyHeight    飞行高度
     * @param fov          无人机广角
     * @param line_overlap 航向重叠率
     * @return 返回航线间距
     */
    private double getLineGap(int flyHeight, float fov, float line_overlap) {
        //航线间距
        float m_lineGap = (float) (2 * flyHeight * Math.tan(Math.toRadians(fov / 2)) * (1 - line_overlap));
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(m_lineGap));
        return bigDecimal.doubleValue();
    }

    private double crossPoint(LatLng a, LatLng b, LatLng c) {
        return (b.longitude - a.longitude) * (c.latitude - a.latitude) - (b.latitude - a.latitude) * (c.longitude - a.longitude);
    }
}
