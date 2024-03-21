package com.rodev.mmf_timetable.presentation.screen.home_screen.state

import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.model.UserInfo
import com.rodev.mmf_timetable.domain.model.Weekday
import com.rodev.mmf_timetable.domain.service.Course

data class HomeScreenState(
    val userInfo: UserInfo? = null,
    val currentWeek: Long? = null,
    val courseList: List<Course> = emptyList(),
    val timetable: Map<Weekday, List<AvailableLesson>>? = emptyMap(),
    val result: HomeScreenResult = HomeScreenResult.Idle,
    val currentLesson: Lesson? = null,
    val courseEditDialogOpened: Boolean = false
)

private val weekdays = arrayOf(
    Weekday.MONDAY,
    Weekday.TUESDAY,
    Weekday.WEDNESDAY,
    Weekday.THURSDAY,
    Weekday.FRIDAY,
    Weekday.SATURDAY
)

fun HomeScreenState.provideWeekdays(): List<Weekday> {
    if (timetable == null) return emptyList()

    return weekdays.filter { timetable.containsKey(it) }
}

sealed class HomeScreenResult {

    object Loading : HomeScreenResult()

    class Error(
        val message: String,
        private val onConsume: () -> Unit
    ) : HomeScreenResult() {

        fun consume() {
            onConsume()
        }

    }

    object Idle : HomeScreenResult()

}