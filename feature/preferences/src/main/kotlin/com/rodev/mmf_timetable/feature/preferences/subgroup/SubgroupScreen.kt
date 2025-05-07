package com.rodev.mmf_timetable.feature.preferences.subgroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rodev.mmf_timetable.core.model.data.SubgroupSubject
import com.rodev.mmf_timetable.core.ui.DynamicScaffoldPortal
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun SubgroupRoute(
    onGotoHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<SubgroupViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SubgroupScreen(
        modifier = modifier.fillMaxSize(),
        onGotoHome = onGotoHome,
        onDataSubmit = viewModel::updateData,
        state = state
    )
}

@Composable
private fun SubgroupScreen(
    onGotoHome: () -> Unit,
    onDataSubmit: suspend (Set<Long>) -> Unit,
    state: SubgroupUiState,
    modifier: Modifier = Modifier
) {
    DynamicScaffoldPortal(title = "Select subgroup")

    when (state) {
        is SubgroupUiState.Error -> ErrorState(
            modifier = modifier,
            error = state.exception.stackTraceToString()
        )

        is SubgroupUiState.Subjects -> SubjectsState(
            modifier = modifier,
            onDataSubmit = onDataSubmit,
            onGotoHome = onGotoHome,
            state = state
        )

        SubgroupUiState.Loading -> LoadingState(modifier = modifier)
    }
}

@Composable
private fun LoadingState(
    modifier: Modifier = Modifier
) {

}

@Composable
private fun ErrorState(
    modifier: Modifier = Modifier,
    error: String
) {

}

private sealed interface SubmitUiState {
    object Success : SubmitUiState
    object Loading : SubmitUiState
    data class Error(val exception: Exception) : SubmitUiState
    object Idle : SubmitUiState
}

private class GroupSaveState(
    val scope: CoroutineScope,
    val onDataSubmit: suspend (Set<Long>) -> Unit,
    val onGotoHome: () -> Unit
) {
    var submitUiState: SubmitUiState by mutableStateOf(SubmitUiState.Idle)

    fun submitData(data: Set<Long>) {
        scope.launch {
            try {
                submitUiState = SubmitUiState.Loading
                onDataSubmit(data)
                onGotoHome()
            } catch (e: Exception) {
                submitUiState = SubmitUiState.Error(e)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SubjectsState(
    state: SubgroupUiState.Subjects,
    onGotoHome: () -> Unit,
    onDataSubmit: suspend (Set<Long>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val subjectStates = remember {
        state.subjects.map { SubjectGroupState(it) }
    }

    val coroutineScope = rememberCoroutineScope()

    val groupSaveState = remember {
        GroupSaveState(
            scope = coroutineScope,
            onDataSubmit = onDataSubmit,
            onGotoHome = onGotoHome
        )
    }

    Column (
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(subjectStates, { it.subject.id }) {
                SubjectGroupsRow(
                    title = {
                        Text(it.subject.name)
                    },
                    state = it
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = groupSaveState.submitUiState !is SubmitUiState.Loading,
                onClick = {
                    val ids = subjectStates.mapNotNull { it.selectedGroupId }.toSet()
                    groupSaveState.submitData(ids)
                }
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Stable
private class SubjectGroupState(
    val subject: SubgroupSubject,
) {
    var selectedGroupId: Long? by mutableStateOf(null)
        private set

    fun onSelectedGroupIdChange(id: Long?) {
        selectedGroupId = if (id == selectedGroupId) {
            null
        } else {
            id
        }
    }
}

@Composable
private fun SubjectGroupsRow(
    title: @Composable () -> Unit,
    state: SubjectGroupState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        title()
        FlowRow(
            Modifier
                .fillMaxWidth(1f)
                .wrapContentHeight(align = Alignment.Top),
            horizontalArrangement = Arrangement.Start,
        ) {
            state.subject.subgroups.forEach { subgroup ->
                FilterChip(
                    selected = state.selectedGroupId == subgroup.id,
                    onClick = { state.onSelectedGroupIdChange(subgroup.id) },
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .align(alignment = Alignment.CenterVertically),
                    label = {
                        Text(subgroup.name)
                    }
                )
            }
        }
    }
}