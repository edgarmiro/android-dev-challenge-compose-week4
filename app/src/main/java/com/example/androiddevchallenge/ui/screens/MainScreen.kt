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
import androidx.compose.material.Button
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.data.FakeDataGenerator
import com.example.androiddevchallenge.domain.Forecast
import com.example.androiddevchallenge.extensions.contentDescription
import com.example.androiddevchallenge.extensions.dayName
import com.example.androiddevchallenge.extensions.description
import com.example.androiddevchallenge.extensions.formattedDate
import com.example.androiddevchallenge.extensions.image
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.shapes
import kotlinx.coroutines.launch
import java.util.concurrent.TimeoutException

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel(), onChangeTheme: () -> Unit = {}) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is WeatherUiState.Loading -> MainLoadingContent()
        is WeatherUiState.Error -> MainErrorContent(
            throwable = uiState.throwable,
            onRetryClick = viewModel::getForecast
        )
        is WeatherUiState.Success -> MainContent(
            current = uiState.forecast.first(),
            forecast = uiState.forecast.drop(1),
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
            Text(text = stringResource(id = R.string.loading), style = MaterialTheme.typography.h1)
        }
    }
}

@Composable
private fun MainErrorContent(throwable: Throwable, onRetryClick: () -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = throwable.javaClass.simpleName, style = MaterialTheme.typography.h1)
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = onRetryClick
            ) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}

@Composable
private fun MainContent(current: Forecast, forecast: List<Forecast>, onChangeTheme: () -> Unit) {
    Backdrop(onChangeTheme = onChangeTheme) {
        Surface(color = MaterialTheme.colors.background) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    MainWeather(
                        modifier = Modifier.fillMaxWidth(),
                        forecast = current,
                    )
                    MainTemperature(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        forecast = current,
                    )
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = stringResource(id = R.string.next_days),
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(tag = stringResource(id = R.string.tag_next_days)),
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
}

@Composable
private fun MainWeather(forecast: Forecast, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.clip(shape = MaterialTheme.shapes.large),
        elevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = forecast.date.contentDescription
                    }
            ) {
                Text(
                    text = forecast.date.dayName,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(
                    text = forecast.date.formattedDate,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics(mergeDescendants = true) {}
            ) {
                Icon(
                    painter = painterResource(id = forecast.condition.image),
                    modifier = Modifier
                        .size(200.dp)
                        .padding(vertical = 4.dp),
                    contentDescription = forecast.condition.description,
                )
                Text(
                    text = forecast.condition.description,
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun MainTemperature(modifier: Modifier = Modifier, forecast: Forecast) {
    val cdTemperature = stringResource(id = R.string.cd_current_temperature, forecast.current) +
        stringResource(id = R.string.cd_min_max_temperature, forecast.min, forecast.max)

    Card(
        elevation = 4.dp,
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .semantics(mergeDescendants = true) {
                contentDescription = cdTemperature
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stringResource(R.string.degrees, forecast.current),
                fontSize = 40.sp,
                style = MaterialTheme.typography.h1,
            )
            Column {
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(R.string.min),
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.width(64.dp)
                    )
                    Text(
                        text = stringResource(R.string.degrees, forecast.min),
                        style = MaterialTheme.typography.h4
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(R.string.max),
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.width(64.dp)
                    )
                    Text(
                        text = stringResource(R.string.degrees, forecast.max),
                        style = MaterialTheme.typography.h4
                    )
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
                text = forecast.date.dayName,
                style = MaterialTheme.typography.h3,
            )
            Icon(
                painter = painterResource(id = forecast.condition.image),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(56.dp),
                contentDescription = forecast.condition.description,
            )
            Text(
                text = forecast.condition.description,
                style = MaterialTheme.typography.h1,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = stringResource(R.string.degrees, forecast.min),
                    style = MaterialTheme.typography.h4,
                )
                Text(
                    text = stringResource(R.string.degrees, forecast.max),
                    style = MaterialTheme.typography.h4,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = stringResource(R.string.min),
                    style = MaterialTheme.typography.h5,
                )
                Text(
                    text = stringResource(R.string.max),
                    style = MaterialTheme.typography.h5,
                )
            }
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
    val city = stringResource(R.string.current_city)
    val selectedCity = remember { mutableStateOf(city) }
    val scope = rememberCoroutineScope()

    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = selectedCity.value,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h1,
        fontSize = 30.sp,
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
            Text(stringResource(R.string.dark_mode), modifier = Modifier.width(100.dp))
            Switch(
                checked = darkModeSelected.value,
                onCheckedChange = {
                    darkModeSelected.value = !darkModeSelected.value
                    onChangeTheme()
                }
            )
        }
    }
}

private val fakeData: List<Forecast>
    get() {
        val generator = FakeDataGenerator()
        return generator.data
    }

@Preview("Loading Light Theme", device = Devices.PIXEL_4)
@Composable
fun LoadingLightPreview() {
    MyTheme {
        MainLoadingContent()
    }
}

@Preview("Loading Dark Theme", device = Devices.PIXEL_4)
@Composable
fun LoadingDarkPreview() {
    MyTheme(darkTheme = true) {
        MainLoadingContent()
    }
}

@Preview("Error Light Theme", device = Devices.PIXEL_4)
@Composable
fun ErrorLightPreview() {
    MyTheme {
        MainErrorContent(TimeoutException()) {}
    }
}

@Preview("Error Dark Theme", device = Devices.PIXEL_4)
@Composable
fun ErrorDarkPreview() {
    MyTheme(darkTheme = true) {
        MainErrorContent(TimeoutException()) {}
    }
}

@Preview("Light Theme", device = Devices.PIXEL_4)
@Composable
fun LightPreview() {
    MyTheme {
        MainContent(current = fakeData.first(), forecast = fakeData) {}
    }
}

@Preview("Dark Theme", device = Devices.PIXEL_4)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MainContent(current = fakeData.first(), forecast = fakeData) {}
    }
}

@Preview("Forecast Light Theme")
@Composable
fun ForecastLightPreview() {
    MyTheme {
        ForecastItem(forecast = fakeData.first())
    }
}

@Preview("Forecast Dark Theme")
@Composable
fun FoecastDarkPreview() {
    MyTheme(darkTheme = true) {
        ForecastItem(forecast = fakeData.first())
    }
}

@Preview("Main Temperature")
@Composable
fun TemperatureLightPreview() {
    MyTheme {
        MainTemperature(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            forecast = fakeData.first()
        )
    }
}
