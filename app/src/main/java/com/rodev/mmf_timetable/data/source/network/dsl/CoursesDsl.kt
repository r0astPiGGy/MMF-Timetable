package com.rodev.mmf_timetable.data.source.network.dsl

import com.rodev.mmf_timetable.data.source.network.model.Course
import com.rodev.mmf_timetable.domain.model.Group

fun courses(scope: CoursesBuilderScope.() -> Unit): List<Course> {
    return with(CoursesBuilderImpl()) {
        scope()
        build()
    }
}

private class CoursesBuilderImpl : CoursesBuilderScope {

    private val courses = mutableListOf<Course>()

    override fun course(course: Int, scope: CourseScope.() -> Unit) {
        courses += with(CourseScopeImpl(course = course)) {
            scope()
            build()
        }
    }

    fun build(): List<Course> = courses
}

private class CourseScopeImpl(
    val course: Int
) : CourseScope {

    private val groupList = mutableListOf<Group>()

    override fun group(id: String, name: String) {
        groupList += Group(
            course = course,
            id = id,
            name = name
        )
    }

    fun build(): Course {
        return Course(
            course = course,
            groups = groupList
        )
    }
}