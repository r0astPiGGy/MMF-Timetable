package com.rodev.mmf_timetable

import com.rodev.mmf_timetable.data.source.network.TimetableNetworkImpl
import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.model.Weekday
import com.rodev.mmf_timetable.domain.resource.Resource
import com.rodev.mmf_timetable.domain.service.TimetableNetworkDataSource
import com.rodev.mmf_timetable.utils.toDisplayableTime
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.util.UUID

class Test {

    private suspend fun getTimetable(): List<Lesson> =
        TimetableNetworkImpl().getTimetable(1, "9-gruppa")

    @Test
    fun displayTest() {
        println(360.toDisplayableTime())
        println(245.toDisplayableTime())
    }

    data class CopyTest(
        val number: Int,
        val name: String,
        val id: UUID = UUID.randomUUID()
    )

    @Test
    fun copyTest() {
        val data = CopyTest(1, "Test")

        val copy = data.copy(name = "Test", number = 2)

        Assert.assertNotEquals(copy, data)
    }

    @Test
    fun test() = runBlocking {
        val result = getTimetable().groupBy { it.weekday }

        Weekday.values().forEach {
            println("Неделя ${it.name}:")
            result[it]?.forEach { lesson ->
                println(" $lesson")
            }
        }
    }

}