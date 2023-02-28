package com.caspar.cpdemo

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.caspar.cpdemo.base.BaseActivity
import com.caspar.cpdemo.ui.navigation.Screen
import com.caspar.cpdemo.ui.page.HomeScreen
import com.caspar.cpdemo.ui.page.SplashPage
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
