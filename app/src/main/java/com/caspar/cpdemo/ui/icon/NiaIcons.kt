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

package com.caspar.cpdemo.ui.icon

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.caspar.cpdemo.R

/**
 * Now in Android icons. Material icons are [ImageVector]s, custom icons are drawable resource IDs.
 */
object NiaIcons {
    const val IconFishPondOn = R.drawable.home_fish_pond_on_ic
    const val IconFishPondOff = R.drawable.home_fish_pond_off_ic
    const val IconFoundOn = R.drawable.home_found_on_ic
    const val IconFoundOff = R.drawable.home_found_off_ic
    const val IconEssayOn = R.drawable.home_home_on_ic
    const val IconEssayOff = R.drawable.home_home_off_ic
    const val IconCourseOn = R.drawable.home_course_on_ic
    const val IconCourseOff = R.drawable.home_course_off_ic
    const val IconMeOn = R.drawable.home_me_on_ic
    const val IconMeOff = R.drawable.home_me_off_ic
}

/**
 * A sealed class to make dealing with [ImageVector] and [DrawableRes] icons easier.
 */
sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
