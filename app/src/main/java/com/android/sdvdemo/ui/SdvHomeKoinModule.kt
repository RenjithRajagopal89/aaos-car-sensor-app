package com.android.sdvdemo.ui

import com.android.domain.connectCarApiUseCase
import com.android.domain.disconnectCarApiUseCase
import com.android.domain.getCarIndicatorUseCase
import com.android.domain.getCarInfoUseCase
import com.android.domain.getCarSpeedUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sdvHomeKoinModule = module {
    viewModel {
        SdvHomeViewModel(
            connectCarApiUseCase,
            disconnectCarApiUseCase,
            getCarSpeedUseCase,
            getCarIndicatorUseCase,
            getCarInfoUseCase,
        )
    }
}
