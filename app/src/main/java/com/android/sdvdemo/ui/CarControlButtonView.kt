package com.android.sdvdemo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.domain.model.Speed
import com.android.domain.model.TurnSignalState

@Composable
fun CarControlButtonView(modifier: Modifier, viewModel: SdvHomeViewModel) {
    LazyColumn(
        modifier = modifier
            .padding(start = 30.dp)
    ) {
        item { SdvItem(viewModel) }
        item { Button(
            onClick = { viewModel.isMoving.value = !viewModel.isMoving.value }
        ) {
            Text(text = "Start/Stop")
        } }
        item { Text(text = "Indicator", color = Color.White) }
        item {
            Row {
                Button(
                    onClick = { viewModel.onIndicatorClicked(TurnSignalState.LEFT) }
                ) {
                    Text(text = "Left")
                }
                Button(
                    onClick = { viewModel.onIndicatorClicked(TurnSignalState.RIGHT) }
                ) {
                    Text(text = "Right")
                }
            }
        }
        item { Text(text = "Door", color = Color.White) }
        item {
            Column {
                Row {
                    Button(
                        onClick = { viewModel.onDoorStatusClicked(SdvHomeViewModel.Door.FRONT_LEFT) }
                    ) {
                        Text(text = "Front Left")
                    }
                    Button(
                        onClick = { viewModel.onDoorStatusClicked(SdvHomeViewModel.Door.FRONT_RIGHT) }
                    ) {
                        Text(text = "Front Right")
                    }
                }
                Row {
                    Button(
                        onClick = { viewModel.onDoorStatusClicked(SdvHomeViewModel.Door.REAR_LEFT) }
                    ) {
                        Text(text = "Rear Left")
                    }
                    Button(
                        onClick = { viewModel.onDoorStatusClicked(SdvHomeViewModel.Door.REAR_RIGHT) }
                    ) {
                        Text(text = "Rear Right")
                    }
                }
            }
        }
    }
}

@Composable
private fun SdvItem(viewModel: SdvHomeViewModel) {

    val speed: Speed by viewModel.speed.collectAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        Text(text = "Speed : ", fontSize = 25.sp, color = Color.White)
        Text(text = "${speed.value}", fontSize = 25.sp, color = Color.Red)
    }
}
