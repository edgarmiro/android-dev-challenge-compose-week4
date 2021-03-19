package com.example.androiddevchallenge.ui.screens

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.domain.Weather
import com.example.androiddevchallenge.ui.theme.MyTheme

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is WeatherUiState.Loading -> MainLoadingContent()
        is WeatherUiState.Error -> MainErrorContent(throwable = uiState.throwable)
        is WeatherUiState.Success -> MainContent(weather = uiState.weather)
    }
}

@Composable
private fun MainLoadingContent() {
    Surface(color = MaterialTheme.colors.background) {
        Text(text = "Loading", color = Color.Gray)
    }
}

@Composable
private fun MainErrorContent(throwable: Throwable) {
    Surface(color = MaterialTheme.colors.background) {
        Text(text = throwable.javaClass.simpleName, color = Color.Red)
    }
}

@Composable
private fun MainContent(weather: Weather) {
    Surface(color = MaterialTheme.colors.background) {
        WeatherIcon(weather)
    }
}

@Composable
private fun WeatherIcon(weather: Weather) {
    Icon(
        imageVector = when (weather) {
            Weather.SUNNY -> Icons.Filled.WbSunny
            Weather.CLOUDY -> Icons.Filled.WbCloudy
        },
        contentDescription = "TODO",
    )
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MainContent(weather = Weather.CLOUDY)
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MainContent(weather = Weather.CLOUDY)
    }
}