package com.android.data.repository

import android.car.Car
import com.android.domain.repository.CarApiRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val carApiDataModule = module {
    single {
        CarApiRepository.Factory.build(car = Car.createCar(androidContext()))
    }
}
