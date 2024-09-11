package com.rodev.mmf_timetable.core.network

import com.rodev.mmf_timetable.core.network.model.NetworkClassroom
import com.rodev.mmf_timetable.core.network.model.NetworkCourse
import com.rodev.mmf_timetable.core.network.model.NetworkLesson
import com.rodev.mmf_timetable.core.network.model.NetworkSubgroupSubject
import com.rodev.mmf_timetable.core.network.model.NetworkTeacher

interface TimetableNetworkDataSource {

    suspend fun getTeachers(): List<NetworkTeacher>

    suspend fun getCourses(): List<NetworkCourse>

    suspend fun getClassrooms(): List<NetworkClassroom>

    suspend fun getLessons(group: String): List<NetworkLesson>

    suspend fun getSubgroupSubjects(group: String): List<NetworkSubgroupSubject>

}