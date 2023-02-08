package com.caspar.cpdemo.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

/**
 * 跳转首页数据
 */
fun NavController.navigateToHomeCourseGraph(navOptions: NavOptions? = null) {
    this.navigate(ProjectScreen.HOME_COURSE, navOptions)
}


fun NavGraphBuilder.homeHomeCourseScreen() {
    composable(route = ProjectScreen.HOME_COURSE) {
        Text(
            text = "课程",
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}