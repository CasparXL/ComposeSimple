package com.caspar.cpdemo.ui.navigation

/**
 * App的界面
 */
sealed class Screen(val page: String) {
    object Splash : Screen("Splash")
    object Main : Screen("Main")
    object HomeFirst : Screen("HomeFirst")
    object HomeOther : Screen("HomeOther")
    object HomeMe : Screen("HomeMe")
}