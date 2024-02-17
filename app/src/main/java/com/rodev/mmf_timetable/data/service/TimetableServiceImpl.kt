package com.rodev.mmf_timetable.data.service

import com.rodev.mmf_timetable.domain.mapper.LessonMapper
import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.service.ApiResult
import com.rodev.mmf_timetable.domain.service.Course
import com.rodev.mmf_timetable.domain.service.TimetableService
import org.jsoup.Jsoup

internal val BASE_URL = fun(course: Int, group: String): String {
    return "https://mmf.bsu.by/ru/raspisanie-zanyatij/dnevnoe-otdelenie/$course-kurs/$group/"
}

class TimetableServiceImpl : TimetableService {

    override val courses: List<Course> = courses {
        course(1) {
            for (i in 1..9) {
                group("$i-gruppa", "$i группа")
            }
            group("vf", "Военная кафедра")
        }
        for (course in 2..4) {
            course(course) {
                for (i in 1..10) {
                    group("$i-gruppa", "$i группа")
                }
                group("vf", "Военная кафедра")
            }
        }
    }

    override fun getCourse(course: Int): Course {
        return courses.first { it.course == course }
    }

    private suspend fun getTimetable(course: Int, group: String): ApiResult<List<Lesson>> {
        return getTimetable(BASE_URL(course, group))
    }

    private suspend fun getTimetable(url: String): ApiResult<List<Lesson>> {
        return try {
            val doc = Jsoup.connect(url).get()

            ApiResult.Success(
                doc.parseLessons().map(LessonMapper::toLesson)
            )

        } catch (e: Throwable) {
            ApiResult.Exception(e)
        }
    }

    private inner class CourseImpl(
        override val course: Int,
        override val groupList: List<Course.Group>
    ) : Course {

        override fun getGroup(id: String): Course.Group {
            return groupList.first { it.id == id }
        }
    }

    private inner class GroupImpl(
        val course: Int,
        override val name: String,
        override val id: String
    ) : Course.Group {

        override suspend fun getTimetable(): ApiResult<List<Lesson>> {
            return getTimetable(course, id)
        }
    }

    interface CourseScope {

        fun group(id: String, name: String)

    }

    interface CoursesBuilderScope {

        fun course(course: Int, scope: CourseScope.() -> Unit)

    }

    private fun courses(scope: CoursesBuilderScope.() -> Unit): List<Course> {
        return with(CoursesBuilderImpl()) {
            scope()
            build()
        }
    }

    private inner class CoursesBuilderImpl : CoursesBuilderScope {

        private val courses = mutableListOf<Course>()

        override fun course(course: Int, scope: CourseScope.() -> Unit) {
            courses += with(CourseScopeImpl(course = course)) {
                scope()
                build()
            }
        }

        fun build(): List<Course> = courses
    }

    private inner class CourseScopeImpl(
        val course: Int
    ) : CourseScope {

        private val groupList = mutableListOf<Course.Group>()

        override fun group(id: String, name: String) {
            groupList += GroupImpl(course, name, id)
        }

        fun build(): Course {
            return CourseImpl(
                course = course,
                groupList = groupList
            )
        }
    }

}