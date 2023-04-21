package com.android.sdvdemo.ui

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.domain.model.CarInfo
import com.android.domain.model.Indicator
import com.android.domain.model.Speed
import com.android.domain.model.TurnSignalState
import com.android.sdvdemo.R
import kotlinx.coroutines.flow.flow
import kotlin.math.roundToInt

@Composable
fun SdvHome(viewModel: SdvHomeViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        //CarControlButtonView(modifier = Modifier.align(Alignment.TopStart), viewModel)

        CarSpeed(
            viewModel,
            Modifier
                .fillMaxWidth(0.4f)
                .fillMaxHeight()
        )

        Row(modifier = Modifier.align(Alignment.Center)) {
            MovingRoad(viewModel)
            CarImage(
                modifier = Modifier.align(Alignment.CenterVertically),
                viewModel = viewModel
            )
            MovingRoad(viewModel)
        }

        CarInfo(
            viewModel = viewModel,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
                .width(500.dp)
        )
    }
}

@Composable
private fun CarSpeed(viewModel: SdvHomeViewModel, modifier: Modifier) {
    Box(modifier = modifier) {

        val speed by viewModel.speed.collectAsState()
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${speed.value.roundToInt()}",
                style = TextStyle(fontSize = 90.sp, fontWeight = FontWeight.ExtraBold),
                color = Color.White
            )
            Text(
                text = "MPH",
                style = TextStyle(fontSize = 70.sp, fontWeight = FontWeight.ExtraBold),
                color = Color.White
            )
        }
    }
}

@Composable
private fun CarImage(modifier: Modifier, viewModel: SdvHomeViewModel) {
    Box(modifier = modifier) {

        val indicator by viewModel.indicator.collectAsState()
        if (indicator.value == TurnSignalState.LEFT) {
            CarIndicator(
                indicatorSize = 50.dp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 55.dp, top = 25.dp)
            )

        } else if (indicator.value == TurnSignalState.RIGHT) {
            CarIndicator(
                indicatorSize = 50.dp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 60.dp, top = 28.dp)
            )
        }

        Image(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            painter = painterResource(id = R.drawable.volvo_image),
            contentDescription = "volvo_image",
        )

        // Back indicators
        if (indicator.value == TurnSignalState.LEFT) {
            CarIndicator(
                indicatorSize = 50.dp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 73.dp, bottom = 22.dp)
            )
        } else if (indicator.value == TurnSignalState.RIGHT) {
            CarIndicator(
                indicatorSize = 50.dp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 80.dp, bottom = 20.dp)
            )
        }

        val frontLeft by viewModel.frontLeftDoor.collectAsState()
        CarDoor(
            door = SdvHomeViewModel.Door.FRONT_LEFT,
            isDoorOpen = frontLeft,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 100.dp)
        )

        val frontRight by viewModel.frontRightDoor.collectAsState()
        CarDoor(
            door = SdvHomeViewModel.Door.FRONT_RIGHT,
            isDoorOpen = frontRight,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 100.dp, end = 5.dp)
        )

        val rearLeft by viewModel.rearLeftDoor.collectAsState()
        CarDoor(
            door = SdvHomeViewModel.Door.REAR_LEFT,
            isDoorOpen = rearLeft,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 150.dp)
        )

        val rearRight by viewModel.rearRightDoor.collectAsState()
        CarDoor(
            door = SdvHomeViewModel.Door.REAR_RIGHT,
            isDoorOpen = rearRight,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 150.dp, end = 5.dp)
        )
    }
}

@Composable
private fun CarInfo(viewModel: SdvHomeViewModel, modifier: Modifier) {
    Box(modifier = modifier) {
        val carInfo by viewModel.carInfo.collectAsState()

        Column {
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "MAKE", weight = 0.4f)
                TableCell(text = carInfo.make, weight = 0.6f)
            }
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "MODEL", weight = 0.4f)
                TableCell(text = carInfo.model, weight = 0.6f)
            }
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "MODEL YEAR", weight = 0.4f)
                TableCell(text = "${carInfo.modelYear}", weight = 0.6f)
            }
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "VIN", weight = 0.4f)
                TableCell(text = carInfo.vin, weight = 0.6f)
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        modifier = Modifier
            .border(1.dp, Color.Gray)
            .weight(weight)
            .padding(8.dp),
        color = Color.White,
        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.W800)
    )
}

@Composable
private fun CarIndicator(
    indicatorSize: Dp,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        val infiniteTransition = rememberInfiniteTransition()
        val size by infiniteTransition.animateValue(
            initialValue = indicatorSize,
            targetValue = 0.dp,//indicatorSize - 10.dp,
            Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(300, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        val smallCircle by infiniteTransition.animateValue(
            initialValue = indicatorSize / 2,
            targetValue = 0.dp,//(indicatorSize / 2) + 10.dp,
            Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(300, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        Box(
            modifier = Modifier
                .width(indicatorSize)
                .height(indicatorSize),
            contentAlignment = Alignment.Center
        ) {
            SimpleCircleShape2(
                size = size,
                color = Color.Yellow.copy(alpha = 0.25f)
            )
            SimpleCircleShape2(
                size = smallCircle,
                color = Color.Yellow.copy(alpha = 0.25f)
            )
            SimpleCircleShape2(
                size = 5.dp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
private fun CarDoor(door: SdvHomeViewModel.Door, isDoorOpen: Boolean, modifier: Modifier) {
    val angle = if (isDoorOpen) {
        when (door) {
            SdvHomeViewModel.Door.FRONT_LEFT,
            SdvHomeViewModel.Door.REAR_LEFT -> 80f
            SdvHomeViewModel.Door.FRONT_RIGHT,
            SdvHomeViewModel.Door.REAR_RIGHT -> -80f
        }
    } else {
        0f
    }
    val doorAngle by animateFloatAsState(
        targetValue = angle,
        animationSpec = tween(500)
    )
    Canvas(
        modifier = modifier
            .size(size = 150.dp)
    ) {
        drawArc(
            color = Color.LightGray.copy(alpha = 0.25f),
            startAngle = 90f,
            sweepAngle = doorAngle,
            useCenter = true
        )
    }
}

@Composable
fun SimpleCircleShape2(
    size: Dp,
    color: Color = Color.Yellow,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.LightGray.copy(alpha = 0.0f)
) {
    Column(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    color
                )
                .border(borderWidth, borderColor)
        )
    }
}

@Composable
private fun MovingRoad(viewModel: SdvHomeViewModel) {

    val isMoving by viewModel.isMoving.collectAsState()

    val transition = rememberInfiniteTransition()
    val startY12 by transition.animateFloat(
        initialValue = -1.5f,
        targetValue = if (isMoving) 0f else -1.5f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(4000, 0, LinearEasing)
        )
    )

    val endY12 by transition.animateFloat(
        initialValue = 2.5f,
        targetValue = if (isMoving) 4f else 2.5f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(4000, 0, LinearEasing)
        )
    )

    Canvas(
        modifier = Modifier
            .fillMaxHeight()
            .width(20.dp)
            .padding(top = 20.dp, bottom = 20.dp)
    ){
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            start = Offset(x = canvasWidth / 2, y = canvasHeight * startY12),
            end = Offset(x = canvasWidth / 2, y = canvasHeight * endY12),
            color = Color.Gray,
            strokeWidth = 10f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 25f), 0f)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    val viewModel = createDummyViewModel()

    MaterialTheme {
        SdvHome(viewModel = viewModel)
    }
}

private fun createDummyViewModel() =
    SdvHomeViewModel(
        connectCarApiUseCase = { Result.success(Unit) },
        disconnectCarApiUseCase = { Result.success(Unit) },
        getCarSpeedUseCase = { flow { emit(Speed(100f)) } },
        getCarIndicatorUseCase = { flow { emit(Indicator(TurnSignalState.NONE)) }},
        getCarInfoUseCase = { flow { emit(CarInfo()) }}
    )

