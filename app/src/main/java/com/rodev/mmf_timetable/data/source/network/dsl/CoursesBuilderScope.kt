package com.rodev.mmf_timetable.data.source.network.dsl

interface CoursesBuilderScope {

    fun course(course: Int, scope: CourseScope.() -> Unit)

}