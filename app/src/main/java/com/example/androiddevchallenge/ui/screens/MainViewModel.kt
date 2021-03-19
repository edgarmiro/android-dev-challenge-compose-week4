package com.example.androiddevchallenge.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.data.MainRepository
import com.example.androiddevchallenge.domain.Weather
import com.example.androiddevchallenge.ui.screens.WeatherUiState.Success
import com.example.androiddevchallenge.ui.screens.WeatherUiState.Loading
import com.example.androiddevchallenge.ui.screens.WeatherUiState.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(Loading)
    val uiState: StateFlow<WeatherUiState> get() = _uiState

    init {
        viewModelScope.launch {
            repository.weather.collect { result ->
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
    class Success(val weather: Weather) : WeatherUiState()
}