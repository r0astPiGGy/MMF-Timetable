package com.rodev.mmf_timetable.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimetableEntity(
    val allLessons: List<LessonEntity>,
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
