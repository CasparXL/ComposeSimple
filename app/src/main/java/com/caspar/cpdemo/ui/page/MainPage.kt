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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.caspar.cpdemo.ui.icon.Icon
import com.caspar.cpdemo.ui.navigation.*
import com.caspar.cpdemo.ui.theme.ComposeDemoTheme
import com.caspar.cpdemo.utils.log.LogUtil
import com.caspar.cpdemo.viewmodel.homepage.HomeViewModel


val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalComposeUiApi::class
)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val snackbarHostState by remember { mutableStateOf(viewModel.snackBarHost) }
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
                currentDestination = navController.currentBackStackEntryAsState().value?.destination,
                modifier = Modifier.testTag("NiaBottomBar"),
            )
        }, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.HomeFirst.page,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                )
        ) {
            homeFirstScreen(viewModel)
            homeHomeFoundScreen()
            homeHomeMeScreen()
        }
    }
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
private fun NiaBottomBar(
    navHostController: NavHostController,
    destinations: List<TopLevelDestination>,
    currentDestination: NavDestination? = null,
    modifier: Modifier = Modifier,
) {
    NiaNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            NiaNavigationBarItem(
                selected = destination.name == currentDestination?.route,
                onClick = {
                    LogUtil.d("更新数据${destination.name},当前数据->${currentDestination?.route}")
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
                        when (destination) {
                            TopLevelDestination.HOME_FIRST -> navHostController.navigate(
                                route = Screen.HomeFirst.page,
                                navOptions = topLevelNavOptions
                            )

                            TopLevelDestination.HOME_OTHER -> navHostController.navigate(
                                route = Screen.HomeOther.page,
                                navOptions = topLevelNavOptions
                            )

                            TopLevelDestination.HOME_ME -> navHostController.navigate(
                                route = Screen.HomeMe.page,
                                navOptions = topLevelNavOptions
                            )
                        }
                    }
                },
                icon = {
                    val icon = if (destination.name == currentDestination?.route) {
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
                alwaysShowLabel = true,
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