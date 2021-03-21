/*
 * Copyright 2021 The Android Open Source Project
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
package com.example.androiddevchallenge.extensions

import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.domain.Condition

val Condition.image: Int
    get() = when (this) {
        Condition.SUN -> R.drawable.ic_sun
        Condition.CLOUD -> R.drawable.ic_cloud
        Condition.FOG -> R.drawable.ic_fog
        Condition.HAIL -> R.drawable.ic_hail
        Condition.MOON -> R.drawable.ic_moon
        Condition.RAIN_HEAVY -> R.drawable.ic_rain_heavy
        Condition.RAIN_LIGHT -> R.drawable.ic_rain_light
        Condition.RAIN_MEDIUM -> R.drawable.ic_rain_medium
        Condition.THUNDER -> R.drawable.ic_thunder
        Condition.SNOW -> R.drawable.ic_snow
        Condition.WIND -> R.drawable.ic_wind
        Condition.WINTERY_MIX -> R.drawable.ic_winterymix
    }

val Condition.description: String
    get() = when (this) {
        Condition.SUN -> "Sunny"
        Condition.CLOUD -> "Cloudy"
        Condition.FOG -> "Foggy"
        Condition.HAIL -> "Hail"
        Condition.MOON -> "Moon"
        Condition.RAIN_HEAVY -> "Heavy rain"
        Condition.RAIN_LIGHT -> "Light rain"
        Condition.RAIN_MEDIUM -> "Medium rain"
        Condition.THUNDER -> "Stormy"
        Condition.SNOW -> "Snowy"
        Condition.WIND -> "Windy"
        Condition.WINTERY_MIX -> "Wintery mix"
    }
