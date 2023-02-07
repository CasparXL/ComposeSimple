package com.caspar.cpdemo.ui.page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.caspar.cpdemo.R
import com.caspar.cpdemo.ui.ProjectScreen
import com.caspar.cpdemo.ui.navigationAndFinish
import kotlinx.coroutines.delay

@Composable
fun SplashPage(navController: NavController = rememberNavController()) {
    Image(
        painter = painterResource(id = R.drawable.img_transition_default),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds,
        contentDescription = "启动页"
    )
    //延迟跳转新界面，并从堆栈中移除
    LaunchedEffect(Unit){
        delay(2000)
        navController.navigationAndFinish(ProjectScreen.MAIN, ProjectScreen.SPLASH)
    }
}
