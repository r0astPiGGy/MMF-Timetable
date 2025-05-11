package com.rodev.mmf_timetable.widget

import com.rodev.mmf_timetable.core.domain.GetUserSelectedTimetableUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class WidgetStateFactory @Inject constructor(
    private val getUserSelectedTimetable: GetUserSelectedTimetableUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<WidgetState?> =
        getUserSelectedTimetable()
            .mapLatest { it ->
//                val calendar = Calendar.getInstance()
//                val week = calendar[Calendar.DAY_OF_WEEK]
//
//                val lessons = it.groupBy { it.weekday }
//
//                val currentTimetable = it.lessons[Weekday.values()[week - 1]]
//                if (currentTimetable.isNullOrEmpty()) {
//                    null
//                } else {
//                    getWidgetState(currentTimetable)
//                }
                null
            }


}