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


import io.reactivex.disposables.Disposable;

/**
 * author : zuoz
 * date : 2021/4/1 13:24
 * description :
 */
public class GoHomeLocationOverlay {
    private Marker marker;
    private AMap mAmap;
    private Context mContext;
    Disposable disposable;

    public GoHomeLocationOverlay(Context context, AMap map) {
        mContext = context;
        mAmap = map;

        if (null == map) {
            return;
        }
        init();

    }

    private void init() {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.draggable(false);


        Bitmap remote = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_home);
        Bitmap resized = Bitmap.createScaledBitmap(remote, 160, 160, true);

        markerOption.anchor(0.5f, 1f)
                .zIndex(1000)
                .icon(BitmapDescriptorFactory.fromBitmap(resized));
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_uav_map));

        marker = mAmap.addMarker(markerOption);
    }


    /**
     * 设置返航位置
     *
     * @param remoteLatLng
     */
    public void setRemotePosition(LatLng remoteLatLng) {
        if (null == remoteLatLng || null == marker) {
            return;
        }

        marker.setPosition(remoteLatLng);

    }


    public void onDetch() {
        mContext = null;
        marker.remove();
        if (null != disposable) {
            disposable.dispose();
        }
    }

}
