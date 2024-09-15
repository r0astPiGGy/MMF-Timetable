package com.rodev.mmf_timetable.core.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rodev.mmf_timetable.core.database.model.LessonEntity

object TimetableTypeConverter {

    @TypeConverter
    fun fromLessonList(list: List<com.rodev.mmf_timetable.core.database.model.LessonEntity>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toLessonList(string: String): List<com.rodev.mmf_timetable.core.database.model.LessonEntity> {
        val type = object : TypeToken<List<com.rodev.mmf_timetable.core.database.model.LessonEntity>>() {}.type

        return Gson().fromJson(string, type)
    }

}