package com.caspar.cpdemo.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.caspar.cpdemo.ui.theme.ComposeDemoTheme
import com.gyf.immersionbar.ktx.immersionBar

/**
 * Compose组件的Activity基类
 */
abstract class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersionBar {
            statusBarDarkFont(true)
            keyboardEnable(true)
        }
        setContent {
            ComposeDemoTheme {
                InitViews(savedInstanceState)
            }
        }
    }

    /**
     * 根据组件命名方式,首字母大写，这里代表组件列表
     */
    @Composable
    protected abstract fun InitViews(savedInstanceState: Bundle?)
}
