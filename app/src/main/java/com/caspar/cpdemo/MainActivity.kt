package com.caspar.cpdemo

import androidx.compose.ui.graphics.Color as C
import android.os.Bundle
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.caspar.cpdemo.base.BaseActivity
import com.caspar.cpdemo.ui.navigation.Screen
import com.caspar.cpdemo.ui.page.HomeScreen
import com.caspar.cpdemo.ui.page.SplashPage
import com.caspar.cpdemo.utils.log.LogUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var navController: NavHostController

    @Composable
    override fun InitViews(savedInstanceState: Bundle?) {
        navController = rememberNavController()
        NavHost(navController, startDestination = Screen.Splash.page) {
            composable(route = Screen.Splash.page) {
                SplashPage(navController)
            }
            composable(route = Screen.Main.page) {
                HomeScreen()
            }
        }

    }

}

/**
 * 自定义View以及动画效果
 */
@Composable
fun TestCompose() {
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1F else 0F,
        animationSpec = tween(durationMillis = (if (startAnimation) 2000 else 1)),
        visibilityThreshold = 0.1F,
        finishedListener = {
            startAnimation = !startAnimation
        }
    )
    val pathMeasure = PathMeasure()

    Column {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(top = 150.dp)
        ) {
            val height = size.height
            val width = size.width
            val path = Path().apply {
                moveTo((0.1 * width).toFloat(), 0F)
                lineTo((0.1 * width).toFloat(), (0.8 * height).toFloat())
                quadraticBezierTo(
                    x1 = (0.1 * width).toFloat(),
                    y1 = (0.9 * height).toFloat(),
                    x2 = (0.2 * width).toFloat(),
                    y2 = (0.9 * height).toFloat(),
                )
                lineTo((0.84 * width).toFloat(), (0.90 * height).toFloat())
                quadraticBezierTo(
                    x1 = (0.90 * width).toFloat(),
                    y1 = (0.9 * height).toFloat(),
                    x2 = (0.90 * width).toFloat(),
                    y2 = (0.8 * height).toFloat(),
                )
                lineTo((0.90 * width).toFloat(), 0f)
            }
            drawPath(
                path,
                color = C.Black.copy(alpha = 0.5F),
                style = Stroke(width = 15.dp.value)
            )
            pathMeasure.setPath(path, false)
            path.reset()
            val stop: Float = pathMeasure.length * alphaAnim.value
            val start = stop - 0.2F * pathMeasure.length
            if (startAnimation) {
                LogUtil.d("start${start} stop ${stop} ${pathMeasure.length}")
                pathMeasure.getSegment(start, stop, path, true)
                drawPath(
                    path, C.Red,
                    style = Stroke(width = 15.dp.value)
                )
            }
        }
        Text(
            text = "isStart:${startAnimation} animate:${alphaAnim.value}",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    startAnimation = true
                }
                .padding(20.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun AbstractAccountAuthenticators() {
    TestCompose()
}