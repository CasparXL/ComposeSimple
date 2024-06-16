package com.caspar.cpdemo.ui.navigation

import com.caspar.cpdemo.R
import com.caspar.cpdemo.ui.icon.Icon
import com.caspar.cpdemo.ui.icon.NiaIcons

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME_FIRST(
        selectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconFishPondOn),
        unselectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconFishPondOff),
        iconTextId = R.string.home_first,
        titleTextId = R.string.home_first,
    ),
    HOME_OTHER(
        selectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconFoundOn),
        unselectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconFoundOff),
        iconTextId = R.string.home_found,
        titleTextId = R.string.home_found,
    ),
    HOME_ME(
        selectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconMeOn),
        unselectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconMeOff),
        iconTextId = R.string.home_me,
        titleTextId = R.string.home_me
    ),
}
