package com.caspar.cpdemo.ui.navigation

/**
 * App的界面
 */
sealed class Screen(val page: String) {
    object Splash : Screen("Splash")
    object Main : Screen("Main")
    object HomeFirst : Screen("HOME_FIRST")
    object HomeOther : Screen("HOME_OTHER")
    object HomeMe : Screen("HOME_ME")
    object ShowImage : Screen("ShowImage")
}