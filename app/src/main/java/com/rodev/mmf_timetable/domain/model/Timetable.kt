package com.rodev.mmf_timetable.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Timetable(
    val allLessons: List<Lesson>,
    val course: Int,
    val group: String,
    val createdDate: Long = System.currentTimeMillis(),
    @PrimaryKey val id: String = createId(course, group)
) {

    companion object {

        fun createId(course: Int, group: String): String {
            return "$course-$group"
        }

    }

}
