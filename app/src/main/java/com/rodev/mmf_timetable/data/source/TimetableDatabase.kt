package com.rodev.mmf_timetable.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rodev.mmf_timetable.data.repository.TimetableTypeConverter
import com.rodev.mmf_timetable.domain.model.Timetable

@Database(entities = [Timetable::class], version = 3, exportSchema = true)
@TypeConverters(TimetableTypeConverter::class)
abstract class TimetableDatabase : RoomDatabase() {

    abstract val timetableDao: TimetableDao

}