package com.example.androiddevchallenge.extensions

import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.domain.Weather

val Weather.image: Int
    get() = when (this) {
        Weather.SUN -> R.drawable.ic_sun
        Weather.CLOUD -> R.drawable.ic_cloud
        Weather.FOG -> R.drawable.ic_fog
        Weather.HAIL -> R.drawable.ic_hail
        Weather.MOON -> R.drawable.ic_moon
        Weather.RAIN_HEAVY -> R.drawable.ic_rain_heavy
        Weather.RAIN_LIGHT -> R.drawable.ic_rain_light
        Weather.RAIN_MEDIUM -> R.drawable.ic_rain_medium
        Weather.THUNDER -> R.drawable.ic_thunder
        Weather.SNOW -> R.drawable.ic_snow
        Weather.WIND -> R.drawable.ic_wind
        Weather.WINTERY_MIX -> R.drawable.ic_winterymix
    }
