package com.rodev.mmf_timetable.data.source.network

import com.rodev.mmf_timetable.data.source.network.dsl.courses
import com.rodev.mmf_timetable.data.source.network.model.Course
import com.rodev.mmf_timetable.data.source.network.model.NetworkLesson
import com.rodev.mmf_timetable.domain.model.Group
import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.service.TimetableNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jsoup.Jsoup
import javax.inject.Inject

internal val BASE_URL = fun(course: Int, group: String): String {
    return "https://mmf.bsu.by/ru/raspisanie-zanyatij/dnevnoe-otdelenie/$course-kurs/$group/"
}

class TimetableNetworkImpl @Inject constructor() : TimetableNetworkDataSource {
    private val courses: List<Course> = courses {
        course(1) {
            (1..9).forEach {
                group("$it-gruppa", "$it группа")
            }
            group("vf", "Военная кафедра")
        }
        (2..4).forEach { course ->
            course(course) {
                (1..10).forEach {
                    group("$it-gruppa", "$it группа")
                }
                group("vf", "Военная кафедра")
            }
        }
    }

    override val allCourses: Flow<List<Int>>
        get() = flowOf(courses.map { it.course })

    override val allGroups: Flow<List<Group>>
        get() = flowOf(courses.flatMap { it.groups })

    private suspend fun getTimetable(url: String): List<Lesson> {
        return Jsoup.connect(url)
            .get()
            .parseLessons()
            .map(NetworkLesson::asExternalModel)
    }

    override suspend fun getTimetable(course: Int, groupId: String)
        = getTimetable(BASE_URL(course, groupId))

}