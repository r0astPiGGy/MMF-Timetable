package com.rodev.mmf_timetable.core.model.data

data class Teacher(
    val id: Long,
    val name: String,
    val fullName: String?,
    val imageUrl: String?,
    val email: String?,
    val phone: String?,
    val position: String?
)
