package com.rodev.mmf_timetable.domain.use_case

import android.content.SharedPreferences
import androidx.core.content.edit
import com.rodev.mmf_timetable.domain.model.Timetable
import com.rodev.mmf_timetable.domain.repository.TimetableRepository

const val LAST_FETCHED_TIMETABLE = "last_fetched_timetable"

class SetLastFetchedTimetableUseCase(
    private val preferences: SharedPreferences
) {

    operator fun invoke(id: String) {
        preferences.edit {
            putString(LAST_FETCHED_TIMETABLE, id)
        }
    }

}

class GetLastFetchedTimetableUseCase(
    private val preferences: SharedPreferences,
    private val repository: TimetableRepository
) {

    suspend operator fun invoke(): Timetable? {
        val id = preferences
            .getString(LAST_FETCHED_TIMETABLE, null)
            ?: return null

        return repository.get(id)
    }

}