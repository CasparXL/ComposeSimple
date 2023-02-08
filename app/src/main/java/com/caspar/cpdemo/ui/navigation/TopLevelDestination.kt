/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    FISH_POND(
        selectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconFishPondOn),
        unselectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconFishPondOff),
        iconTextId = R.string.home_fish_pond,
        titleTextId = R.string.home_fish_pond,
    ),
    FOUND(
        selectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconFoundOn),
        unselectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconFoundOff),
        iconTextId = R.string.home_found,
        titleTextId = R.string.home_found,
    ),
    ESSAY(
        selectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconEssayOn),
        unselectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconEssayOff),
        iconTextId = R.string.home_essay,
        titleTextId = R.string.home_essay,
    ),
    COURSE(
        selectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconCourseOn),
        unselectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconCourseOff),
        iconTextId = R.string.home_course,
        titleTextId = R.string.home_course,
    ),
    ME(
        selectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconMeOn),
        unselectedIcon = Icon.DrawableResourceIcon(NiaIcons.IconMeOff),
        iconTextId = R.string.home_me,
        titleTextId = R.string.home_me
    ),
}
