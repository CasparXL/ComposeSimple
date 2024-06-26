package com.caspar.cpdemo.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.caspar.cpdemo.ui.page.FishScreen
import com.caspar.cpdemo.ui.page.OtherPage
import com.caspar.cpdemo.viewmodel.homepage.HomeViewModel

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

fun NavGraphBuilder.homeHomeFoundScreen() {
    composable(route = Screen.HomeOther.page) {
        OtherPage()
    }
}

fun NavGraphBuilder.homeFirstScreen(viewModel:HomeViewModel) {
    composable(route = Screen.HomeFirst.page) {
        FishScreen(viewModel)
    }
}
