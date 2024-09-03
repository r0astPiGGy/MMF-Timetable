package com.rodev.mmf_timetable.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rodev.mmf_timetable.data.source.local.model.TimetableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {

    @Query("SELECT * FROM TimetableEntity WHERE id = :id")
    fun get(id: String): Flow<TimetableEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timetable: TimetableEntity)

    @Delete
    suspend fun delete(timetable: TimetableEntity)


}