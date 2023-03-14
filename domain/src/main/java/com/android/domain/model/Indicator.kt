package com.android.domain.model

data class Indicator(val value: TurnSignalState)

fun Int.mapToIndicator() =
    Indicator(
        when (this) {
            1 -> TurnSignalState.LEFT
            2 -> TurnSignalState.RIGHT
            else -> TurnSignalState.NONE
        }
    )

enum class TurnSignalState {
    NONE,
    RIGHT,
    LEFT
}
