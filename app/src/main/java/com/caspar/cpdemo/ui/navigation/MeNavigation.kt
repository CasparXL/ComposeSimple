package com.caspar.cpdemo.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

/**
 * 跳转首页数据
 */
fun NavController.navigateToHomeMeGraph(navOptions: NavOptions? = null) {
    this.navigate(Screen.HomeMe.page, navOptions)
}


fun NavGraphBuilder.homeHomeMeScreen() {
    composable(route = Screen.HomeMe.page) {
        Column {
            Text(
                text = "我的",
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .height(1.dp)
            )
        }
    }
}