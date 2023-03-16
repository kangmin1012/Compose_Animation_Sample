package kang.mingu.composeanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kang.mingu.composeanimation.ui.theme.ComposeAnimationSampleTheme
import kang.mingu.composeanimation.ui.theme.Pink80
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAnimationSampleTheme {
                // A surface container using the 'background' color from the theme
                ActivityView()
            }
        }
    }
}

@Composable
fun ActivityView() {
    Surface(
        color = Pink80
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StepperAnimation(modifier = Modifier)
        }
    }
}

@Composable
fun StepperAnimation(modifier: Modifier) {
    var currentNumber by remember {
        mutableStateOf(0)
    }
    var frontNumber by remember {
        mutableStateOf(0)
    }
    var backNumber by remember {
        mutableStateOf(currentNumber)
    }
    var targetAngle by remember {
        mutableStateOf(0f)
    }

    val rotation = animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        )
    )

    fun isFront(): Boolean {
        val value = abs(targetAngle % 360)
        return value < 90 || value > 270
    }

    fun flipBack() {
        currentNumber -= 1
        if (isFront()) {
            backNumber = currentNumber
        } else {
            frontNumber = currentNumber
        }
        targetAngle -= 180f
    }

    fun flipNext() {
        currentNumber += 1
        if (isFront()) {
            backNumber = currentNumber
        } else {
            frontNumber = currentNumber
        }
        targetAngle += 180f
    }

    @Composable
    fun Step(number: Int, rotationY: Float) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .graphicsLayer {
                    this.rotationY = rotationY
                }
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { flipBack() }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = number.toString(),
                    color = Color.Black,
                    fontSize = 40.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        flipNext()
                    }
            )
        }
    }

    Box(
        modifier = modifier
            .height(80.dp)
            .width(200.dp)
            .graphicsLayer {
                rotationY = rotation.value
            }
    ) {
        if (isFront()) {
            //Front
            Step(number = frontNumber, rotationY = 0f)
        } else {
            //Back
            Step(number = backNumber, rotationY = targetAngle)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ActivityPreview() {
    ComposeAnimationSampleTheme {
        ActivityView()
    }
}


@Preview
@Composable
fun StepPreview() {
    StepperAnimation(Modifier)
}
