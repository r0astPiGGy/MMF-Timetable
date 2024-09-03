package com.rodev.mmf_timetable.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rodev.mmf_timetable.data.source.local.model.TimetableEntity

@Database(
    entities = [TimetableEntity::class],
    version = 4,
    exportSchema = true
)
@TypeConverters(
    TimetableTypeConverter::class
)
abstract class TimetableDatabase : RoomDatabase() {
    abstract val timetableDao: TimetableDao
}