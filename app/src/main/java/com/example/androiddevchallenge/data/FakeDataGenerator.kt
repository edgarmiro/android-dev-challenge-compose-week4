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
package com.example.androiddevchallenge.data

import com.example.androiddevchallenge.domain.Condition
import com.example.androiddevchallenge.domain.Forecast
import java.util.Calendar
import javax.inject.Inject

class FakeDataGenerator @Inject constructor() {

    val data: List<Forecast>
        get() {
            val calendar = Calendar.getInstance()

            return (1..7).map {
                val date = calendar.time
                calendar.add(Calendar.DAY_OF_MONTH, 1)

                val condition = Condition.values().random()
                val min = (0..8).random()
                val max = (9..20).random()
                val current = (min..max).random()

                Forecast(date, condition, min, max, current)
            }
        }
}
