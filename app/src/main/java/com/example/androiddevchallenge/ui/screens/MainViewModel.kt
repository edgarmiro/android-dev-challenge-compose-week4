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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.data.MainRepository
import com.example.androiddevchallenge.domain.Forecast
import com.example.androiddevchallenge.ui.screens.WeatherUiState.Error
import com.example.androiddevchallenge.ui.screens.WeatherUiState.Loading
import com.example.androiddevchallenge.ui.screens.WeatherUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(Loading)
    open val uiState: StateFlow<WeatherUiState> get() = _uiState

    init {
        getForecast()
    }

    open fun getForecast() {
        viewModelScope.launch {
            repository.forecast.collect { result ->
                result.fold(
                    onLeft = { _uiState.value = Error(it) },
                    onRight = { _uiState.value = Success(it) }
                )
            }
        }
    }
}

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    class Error(val throwable: Throwable) : WeatherUiState()
    class Success(val forecast: List<Forecast>) : WeatherUiState()
}
