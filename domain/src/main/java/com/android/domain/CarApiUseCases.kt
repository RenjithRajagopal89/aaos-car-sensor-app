package com.android.domain

import com.android.domain.model.CarInfo
import com.android.domain.model.Indicator
import com.android.domain.model.Speed
import com.android.domain.repository.CarApiRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.scope.Scope

typealias ConnectCarApiUseCase = () -> Result<Unit>
val Scope.connectCarApiUseCase: ConnectCarApiUseCase
    get() = carApiRepository::connectCarApi

typealias DisconnectCarApiUseCase = () -> Result<Unit>
val Scope.disconnectCarApiUseCase: DisconnectCarApiUseCase
    get() = carApiRepository::disconnectCarApi

typealias GetCarSpeedUseCase = () -> Flow<Speed>
val Scope.getCarSpeedUseCase: GetCarSpeedUseCase
    get() = carApiRepository::getCarSpeed

typealias GetCarIndicatorUseCase = () -> Flow<Indicator>
val Scope.getCarIndicatorUseCase: GetCarIndicatorUseCase
    get() = carApiRepository::getIndicatorState

typealias GetCarInfoUseCase = () -> Flow<CarInfo>
val Scope.getCarInfoUseCase: GetCarInfoUseCase
    get() = carApiRepository::getCarInfo

private val Scope.carApiRepository: CarApiRepository get() = get()
