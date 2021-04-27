package dji.ux.beta.core.widget.other

import dji.common.flightcontroller.LocationCoordinate3D
import dji.keysdk.BatteryKey
import dji.keysdk.CameraKey
import dji.keysdk.FlightControllerKey
import dji.ux.beta.core.base.DJISDKModel
import dji.ux.beta.core.base.WidgetModel
import dji.ux.beta.core.communication.ObservableInMemoryKeyedStore
import dji.ux.beta.core.util.DataProcessor


private const val DEFAULT_PERCENTAGE = 0f

/**
 *  author      : zuoz
 *  date        : 2021/4/22 15:17
 *  description :
 */
class FlightWidgetModel(djiSdkModel: DJISDKModel,
                        keyedStore: ObservableInMemoryKeyedStore
) : WidgetModel(djiSdkModel, keyedStore) {

    private val velocityXProcessor = DataProcessor.create(DEFAULT_PERCENTAGE)
    private val velocityYProcessor = DataProcessor.create(DEFAULT_PERCENTAGE)
    private val velocityZProcessor = DataProcessor.create(DEFAULT_PERCENTAGE)
    private val altitudeProcessor = DataProcessor.create(DEFAULT_PERCENTAGE)
    private val cameraCaptureProcessor = DataProcessor.create(DEFAULT_PERCENTAGE)

    override fun inSetup() {
        val flightVelocityXKey = FlightControllerKey.create(FlightControllerKey.VELOCITY_X)
        val flightVelocityYKey = FlightControllerKey.create(FlightControllerKey.VELOCITY_Y)
        val flightVelocityZKey = FlightControllerKey.create(FlightControllerKey.VELOCITY_Z)
        val flightLocationKey = FlightControllerKey.create(FlightControllerKey.AIRCRAFT_LOCATION)
        val cameraCaptureCountKey = CameraKey.create(CameraKey.SDCARD_AVAILABLE_CAPTURE_COUNT)

        bindDataProcessor(flightVelocityXKey, velocityXProcessor)
        bindDataProcessor(flightVelocityYKey, velocityYProcessor)
        bindDataProcessor(flightVelocityZKey, velocityZProcessor)
        bindDataProcessor(flightLocationKey, altitudeProcessor)
        bindDataProcessor(cameraCaptureCountKey, cameraCaptureProcessor)

    }

    override fun inCleanup() {
    }

    override fun updateStates() {
        if (productConnectionProcessor.value) {
//            when(){
//
//            }
        }
    }
}