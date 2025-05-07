package com.rodev.mmf_timetable.feature.preferences.subgroup

import androidx.compose.runtime.Immutable
import com.rodev.mmf_timetable.core.model.data.Course
import com.rodev.mmf_timetable.core.model.data.SubgroupSubject

sealed interface SubgroupUiState {

    @Immutable
    data class Subjects(
        val subjects: List<SubgroupSubject>,
    ) : SubgroupUiState

    object Loading : SubgroupUiState

    data class Error(val exception: Throwable) : SubgroupUiState

}