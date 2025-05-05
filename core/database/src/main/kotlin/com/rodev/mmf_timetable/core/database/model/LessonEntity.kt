package com.rodev.mmf_timetable.core.database.model

import androidx.room.Entity
import com.rodev.mmf_timetable.core.model.data.Lesson
import com.rodev.mmf_timetable.core.model.data.Weekday

@Entity
data class LessonEntity(
    val weekday: Weekday,
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

    fun asExternalModel(): Lesson {
        TODO()
//        return Lesson(
//            type = type?.let { Lesson.Type.valueOf(it.name) },
//            weekType = week?.let {
//                Lesson.WeekType.valueOf(
//                    it.name
//                )
//            },
//            weekday = weekday,
//            classroom = classroom,
//            subject = subject,
//            timeStartMinutes = timeStartMinutes,
//            timeEndMinutes = timeEndMinutes,
//            teacher = teacher,
//            remarks = remarks
//        )
    }

    companion object {

        fun fromExternalModel(model: Lesson): LessonEntity {
            TODO()
//            with(model) {
//                return LessonEntity(
//                    week = weekType?.let { com.rodev.mmf_timetable.core.database.model.LessonEntity.WeekType.valueOf(it.name) },
//                    type = type?.let { com.rodev.mmf_timetable.core.database.model.LessonEntity.Type.valueOf(it.name) },
//                    weekday = weekday,
//                    classroom = classroom,
//                    subject = subject,
//                    timeStartMinutes = timeStartMinutes,
//                    timeEndMinutes = timeEndMinutes,
//                    teacher = teacher,
//                    remarks = remarks
//                )
//            }
        }

    }
}


