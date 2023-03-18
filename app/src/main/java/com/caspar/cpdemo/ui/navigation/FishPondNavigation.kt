package com.caspar.cpdemo.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.caspar.cpdemo.ui.page.FishScreen

/**
 * 跳转首页数据
 */
fun NavController.navigateToHomeFirstGraph(navOptions: NavOptions? = null) {
    this.navigate(Screen.HomeFirst.page, navOptions)
}


fun NavGraphBuilder.homeFirstScreen() {
    composable(route = Screen.HomeFirst.page) {
        FishScreen()
    }
}
