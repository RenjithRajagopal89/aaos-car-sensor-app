package com.android.domain.model

data class CarInfo(
    var vin: String = "",
    var make: String = "",
    var model: String = "",
    var modelYear: Int = 0,
) {
    fun isEmpty(): Boolean = vin.isBlank() && make.isBlank() && model.isBlank() && modelYear == 0
}
