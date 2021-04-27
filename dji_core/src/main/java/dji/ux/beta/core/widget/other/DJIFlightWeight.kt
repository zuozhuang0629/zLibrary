package dji.ux.beta.core.widget.other

import android.content.Context
import android.util.AttributeSet
import dji.ux.beta.core.R
import dji.ux.beta.core.base.DJISDKModel
import dji.ux.beta.core.base.widget.ConstraintLayoutWidget
import dji.ux.beta.core.communication.ObservableInMemoryKeyedStore
import dji.ux.beta.core.widget.battery.BatteryWidget
import dji.ux.beta.core.widget.battery.BatteryWidgetModel

/**
 *  author      : zuoz
 *  date        : 2021/4/22 15:14
 *  description :
 */
class DJIFlightWeight @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayoutWidget<BatteryWidget.ModelState>(context, attrs, defStyleAttr) {

    private val widgetModel by lazy {
        FlightWidgetModel(
                DJISDKModel.getInstance(),
                ObservableInMemoryKeyedStore.getInstance())
    }
    override fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        inflate(context, R.layout.uxsdk_widget_gps_signal, this)

    }

    override fun reactToModelChanges() {
    }

    override fun getIdealDimensionRatioString(): String? = null
}