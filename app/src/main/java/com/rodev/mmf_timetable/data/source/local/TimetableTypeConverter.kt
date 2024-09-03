package com.rodev.mmf_timetable.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rodev.mmf_timetable.data.source.local.model.LessonEntity
import com.rodev.mmf_timetable.domain.model.Lesson

object TimetableTypeConverter {

    @TypeConverter
    fun fromLessonList(list: List<LessonEntity>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toLessonList(string: String): List<LessonEntity> {
        val type = object : TypeToken<List<LessonEntity>>() {}.type

        return Gson().fromJson(string, type)
    }

}