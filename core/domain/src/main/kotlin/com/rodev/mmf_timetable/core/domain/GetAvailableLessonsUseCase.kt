package com.rodev.mmf_timetable.core.domain

import android.util.Log.d
import com.rodev.mmf_timetable.core.model.data.Availability
import com.rodev.mmf_timetable.core.model.data.AvailableLesson
import com.rodev.mmf_timetable.core.model.data.Group
import com.rodev.mmf_timetable.core.model.data.Lesson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class GetAvailableLessonsUseCase @Inject constructor(
    val getUserSelectedTimetable: GetUserSelectedTimetableUseCase
) {


    companion object {
        private fun isEvenWeek(date: LocalDate): Boolean {
            // Set to nearest Thursday: current date + 4 - current day number
            // Make Sunday's day number 7
            fun week(date: LocalDate): Int {
                // Adjust to the nearest Thursday: current date + 4 - current day number
                val dayOfWeek = date.dayOfWeek
                val daysToThursday = 4 - (dayOfWeek.isoDayNumber % 7)
                val adjustedDate = date.plus(daysToThursday, DateTimeUnit.DAY)

                // Get the first day of the year
                val yearStart = LocalDate(date.year, 1, 1)

                // Calculate the difference in days
                val daysBetween = yearStart.daysUntil(adjustedDate)

                // Calculate the week number
                return (daysBetween / 7) + 1
            }

            val d2 = week(date)
            val year = if (date.monthNumber >= Month.SEPTEMBER.number) {
                date.year
            } else {
                date.year - 1
            }
            val d1 = week(LocalDate(year = year, Month.SEPTEMBER, dayOfMonth = 1))


            return (d2 - d1 + 1) % 2 == 0
        }

        private fun Availability.isAvailable(): Boolean {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val todayDate = today.date

            return when (this) {
                is Availability.After -> todayDate >= date
                Availability.EvenWeek -> isEvenWeek(todayDate)
                Availability.OddWeek -> !isEvenWeek(todayDate)
                is Availability.Except -> dates.all { it != todayDate }
                is Availability.InRange -> todayDate >= from && todayDate <= to
                is Availability.Only -> dates.all { it == todayDate }
                is Availability.Until -> todayDate <= date
            }
        }

        fun Lesson.isAvailable(subGroups: Set<Long>): Boolean {
            if (subGroup != null && !subGroups.contains(subGroup!!.id)) { return false }

            return availability.all { it.isAvailable() }
        }
    }

//    operator fun invoke(): Flow<List<AvailableLesson>> {
//        return getUserSelectedTimetable()
//            .map { lessons ->
//                lessons.map {
//                    AvailableLesson(
//                        lesson = it,
//                        isAvailable = it.isAvailable()
//                    )
//                }
//            }
//    }

}