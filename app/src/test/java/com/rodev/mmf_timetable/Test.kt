package com.rodev.mmf_timetable

import com.rodev.mmf_timetable.data.source.network.TimetableNetworkImpl
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.Weekday
import com.rodev.mmf_timetable.utils.toDisplayableTime
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.util.UUID

class Test {

    private suspend fun getTimetable(): List<com.rodev.mmf_timetable.core.model.data.Lesson> =
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

        com.rodev.mmf_timetable.core.model.data.Weekday.values().forEach {
            println("Неделя ${it.name}:")
            result[it]?.forEach { lesson ->
                println(" $lesson")
            }
        }
    }

}