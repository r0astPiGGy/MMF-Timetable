package com.rodev.mmf_timetable.data.source.local.model

import androidx.room.Entity
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.Weekday

@Entity
data class LessonEntity(
    val weekday: com.rodev.mmf_timetable.core.model.data.Weekday,
    val type: Type?,
    val classroom: String,
    val subject: String,
    val timeStartMinutes: Int,
    val timeEndMinutes: Int,
    val teacher: String,
    val remarks: String?,
    val week: WeekType?
) {

    enum class Type {
        LECTURE,
        PRACTICE
    }

    enum class WeekType {
        ODD,
        EVEN
    }

    fun asExternalModel(): com.rodev.mmf_timetable.core.model.data.Lesson {
        return com.rodev.mmf_timetable.core.model.data.Lesson(
            type = type?.let { com.rodev.mmf_timetable.core.model.data.Lesson.Type.valueOf(it.name) },
            weekType = week?.let {
                com.rodev.mmf_timetable.core.model.data.Lesson.WeekType.valueOf(
                    it.name
                )
            },
            weekday = weekday,
            classroom = classroom,
            subject = subject,
            timeStartMinutes = timeStartMinutes,
            timeEndMinutes = timeEndMinutes,
            teacher = teacher,
            remarks = remarks
        )
    }

    companion object {

        fun fromExternalModel(model: com.rodev.mmf_timetable.core.model.data.Lesson): LessonEntity {
            with(model) {
                return LessonEntity(
                    week = weekType?.let { LessonEntity.WeekType.valueOf(it.name) },
                    type = type?.let { LessonEntity.Type.valueOf(it.name) },
                    weekday = weekday,
                    classroom = classroom,
                    subject = subject,
                    timeStartMinutes = timeStartMinutes,
                    timeEndMinutes = timeEndMinutes,
                    teacher = teacher,
                    remarks = remarks
                )
            }
        }

    }
}


