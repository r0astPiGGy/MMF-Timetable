package com.rodev.mmf_timetable

import com.rodev.mmf_timetable.data.service.TimetableServiceImpl
import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.model.Weekday
import com.rodev.mmf_timetable.domain.service.ApiResult
import com.rodev.mmf_timetable.domain.service.TimetableService
import com.rodev.mmf_timetable.utils.toDisplayableTime
import kotlinx.coroutines.runBlocking
import org.junit.Test

class Test {

    private suspend fun getTimetable(): ApiResult<List<Lesson>> {
        val timetableService: TimetableService = TimetableServiceImpl()
        return timetableService.getCourse(1).getGroup("9-gruppa").getTimetable()
    }

    @Test
    fun displayTest() {
        println(360.toDisplayableTime())
        println(245.toDisplayableTime())
    }

    @Test
    fun test() = runBlocking {
        when (val result = getTimetable()) {
            is ApiResult.Exception -> {
                throw result.exception
            }
            is ApiResult.Failure -> {
                throw Exception(result.error)
            }
            is ApiResult.Success -> {
                val mappedLesson = hashMapOf<Weekday, MutableList<Lesson>>()

                for (timetable in result.data) {
                    mappedLesson.computeIfAbsent(timetable.weekday) { mutableListOf() }.add(timetable)
                }

                Weekday.values().forEach {
                    println("Неделя ${it.name}:")
                    mappedLesson[it]?.forEach { lesson ->
                        println(" $lesson")
                    }
                }
            }
        }
    }

}