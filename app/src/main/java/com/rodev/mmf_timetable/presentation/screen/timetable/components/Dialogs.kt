package com.rodev.mmf_timetable.presentation.screen.timetable.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rodev.mmf_timetable.core.model.data.Group
import com.rodev.mmf_timetable.presentation.screen.UserDataUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

typealias OnCourseSelected = (course: Int, group: com.rodev.mmf_timetable.core.model.data.Group, subGroup: String?) -> Unit

@Composable
fun CourseEditDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    state: UserDataUiState.Success,
    onCourseAndGroupSelected: OnCourseSelected
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        CourseEditDialogContent(
            modifier = modifier,
            onCourseAndGroupSelected = onCourseAndGroupSelected,
            state = state
        )
    }
}

@Composable
private fun CourseEditDialogContent(
    modifier: Modifier = Modifier,
    state: UserDataUiState.Success,
    onCourseAndGroupSelected: OnCourseSelected
) {
    var selectedCourse: Int? by remember { mutableStateOf(null) }
    var selectedGroup: com.rodev.mmf_timetable.core.model.data.Group? by remember { mutableStateOf(null) }
    var selectedSubGroup: String? by remember { mutableStateOf(null) }
    var groups: ImmutableList<com.rodev.mmf_timetable.core.model.data.Group> by remember { mutableStateOf(persistentListOf()) }

    Card(
        modifier = modifier
            .fillMaxSize(fraction = 0.8f)
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
            ) {
                DialogDropDowns(
                    selectedCourse = selectedCourse,
                    onSetSelectedGroup = { selectedGroup = it },
                    onSetSelectedCourse = {
                        groups = state.groupsByCourse[it].orEmpty().toPersistentList()
                        selectedCourse = it
                    },
                    groups = groups,
                    selectedGroup = selectedGroup,
                    selectedSubGroup = selectedSubGroup,
                    courseList = state.courses,
                    onSetSelectedSubGroup = { selectedSubGroup = it }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                enabled = selectedCourse != null && selectedGroup != null,
                onClick = {
                    onCourseAndGroupSelected(selectedCourse!!, selectedGroup!!, selectedSubGroup)
                }
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Composable
private fun DialogDropDowns(
    selectedCourse: Int?,
    onSetSelectedCourse: (Int) -> Unit,
    groups: ImmutableList<com.rodev.mmf_timetable.core.model.data.Group>,
    courseList: ImmutableList<Int>,
    selectedGroup: com.rodev.mmf_timetable.core.model.data.Group?,
    onSetSelectedGroup: (com.rodev.mmf_timetable.core.model.data.Group) -> Unit,
    selectedSubGroup: String?,
    onSetSelectedSubGroup: (String) -> Unit
) {
    Text("Редактирование")

    val courseDropDown = remember { DropDownMenuState() }
    CustomDropdownMenu(
        state = courseDropDown,
        modifier = Modifier.fillMaxWidth(),
        itemLabel = selectedCourse?.let { "Курс $it" } ?: "Курс"
    ) {
        courseList.forEach {
            DropdownMenuItem(
                text = { Text("Курс $it") },
                onClick = {
                    courseDropDown.expanded = false
                    onSetSelectedCourse(it)
                }
            )
        }
    }

    if (selectedCourse != null) {
        val groupDropdown = remember { DropDownMenuState() }
        CustomDropdownMenu(
            state = groupDropdown,
            modifier = Modifier.fillMaxWidth(),
            itemLabel = selectedGroup?.name ?: "Группа"
        ) {
            groups.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        groupDropdown.expanded = false
                        onSetSelectedGroup(it)
                    }
                )
            }
        }
    }

    // TODO
//    val groupDropdown = remember { DropDownMenuState() }
//    CustomDropdownMenu(
//        state = groupDropdown,
//        modifier = Modifier.fillMaxWidth(),
//        itemLabel = selectedSubGroup ?: "Подгруппа"
//    ) {
//        Lesson.subGroups.forEach {
//            DropdownMenuItem(
//                text = { Text(it) },
//                onClick = {
//                    groupDropdown.expanded = false
//                    onSetSelectedSubGroup(it)
//                }
//            )
//        }
//    }

}

private class DropDownMenuState {
    var expanded by mutableStateOf(false)
}

@Composable
private fun CustomDropdownMenu(
    modifier: Modifier = Modifier,
    itemLabel: String,
    state: DropDownMenuState = remember { DropDownMenuState() },
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Box {
            val width = 200.dp

            OutlinedButton(
                onClick = { state.expanded = !state.expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = itemLabel
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }

            DropdownMenu(
                expanded = state.expanded,
                onDismissRequest = { state.expanded = false },
                modifier = Modifier.width(width)
            ) {
                content()
            }
        }
    }
}