package com.caspar.cpdemo.ui.navigation

/**
 * App的界面
 */
sealed class Screen(val page: String) {
    object Splash : Screen("Splash")
    object Main : Screen("Main")
    object HomeFishPond : Screen("HomeFishPond")
    object HomeFound : Screen("HomeFound")
    object HomeEssay : Screen("HomeEssay")
    object HomeCourse : Screen("HomeCourse")
    object HomeMe : Screen("HomeMe")
}