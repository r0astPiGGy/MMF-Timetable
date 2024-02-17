package com.rodev.mmf_timetable.domain.repository

interface UserPreferences {

    fun setString(key: String, value: String)

    fun getString(key: String): String

}