package com.caspar.cpdemo.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.caspar.cpdemo.ui.page.FishScreen

/**
 * 跳转首页数据
 */
fun NavController.navigateToHomeFishPondGraph(navOptions: NavOptions? = null) {
    this.navigate(Screen.HomeFishPond.page, navOptions)
}


fun NavGraphBuilder.homeFishPondScreen() {
    composable(route = Screen.HomeFishPond.page) {
        FishScreen()
    }
}
