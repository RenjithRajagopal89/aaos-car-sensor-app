package com.android.sdvdemo.ui

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.ConnectCarApiUseCase
import com.android.domain.DisconnectCarApiUseCase
import com.android.domain.GetCarIndicatorUseCase
import com.android.domain.GetCarInfoUseCase
import com.android.domain.GetCarSpeedUseCase
import com.android.domain.model.CarInfo
import com.android.domain.model.Indicator
import com.android.domain.model.Speed
import com.android.domain.model.TurnSignalState
import com.android.domain.model.toMilesPerHour
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SdvHomeViewModel(
    private val connectCarApiUseCase: ConnectCarApiUseCase,
    private val disconnectCarApiUseCase: DisconnectCarApiUseCase,
    private val getCarSpeedUseCase: GetCarSpeedUseCase,
    private val getCarIndicatorUseCase: GetCarIndicatorUseCase,
    private val getCarInfoUseCase: GetCarInfoUseCase,
) : ViewModel(), DefaultLifecycleObserver {

    val speed = MutableStateFlow(Speed(0f))
    val indicator = MutableStateFlow(Indicator(TurnSignalState.NONE))
    val carInfo = MutableStateFlow(CarInfo())

    var isMoving = MutableStateFlow(false)

    var frontLeftDoor = MutableStateFlow(false)
    var frontRightDoor = MutableStateFlow(false)
    var rearLeftDoor = MutableStateFlow(false)
    var rearRightDoor = MutableStateFlow(false)

    override fun onStart(owner: LifecycleOwner) {
        // listen for all car apis
        Log.d("ViewModel", "onStart: ")
        presentCarSpeed()
        presentIndicatorState()
        presentCarInfo()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        disconnectCarApiUseCase()
    }

    fun connectCarApi() {
        connectCarApiUseCase()
    }

    private fun presentCarSpeed() {
        getCarSpeedUseCase().onEach {
            speed.value = it.toMilesPerHour()
            Log.d("ViewModel", "presentCarSpeed: $it")
            isMoving.value = it.value > 0
        }.launchIn(viewModelScope)
    }

    private fun presentIndicatorState() {
        getCarIndicatorUseCase().onEach {
            indicator.value = it
            Log.d("ViewModel", "presentIndicatorState: $it")
        }.launchIn(viewModelScope)
    }

    private fun presentCarInfo() {
        getCarInfoUseCase().onEach {
            carInfo.value = it
            Log.d("ViewModel", "presentCarInfo: $it")
        }.launchIn(viewModelScope)
    }

    fun onIndicatorClicked(signalState: TurnSignalState) {
        resetIndicatorIfNeeded(signalState)
    }

    private fun resetIndicatorIfNeeded(signalState: TurnSignalState) {
        if (indicator.value.value == signalState) {
            indicator.value = Indicator(TurnSignalState.NONE)
        } else {
            indicator.value = Indicator(signalState)
        }
    }

    fun onDoorStatusClicked(door: Door) {
        when (door) {
            Door.FRONT_LEFT -> frontLeftDoor.value = !frontLeftDoor.value
            Door.FRONT_RIGHT -> frontRightDoor.value = !frontRightDoor.value
            Door.REAR_LEFT -> rearLeftDoor.value = !rearLeftDoor.value
            Door.REAR_RIGHT -> rearRightDoor.value = !rearRightDoor.value
        }
    }

    enum class Door {
        FRONT_LEFT,
        FRONT_RIGHT,
        REAR_LEFT,
        REAR_RIGHT
    }
}
