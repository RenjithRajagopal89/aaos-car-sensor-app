package com.android.data.repository

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.util.Log
import com.android.domain.model.CarInfo
import com.android.domain.model.Indicator
import com.android.domain.model.Speed
import com.android.domain.model.TurnSignalState
import com.android.domain.model.mapToIndicator
import com.android.domain.repository.CarApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal fun CarApiRepository.Factory.build(car: Car): CarApiRepository = CarApiRepositoryImpl(car)

class CarApiRepositoryImpl(private val car: Car) : CarApiRepository {

    private lateinit var carPropertyManager: CarPropertyManager

    private var speedFlow = MutableStateFlow(Speed(0f))

    private var indicatorFlow = MutableStateFlow(Indicator(TurnSignalState.NONE))

    private var carInfoFlow = MutableStateFlow(CarInfo())

    private var carPropertyListener = createCarPropertyCallback()

    override fun connectCarApi(): Result<Unit> {
        registerCarProperty(VehiclePropertyIds.PERF_VEHICLE_SPEED_DISPLAY)
        registerCarProperty(VehiclePropertyIds.RANGE_REMAINING)
        registerCarProperty(VehiclePropertyIds.TURN_SIGNAL_STATE)
        return Result.success(Unit)
    }

    override fun disconnectCarApi(): Result<Unit> {
        car.disconnect()
        return Result.success(Unit)
    }

    override fun getCarSpeed(): Flow<Speed> {
        return speedFlow
    }

    override fun getIndicatorState(): Flow<Indicator> {
        return indicatorFlow
    }

    override fun getCarInfo(): Flow<CarInfo> {
        return carInfoFlow
    }

    private fun registerCarProperty(propertyId: Int) {
        carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager

        // Subscribes to the gear change events.
        Log.d("CarApiRepositoryImpl", "connectCarApi connected")
        carPropertyManager.registerCallback(
            carPropertyListener,
            propertyId,
            getFlowRate(propertyId)
        )
    }

    private fun getFlowRate(propertyId: Int) =
        when (propertyId) {
            VehiclePropertyIds.PERF_VEHICLE_SPEED_DISPLAY -> CarPropertyManager.SENSOR_RATE_FAST
            else -> CarPropertyManager.SENSOR_RATE_ONCHANGE
        }


    private fun createCarPropertyCallback() = object : CarPropertyManager.CarPropertyEventCallback {
        override fun onChangeEvent(value: CarPropertyValue<Any>) {
            // Get car info value
            if (carInfoFlow.value.isEmpty() || carInfoFlow.value.model.isEmpty() || carInfoFlow.value.make.isEmpty() || carInfoFlow.value.vin.isEmpty() ) {
                Log.d("CarApiRepositoryImpl", "Fetching CarInfo")
                fetchCarInfo()
            }

            // value.value type changes depending on the vehicle property.
            Log.d("CarApiRepositoryImpl", "Received on changed car property event: ${value.propertyId}, value: ${value.value}")
            mapToCarProperty(value)
        }

        override fun onErrorEvent(propId: Int, zone: Int) {
            Log.w("CarApiRepositoryImpl", "Received error car property event, propId=$propId")
        }
    }

    private fun mapToCarProperty(property: CarPropertyValue<Any>) {
        when (property.propertyId) {
            VehiclePropertyIds.PERF_VEHICLE_SPEED_DISPLAY -> speedFlow.value = Speed(property.value as Float)
            VehiclePropertyIds.TURN_SIGNAL_STATE -> indicatorFlow.value = (property.value as Int).mapToIndicator()
            else -> { /* no-op */ }
        }
    }

    private fun fetchCarInfo() {
        val vin = carPropertyManager.getProperty<String>(VehiclePropertyIds.INFO_VIN, 0).value
        val make = carPropertyManager.getProperty<String>(VehiclePropertyIds.INFO_MAKE, 0).value
        val model = carPropertyManager.getProperty<String>(VehiclePropertyIds.INFO_MODEL, 0).value
        val modelYear = carPropertyManager.getProperty<Int>(VehiclePropertyIds.INFO_MODEL_YEAR, 0).value

        val carInfo = CarInfo(vin, make, model, modelYear)

        carInfoFlow.value = carInfo
        Log.d("CarApiRepositoryImpl", "fetchCarInfo: ${carInfoFlow.value}")
    }
}
