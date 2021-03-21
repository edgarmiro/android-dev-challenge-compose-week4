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
package com.example.androiddevchallenge

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androiddevchallenge.domain.Condition
import com.example.androiddevchallenge.domain.Forecast
import com.example.androiddevchallenge.extensions.dayName
import com.example.androiddevchallenge.extensions.description
import com.example.androiddevchallenge.extensions.formattedDate
import com.example.androiddevchallenge.ui.screens.MainScreen
import com.example.androiddevchallenge.ui.screens.MainViewModel
import com.example.androiddevchallenge.ui.screens.WeatherUiState
import com.example.androiddevchallenge.ui.theme.MyTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.joda.time.DateTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class MainScreenInstrumentedTest {

    private val fakeForecast: List<Forecast>
        get() {
            val dateTime = DateTime(2021, 3, 21, 0, 0)
            return listOf(
                Forecast(dateTime.toDate(), Condition.SUN, 0, 10, 5),
                Forecast(dateTime.plusDays(1).toDate(), Condition.CLOUD, 1, 11, 6),
                Forecast(dateTime.plusDays(2).toDate(), Condition.FOG, 2, 12, 7),
                Forecast(dateTime.plusDays(3).toDate(), Condition.HAIL, 3, 13, 8),
                Forecast(dateTime.plusDays(4).toDate(), Condition.MOON, 4, 14, 9),
                Forecast(dateTime.plusDays(5).toDate(), Condition.RAIN_HEAVY, 5, 15, 10),
                Forecast(dateTime.plusDays(6).toDate(), Condition.SNOW, 6, 16, 11),
                Forecast(dateTime.plusDays(7).toDate(), Condition.THUNDER, 7, 17, 12),
            )
        }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val viewModel = mockk<MainViewModel>()

    // Loading UI State
    @Test
    fun given_a_loading_ui_state_when_the_screen_is_created_then_it_shows_the_loading() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Loading)
        addMainScreen()

        val text = composeTestRule.activity.getString(R.string.loading)
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    // Error UI State
    @Test
    fun given_an_error_ui_state_when_the_screen_is_created_then_it_shows_the_retry_button() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Error(Throwable()))
        addMainScreen()

        val text = composeTestRule.activity.getString(R.string.retry)
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    @Test
    fun given_an_error_ui_state_when_the_screen_is_created_then_it_shows_the_error_name() {
        val throwable = TimeoutException()
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Error(throwable))
        addMainScreen()

        composeTestRule.onNodeWithText(throwable.javaClass.simpleName).assertIsDisplayed()
    }

    @Test
    fun given_an_error_ui_state_when_the_retry_button_is_clicked_then_it_retries_the_call() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Error(TimeoutException()))
        every { viewModel.getForecast() } returns Unit
        addMainScreen()

        val text = composeTestRule.activity.getString(R.string.retry)
        composeTestRule.onNodeWithText(text).performClick()

        verify(exactly = 1) { viewModel.getForecast() }
    }

    // Success UI State
    @Test
    fun given_a_success_ui_state_when_the_screen_is_created_then_it_shows_the_city_name() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Success(fakeForecast))
        addMainScreen()

        val text = composeTestRule.activity.getString(R.string.current_city)
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    @Test
    fun given_a_success_ui_state_when_the_city_name_is_clicked_then_it_shows_the_dark_mode_switch_with_off_value() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Success(fakeForecast))
        addMainScreen()

        val textCity = composeTestRule.activity.getString(R.string.current_city)
        composeTestRule.onNodeWithText(textCity).performClick()

        val textDarkMode = composeTestRule.activity.getString(R.string.dark_mode)
        composeTestRule.onNodeWithText(textDarkMode).assertIsDisplayed()

        composeTestRule.onNode(isToggleable()).assertIsOff()
    }

    @Test
    fun given_a_success_ui_state_when_the_screen_is_created_then_it_shows_the_name_of_the_day() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Success(fakeForecast))
        addMainScreen()

        composeTestRule.onNodeWithText(fakeForecast.first().date.dayName).assertIsDisplayed()
    }

    @Test
    fun given_a_success_ui_state_when_the_screen_is_created_then_it_shows_the_formatted_date() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Success(fakeForecast))
        addMainScreen()

        composeTestRule.onNodeWithText(fakeForecast.first().date.formattedDate).assertIsDisplayed()
    }

    @Test
    fun given_a_success_ui_state_when_the_screen_is_created_then_it_shows_the_weather_condition_image() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Success(fakeForecast))
        addMainScreen()

        composeTestRule.onNodeWithContentDescription(fakeForecast.first().condition.description)
            .assertIsDisplayed()
    }

    @Test
    fun given_a_success_ui_state_when_the_screen_is_created_then_it_shows_the_weather_condition_description() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Success(fakeForecast))
        addMainScreen()

        composeTestRule.onNodeWithText(fakeForecast.first().condition.description)
            .assertIsDisplayed()
    }

    @Test
    fun given_a_success_ui_state_when_the_screen_is_created_then_it_shows_the_current_temperature() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Success(fakeForecast))
        addMainScreen()

        val forecast = fakeForecast.first()
        val text =
            composeTestRule.activity.getString(R.string.cd_current_temperature, forecast.current) +
                composeTestRule.activity.getString(
                    R.string.cd_min_max_temperature,
                    forecast.min,
                    forecast.max
                )

        composeTestRule.onNodeWithContentDescription(text).assertIsDisplayed()
    }

    @Test
    fun given_a_success_ui_state_when_the_screen_is_created_then_it_shows_the_next_days_label() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Success(fakeForecast))
        addMainScreen()

        val text = composeTestRule.activity.getString(R.string.next_days)
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    @Test
    fun given_a_success_ui_state_when_the_screen_is_created_then_it_shows_the_next_days_list() {
        every { viewModel.uiState } returns MutableStateFlow(WeatherUiState.Success(fakeForecast))
        addMainScreen()

        val tag = composeTestRule.activity.getString(R.string.tag_next_days)
        composeTestRule.onNodeWithTag(tag).assertIsDisplayed()
    }

    private fun addMainScreen() {
        composeTestRule.setContent {
            MyTheme {
                MainScreen(viewModel)
            }
        }
    }
}
