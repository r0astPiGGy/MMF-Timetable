package com.rodev.mmf_timetable.core.domain

import com.rodev.mmf_timetable.core.model.data.Availability
import com.rodev.mmf_timetable.core.model.data.Lesson
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.plus
import javax.inject.Inject

class GetAvailableLessonsUseCase @Inject constructor(
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


            return (d2 - d1 + 1) % 2 != 0
        }

        private fun Availability.isAvailable(lessonDate: LocalDate): Boolean {
            return when (this) {
                is Availability.After -> lessonDate >= date
                Availability.EvenWeek -> isEvenWeek(lessonDate)
                Availability.OddWeek -> !isEvenWeek(lessonDate)
                is Availability.Except -> dates.all { it != lessonDate }
                is Availability.InRange -> lessonDate >= from && lessonDate <= to
                is Availability.Only -> dates.all { it == lessonDate }
                is Availability.Until -> lessonDate <= date
            }
        }

        fun Lesson.isAvailable(date: LocalDate, subGroups: Set<Long>): Boolean {
            if (subGroup != null && !subGroups.contains(subGroup!!.id)) { return false }

            return availability.all { it.isAvailable(date) }
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