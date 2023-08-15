package com.caspar.cpdemo.ui.theme

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.view.Display
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.core.view.ViewCompat
import com.klsy.cashier.app.ui.view.Presentation

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

/**
 *
 */
@Composable
fun ComposeDemoTheme(
    secondaryScreen: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val fontScale = LocalDensity.current.fontScale
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels
    val display = DisplayManagerCompat.getInstance(LocalContext.current)
    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = Typography,
        content = {
            display.displays.also { ownDisplayList ->
                ownDisplayList.forEach { // 遍历屏幕
                    if (it.displayId == Display.DEFAULT_DISPLAY) { // 是否为默认屏幕(主屏)
                        CompositionLocalProvider(
                            LocalDensity provides Density(
                                density = dynamicDensity(1920f,1080f),
                                fontScale = fontScale
                            )
                        ) {
                            content()
                        }
                    } else {
                        CompositionLocalProvider(
                            LocalDensity provides Density(
                                density = dynamicDensity(1920f,1080f),
                                fontScale = fontScale
                            )
                        ) {
                            Presentation(display = it, onDismissRequest = {  }) {
                                secondaryScreen()
                            }
                        }
                    }
                }
            }

        }
    )
}

/**
 * 根据UI设计图得出动态密度适配不同屏幕
 * @param designWidth 填入UI设计图的屏幕短边dp值（绝对宽度）
 * @param designHeight 填入UI设计图的屏幕长边dp值（绝对高度）
 */
@Composable
private fun dynamicDensity(designWidth: Float, designHeight: Float): Float {
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels    //屏幕短边像素（绝对宽度）
    val heightPixels = displayMetrics.heightPixels  //屏幕长边像素（绝对高度）
    val isPortrait = LocalContext.current.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT  //判断横竖屏
    return if (isPortrait) widthPixels / designWidth else heightPixels / designHeight //计算密度
}