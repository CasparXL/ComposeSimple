package com.caspar.cpdemo

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.caspar.cpdemo.base.BaseActivity
import com.caspar.cpdemo.ui.ProjectScreen
import com.caspar.cpdemo.ui.page.Greeting
import com.caspar.cpdemo.ui.page.SplashPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Composable
    override fun InitViews(savedInstanceState: Bundle?) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = ProjectScreen.SPLASH) {
            composable(route = ProjectScreen.SPLASH) {
                SplashPage(navController)
            }
            composable(route = ProjectScreen.MAIN) {
                Greeting(name = "Hello Compose")
            }
        }

    }

}
