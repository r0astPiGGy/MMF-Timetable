package com.rodev.mmf_timetable.domain.use_case

import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.model.WidgetState
import com.rodev.mmf_timetable.utils.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class GetWidgetStateUseCase @Inject constructor(
    private val getUserSelectedTimetable: GetUserSelectedTimetableUseCase
) {

    private fun Lesson.timeSpanIncludes(totalTime: Int): Boolean {
        return totalTime in timeStartMinutes..timeEndMinutes
    }

    private fun List<Lesson>.findCurrentLessons(time: Int): List<Lesson> {
        return filter { it.timeSpanIncludes(time) }
    }

    private fun findClosestLessons(totalTime: Int, lessons: List<Lesson>): List<Lesson> {
        var lastMax = Int.MAX_VALUE
        val closestLessons = mutableListOf<Lesson>()

        for (lesson in lessons) {
            // skip if lesson already ended
            if (lesson.timeEndMinutes < totalTime) {
                continue
            }

            with (lesson) {
                val range = timeStartMinutes - totalTime

                if (range == lastMax) {
                    closestLessons.add(lesson)
                }

                if (range in 0 until lastMax) {
                    lastMax = range
                    closestLessons.clear()
                    closestLessons.add(lesson)
                }
            }
        }

        return closestLessons
    }

    private fun getWidgetState(lessons: List<Lesson>): WidgetState {
        val calendar = Calendar.getInstance()

        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val totalTime = (hour * 60) + minute

        val currentLessons = lessons.findCurrentLessons(totalTime)

        if (currentLessons.isEmpty()) {
            val closestLessons = findClosestLessons(totalTime, lessons)

            if (closestLessons.isEmpty()) {
                return WidgetState.NoLesson
            }

            val closestLesson = closestLessons.first()
            val minutesBeforeStart = closestLesson.timeStartMinutes - totalTime

            return WidgetState.NextLesson(
                lesson = closestLesson,
                minutesBeforeStart = minutesBeforeStart
            )
        }

        val currentLesson = currentLessons.first()

        val startTime = currentLesson.timeStartMinutes
        val endTime = currentLesson.timeEndMinutes

        val totalTimeInRange = totalTime - startTime
        val range = endTime - startTime

        return WidgetState.OngoingLesson(
            lesson = currentLesson,
            minutesRemaining = max(0, endTime - totalTime),
            progress = min(1f, totalTimeInRange.toFloat() / range.toFloat())
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<WidgetState?> =
        getUserSelectedTimetable()
            .mapLatest {
                if (it == null) {
                    null
                } else {
                    val currentTimetable = it.lessons[DateUtils.getCurrentWeekday()]

                    if (currentTimetable.isNullOrEmpty()) {
                        null
                    } else {
                        getWidgetState(currentTimetable)
                    }
                }
            }
}