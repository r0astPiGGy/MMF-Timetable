package com.rodev.mmf_timetable.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [com.rodev.mmf_timetable.core.database.model.TimetableEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(
    TimetableTypeConverter::class
)
abstract class TimetableDatabase : RoomDatabase() {
    abstract val timetableDao: TimetableDao
}