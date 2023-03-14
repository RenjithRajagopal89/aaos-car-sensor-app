package com.android.domain.repository

import com.android.domain.model.CarInfo
import com.android.domain.model.Indicator
import kotlinx.coroutines.flow.Flow
import com.android.domain.model.Speed

interface CarApiRepository {

    fun connectCarApi(): Result<Unit>

    fun disconnectCarApi(): Result<Unit>

    fun getCarSpeed(): Flow<Speed>

    fun getIndicatorState(): Flow<Indicator>

    fun getCarInfo(): Flow<CarInfo>

    companion object Factory
}
