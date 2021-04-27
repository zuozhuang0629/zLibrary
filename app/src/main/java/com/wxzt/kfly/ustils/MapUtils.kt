package com.wxzt.kfly.ustils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.blankj.utilcode.util.ToastUtils
import com.wxzt.kfly.R
import com.wxzt.kfly.interfaces.ILocationChange


/**
 *  author      : zuoz
 *  date        : 2021/4/8 14:46
 *  description : 搞得地图工具
 */
class MapUtils(private val mMap: AMap, val context: Context, private val mChange: ILocationChange)
    : LocationSource, AMapLocationListener {
    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var mLocationClient: AMapLocationClient? = null
    private var mFirstFix = false
    private var mLocMarker: Marker? = null
    private var mLocationOption: AMapLocationClientOption? = null
    private var myLocation: LatLng? = null// 自己的位置信息
    private var mSensorHelper: SensorEventHelper? = null

    init {
        //设置地图缩放按钮显隐
        val mUiSettings = mMap.uiSettings //实例化UiSettings类对象
        mUiSettings.isZoomControlsEnabled = false
        mMap.setLocationSource(this)
        mMap.isMyLocationEnabled = true

        mSensorHelper = SensorEventHelper(context)
        mSensorHelper?.registerSensorListener()
    }


    private fun addMarker(latlng: LatLng) {
        if (mLocMarker != null) {
            return
        }
        var bMap = BitmapFactory.decodeResource(context.resources,
                R.drawable.navi_map_gps_locked)
//        bMap = zoomImg(bMap, 120, 120)
        val des = BitmapDescriptorFactory.fromBitmap(bMap)

//		BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
        val options = MarkerOptions()
        options.icon(des)
        options.anchor(0.5f, 0.5f)
        options.position(latlng)
        mLocMarker = mMap.addMarker(options)
    }

    private fun zoomImg(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        // 获得图片的宽高
        val width = bm.width
        val height = bm.height
        // 计算缩放比例
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true)
    }

    override fun activate(listener: LocationSource.OnLocationChangedListener?) {
        mListener = listener
        if (mLocationClient == null) {
            mLocationClient = AMapLocationClient(context)
            mLocationOption = AMapLocationClientOption()
            mLocationClient?.apply {
                setLocationListener(this@MapUtils)
                setLocationOption(mLocationOption)
                startLocation()
            }
            mLocationOption?.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        }
    }

    override fun deactivate() {
        mListener = null
        mLocationClient?.let {
            it.stopLocation()
            it.onDestroy()
        }

        mLocationClient = null
    }


    override fun onLocationChanged(amapLocation: AMapLocation?) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.errorCode == 0) {
                val location = LatLng(amapLocation.latitude, amapLocation.longitude)
                myLocation = location
                if (!mFirstFix) {
                    mFirstFix = true
                    addMarker(location) //添加定位图标
                    mSensorHelper?.setCurrentMarker(mLocMarker) //定位图标旋转
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f))
                    mChange.locationChange(location)
                } else {
                    mLocMarker!!.position = location
                }
            } else {
                val errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo()
//                ToastUtils.showLong(errText)
            }
        }
    }


}