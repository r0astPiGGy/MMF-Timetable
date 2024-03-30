package com.rodev.mmf_timetable.presentation.screen.home_screen.state

import androidx.compose.runtime.Stable
import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.model.UserInfo
import com.rodev.mmf_timetable.domain.model.Weekday
import com.rodev.mmf_timetable.domain.service.Course
import com.rodev.mmf_timetable.utils.DateUtils

typealias MappedTimetable = Map<Weekday, List<AvailableLesson>>

@Stable
data class HomeScreenState(
    val userInfo: UserInfo? = null,
    val currentStudyWeek: Long? = null,
    val courseList: List<Course> = emptyList(),
    val timetable: MappedTimetable? = emptyMap(),
    val result: HomeScreenResult = HomeScreenResult.Idle,
    val currentLesson: Lesson? = null,
    val courseEditDialogOpened: Boolean = false,
    val weekdays: List<Weekday> = timetable.provideWeekdays(),
    val todayWeekday: Weekday = DateUtils.getCurrentWeekday()
)

private val allWeekdays = arrayOf(
    Weekday.MONDAY,
    Weekday.TUESDAY,
    Weekday.WEDNESDAY,
    Weekday.THURSDAY,
    Weekday.FRIDAY,
    Weekday.SATURDAY
)

fun MappedTimetable?.provideWeekdays(): List<Weekday> {
    if (this == null) return emptyList()

    return allWeekdays.filter { containsKey(it) }
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