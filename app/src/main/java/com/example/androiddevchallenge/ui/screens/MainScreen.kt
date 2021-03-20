package com.example.androiddevchallenge.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.domain.Forecast
import com.example.androiddevchallenge.domain.Weather
import com.example.androiddevchallenge.extensions.description
import com.example.androiddevchallenge.extensions.image
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.shapes
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel(), onChangeTheme: () -> Unit) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is WeatherUiState.Loading -> MainLoadingContent()
        is WeatherUiState.Error -> MainErrorContent(throwable = uiState.throwable)
        is WeatherUiState.Success -> MainContent(
            weather = uiState.weather,
            forecast = forecast,
            onChangeTheme = onChangeTheme
        )
    }
}

@Composable
private fun MainLoadingContent() {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Loading", color = Color.Gray)
        }
    }
}

@Composable
private fun MainErrorContent(throwable: Throwable) {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = throwable.javaClass.simpleName, color = Color.Red)
        }
    }
}

@Composable
private fun MainContent(weather: Weather, forecast: List<Forecast>, onChangeTheme: () -> Unit) {
    Backdrop(onChangeTheme = onChangeTheme) {
        Surface(color = MaterialTheme.colors.background) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    MainWeather(
                        weather = weather,
                        modifier = Modifier.fillMaxWidth()
                    )
                    MainTemperature(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    contentPadding = PaddingValues(all = 2.dp)
                ) {
                    items(forecast) {
                        ForecastItem(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun MainWeather(weather: Weather, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.clip(shape = MaterialTheme.shapes.large),
        elevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Saturday",
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(top = 16.dp),
            )
            Text(
                text = "March 20, 2021",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(top = 8.dp),
            )
            Icon(
                painter = painterResource(id = weather.image),
                modifier = Modifier
                    .size(200.dp)
                    .padding(vertical = 4.dp),
                contentDescription = weather.description,
            )
            Text(
                text = weather.description,
                style = MaterialTheme.typography.h1,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun MainTemperature(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium),
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "11ºC",
                fontSize = 40.sp,
                style = MaterialTheme.typography.h1,
            )
            Column {
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "MIN", fontSize = 14.sp, modifier = Modifier.width(64.dp))
                    Text(text = "2ºC", fontSize = 14.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "MAX", fontSize = 14.sp, modifier = Modifier.width(64.dp))
                    Text(text = "14ºC", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun ForecastItem(forecast: Forecast) {
    Card(
        modifier = Modifier
            .width(128.dp)
            .padding(vertical = 2.dp, horizontal = 2.dp)
            .clip(shape = shapes.medium),
        elevation = 4.dp,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
        ) {
            Text(
                text = forecast.day,
                style = MaterialTheme.typography.h3,
            )
            Icon(
                painter = painterResource(id = forecast.weather.image),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(56.dp),
                contentDescription = forecast.weather.description,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "2ºC", fontSize = 14.sp)
                Text(text = "13ºC", fontSize = 14.sp)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "MIN", fontSize = 14.sp)
                Text(text = "MAX", fontSize = 14.sp)
            }
            Text(
                text = forecast.weather.description,
                style = MaterialTheme.typography.h1,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Backdrop(onChangeTheme: () -> Unit, frontLayerContent: @Composable () -> Unit) {
    val backdropState = rememberBackdropScaffoldState(BackdropValue.Concealed)

    BackdropScaffold(
        scaffoldState = backdropState,
        appBar = { BackdropAppBar(backdropState) },
        backLayerBackgroundColor = MaterialTheme.colors.background,
        backLayerContent = { BackdropBackContent(onChangeTheme) },
        frontLayerContent = frontLayerContent
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BackdropAppBar(backdropState: BackdropScaffoldState) {
    val selectedCity = remember { mutableStateOf("Alcoy") }
    val scope = rememberCoroutineScope()

    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = selectedCity.value,
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    scope.launch {
                        if (backdropState.currentValue == BackdropValue.Concealed) {
                            backdropState.reveal()
                        } else {
                            backdropState.conceal()
                        }
                    }
                }
            ),
    )
}

@Composable
private fun BackdropBackContent(onChangeTheme: () -> Unit) {
    val darkModeSelected = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 32.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        Row {
            Text("Dark mode", modifier = Modifier.width(100.dp))
            Switch(checked = darkModeSelected.value, onCheckedChange = {
                darkModeSelected.value = !darkModeSelected.value
                onChangeTheme()
            })
        }
    }
}

val forecast = listOf(
    Forecast("Monday", Weather.RAIN_HEAVY),
    Forecast("Tuesday", Weather.RAIN_MEDIUM),
    Forecast("Wednesday", Weather.RAIN_LIGHT),
    Forecast("Thursday", Weather.CLOUD),
    Forecast("Friday", Weather.FOG),
    Forecast("Saturday", Weather.THUNDER),
    Forecast("Sunday", Weather.SUN),
)

@Preview("Light Theme", device = Devices.PIXEL_4)
@Composable
fun LightPreview() {
    MyTheme {
        MainContent(weather = Weather.CLOUD, forecast = forecast) {}
    }
}

@Preview("Dark Theme", device = Devices.PIXEL_4)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MainContent(weather = Weather.CLOUD, forecast = forecast) {}
    }
}

@Preview("Forecast Light Theme")
@Composable
fun ForecastLightPreview() {
    MyTheme {
        ForecastItem(forecast = Forecast("Monday", Weather.FOG))
    }
}

@Preview("Forecast Dark Theme")
@Composable
fun FoecastDarkPreview() {
    MyTheme(darkTheme = true) {
        ForecastItem(forecast = Forecast("Monday", Weather.FOG))
    }
}

@Preview("Main Temperature")
@Composable
fun TemperatureLightPreview() {
    MyTheme {
        MainTemperature(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}