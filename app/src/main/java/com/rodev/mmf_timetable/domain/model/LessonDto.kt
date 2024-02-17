package com.rodev.mmf_timetable.domain.model

data class LessonDto(
    val weekday: String,
    val time: String,
    val remarks: String,
    val subjectTeachers: String,
    val lecturePractice: String,
    val room: String
)
