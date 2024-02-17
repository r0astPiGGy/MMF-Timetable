package com.rodev.mmf_timetable.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rodev.mmf_timetable.domain.model.Timetable

@Dao
interface TimetableDao {

    @Query("SELECT * FROM Timetable WHERE id = :id")
    suspend fun get(id: String): Timetable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timetable: Timetable)

    @Delete
    suspend fun delete(timetable: Timetable)


}