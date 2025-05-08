package com.rodev.mmf_timetable.feature.settings

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.Group
import com.rodev.mmf_timetable.core.model.data.Lesson

sealed interface SettingsUiState {

    @Immutable
    data class Settings(val todo: String? = null) : SettingsUiState

    object Loading : SettingsUiState

    data class Error(val exception: Throwable) : SettingsUiState

}