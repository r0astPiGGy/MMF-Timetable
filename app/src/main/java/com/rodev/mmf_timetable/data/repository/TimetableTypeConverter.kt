package com.rodev.mmf_timetable.data.repository

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rodev.mmf_timetable.domain.model.Lesson

object TimetableTypeConverter {

    @TypeConverter
    fun fromLessonList(list: List<Lesson>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toLessonList(string: String): List<Lesson> {
        val type = object : TypeToken<List<Lesson>>() {}.type

        return Gson().fromJson(string, type)
    }

}