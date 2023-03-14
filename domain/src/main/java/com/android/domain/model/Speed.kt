package com.android.domain.model

data class Speed(val value: Float)

fun Speed.toMilesPerHour() =
    Speed(value = value * 2.236936f)
