package com.caspar.cpdemo.ui.page

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.caspar.cpdemo.ui.icon.Icon
import com.caspar.cpdemo.ui.navigation.*
import com.caspar.cpdemo.ui.theme.ComposeDemoTheme


val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NiaBottomBar(
                navHostController = navController,
                destinations = topLevelDestinations,
                modifier = Modifier.testTag("NiaBottomBar"),
            )
        }) { padding ->
            NavHost(
                navController = navController,
                startDestination = ProjectScreen.HOME_FISH_POND,
                modifier = Modifier
                    .padding(padding)
                    .consumedWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    )
            ) {
                homeFishPondScreen()
                homeHomeFoundScreen()
                homeHomeEssayScreen()
                homeHomeCourseScreen()
                homeHomeMeScreen()
            }
        }
}

@Composable
private fun NiaBottomBar(
    navHostController: NavHostController,
    destinations: List<TopLevelDestination>,
    modifier: Modifier = Modifier,
) {
    var currentDestination by remember {
        mutableStateOf(destinations.first().name)
    }
    NiaNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            NiaNavigationBarItem(
                selected = destination.name == currentDestination,
                onClick = {
                    trace("Navigation: ${destination.name}") {
                        val topLevelNavOptions = navOptions {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                        currentDestination = destination.name
                        when (destination) {
                            TopLevelDestination.FISH_POND -> navHostController.navigateToHomeFishPondGraph(
                                topLevelNavOptions
                            )
                            TopLevelDestination.FOUND -> navHostController.navigateToHomeFoundGraph(
                                topLevelNavOptions
                            )
                            TopLevelDestination.ESSAY -> navHostController.navigateToHomeEssayGraph(
                                topLevelNavOptions
                            )
                            TopLevelDestination.COURSE -> navHostController.navigateToHomeCourseGraph(
                                topLevelNavOptions
                            )
                            TopLevelDestination.ME -> navHostController.navigateToHomeMeGraph(
                                topLevelNavOptions
                            )
                        }
                    }
                },
                icon = {
                    val icon = if (destination.name == currentDestination) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }
                    when (icon) {
                        is Icon.ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null,
                            tint = Color.Unspecified
                        )

                        is Icon.DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            modifier = Modifier.size(22.dp),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                },
                label = { Text(stringResource(destination.iconTextId)) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val remoteCallbackList = rememberNavController()
    ComposeDemoTheme {
        NiaBottomBar(
            navHostController = remoteCallbackList,
            destinations = topLevelDestinations,
            modifier = Modifier.testTag("NiaBottomBar"),
        )
    }
}