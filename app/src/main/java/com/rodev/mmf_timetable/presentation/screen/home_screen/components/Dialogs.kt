package com.rodev.mmf_timetable.presentation.screen.home_screen.components

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
import com.rodev.mmf_timetable.domain.model.Lesson
import com.rodev.mmf_timetable.domain.service.Course
import com.rodev.mmf_timetable.presentation.screen.home_screen.state.HomeScreenState

@Composable
fun CourseEditDialog(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    presented: Boolean,
    onDismiss: () -> Unit,
    onCourseAndGroupSelected: (course: Course, group: Course.Group, subGroup: String?) -> Unit
) {
    if (presented) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            CourseEditDialogContent(
                modifier = modifier,
                courseList = state.courseList,
                onCourseAndGroupSelected = onCourseAndGroupSelected
            )
        }
    }
}

@Composable
private fun CourseEditDialogContent(
    modifier: Modifier = Modifier,
    courseList: List<Course>,
    onCourseAndGroupSelected: (course: Course, group: Course.Group, subGroup: String?) -> Unit
) {
    var selectedCourse: Course? by remember { mutableStateOf(null) }
    var selectedGroup: Course.Group? by remember { mutableStateOf(null) }
    var selectedSubGroup: String? by remember { mutableStateOf(null) }

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
                    onSetSelectedCourse = { selectedCourse = it },
                    selectedGroup = selectedGroup,
                    selectedSubGroup = selectedSubGroup,
                    courseList = courseList,
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
    selectedCourse: Course?,
    onSetSelectedCourse: (Course) -> Unit,
    selectedGroup: Course.Group?,
    onSetSelectedGroup: (Course.Group) -> Unit,
    selectedSubGroup: String?,
    onSetSelectedSubGroup: (String) -> Unit,
    courseList: List<Course>
) {
    Text("Редактирование")

    val courseDropDown = remember { DropDownMenuState() }
    CustomDropdownMenu(
        state = courseDropDown,
        modifier = Modifier.fillMaxWidth(),
        itemLabel = selectedCourse?.course?.let { "Курс $it" } ?: "Курс"
    ) {
        courseList.forEach {
            DropdownMenuItem(
                text = { Text("Курс ${it.course}") },
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
            selectedCourse.groupList.forEach {
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

    val groupDropdown = remember { DropDownMenuState() }
    CustomDropdownMenu(
        state = groupDropdown,
        modifier = Modifier.fillMaxWidth(),
        itemLabel = selectedSubGroup ?: "Подгруппа"
    ) {
        Lesson.subGroups.forEach {
            DropdownMenuItem(
                text = { Text(it) },
                onClick = {
                    groupDropdown.expanded = false
                    onSetSelectedSubGroup(it)
                }
            )
        }
    }

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