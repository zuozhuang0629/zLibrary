package com.wxzt.kfly.overlays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.wxzt.kfly.R;
import com.wxzt.kfly.ustils.MapUtils;
import com.wxzt.kfly.ustils.ZAMapUtil;
import com.wxzt.lib_common.rx.RxBus;


import dji.common.flightcontroller.FlightControllerState;
import dji.common.flightcontroller.LocationCoordinate3D;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/12 15:41
 * Description:   无人机位置图层
 * History:
 */
public class DJIUAVOverlay {

    private Marker uavMarker;
    private AMap mAmap;
    private Bitmap mBitmap;
    Disposable disposable;

    public DJIUAVOverlay(Bitmap bitmap, AMap map) {
        mBitmap = bitmap;
        mAmap = map;

        if (null == map) {
            return;
        }
        init();

    }

    private void init() {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.draggable(false);

//        Bitmap resized = Bitmap.createScaledBitmap(mBitmap, 60, 60, true);

        markerOption.anchor(0.5f, 0.5f)
                .zIndex(1000)
                .icon(BitmapDescriptorFactory.fromBitmap(mBitmap));
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_uav_map));

        uavMarker = mAmap.addMarker(markerOption);


    }


    public void refreshUAVPosition(FlightControllerState flightControllerState) {
        if (null == flightControllerState) {
            return;
        }

        LocationCoordinate3D uavLocation = flightControllerState.getAircraftLocation();
        if (null == uavLocation) {
            return;
        }

        LatLng uavLatLng = ZAMapUtil.gps84_To_Gcj02(uavLocation.getLongitude(), uavLocation.getLatitude());
        setUAVPosition(uavLatLng);
        setUAVDirection(flightControllerState.getAircraftHeadDirection());
    }

    /**
     * 设置无人机位置
     *
     * @param uavLatLng
     */
    private void setUAVPosition(LatLng uavLatLng) {
        if (null == uavLatLng || null == uavMarker) {
            return;
        }

        uavMarker.setPosition(uavLatLng);

    }

    /**
     * 设置无人机方向
     */
    private void setUAVDirection(int direction) {
        if (null == uavMarker) {
            return;
        }
        uavMarker.setRotateAngle(-direction);
    }

    /**
     * 转换坐标
     */


    public void setUAVVisible(boolean isShow) {
        if (null == mAmap) {
            return;
        }

        uavMarker.setVisible(isShow);
    }

    public void onDetch() {
        mBitmap.recycle();
        uavMarker.remove();
        if (null != disposable) {
            disposable.dispose();
        }
    }
}
