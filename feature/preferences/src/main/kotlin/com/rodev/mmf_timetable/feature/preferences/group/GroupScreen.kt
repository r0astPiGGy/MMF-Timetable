package com.rodev.mmf_timetable.feature.preferences.group

import android.R.attr.onClick
import android.util.Log.i
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.rodev.mmf_timetable.core.model.data.Course
import com.rodev.mmf_timetable.core.ui.DynamicScaffoldPortal
import com.rodev.mmf_timetable.core.ui.HorizontalPagerAdapter
import com.rodev.mmf_timetable.core.ui.PagerValuesState
import com.rodev.mmf_timetable.core.ui.TabRow
import com.rodev.mmf_timetable.feature.preferences.R
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun GroupScreenRoute(
    onNavBack: () -> Unit,
    onGotoSubgroupScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<GroupViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    GroupScreen(
        state = state,
        onGotoSubgroupScreen = onGotoSubgroupScreen,
        onCourseSelected = viewModel::onCourseChanged
    )
}

@Composable
private fun GroupScreen(
    state: GroupUiState,
    onGotoSubgroupScreen: (String) -> Unit,
    onCourseSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    DynamicScaffoldPortal(title = stringResource(R.string.select_group))

    when (state) {
        is GroupUiState.Error -> ErrorState(modifier = modifier, error = state.exception.stackTraceToString())
        is GroupUiState.Courses -> Courses(
            modifier = modifier,
            onGotoSubgroupScreen = onGotoSubgroupScreen,
            onCourseSelected = onCourseSelected,
            state = state
        )
        GroupUiState.Loading -> LoadingState(modifier = modifier)
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

@Composable
private fun Courses(
    state: GroupUiState.Courses,
    onGotoSubgroupScreen: (String) -> Unit,
    onCourseSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val index = state.courses.indexOfFirst { it.course == state.selectedCourse }
    val pagerState = rememberPagerState { state.courses.size }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onCourseSelected(state.courses.get(pagerState.currentPage).course)
    }

    LaunchedEffect(index) {
        if (index >= 0 && index != pagerState.currentPage) {
            pagerState.scrollToPage(index)
        }
    }

    val pagerValues = remember { PagerValuesState(state.courses) }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            currentPage = pagerState.currentPage,
            onPageClick = { i ->
                onCourseSelected(state.courses[i].course)
            },
            list = state.courses,
            textProvider = { it.stringResource() }
        )
        HorizontalDivider()

        HorizontalPagerAdapter(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            valuesState = pagerValues,
        ) { course ->
            GroupList(
                modifier = Modifier.fillMaxSize(),
                course = course
            ) { groupId ->
                onGotoSubgroupScreen(groupId)
            }
        }
    }
}

@Composable
private fun GroupList(
    course: Course,
    modifier: Modifier = Modifier,
    onClick: (groupId: String) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(course.groups, { i, it -> it.id }) { i, group ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onClick(group.id) })
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(group.name)
                if (group.description != null) {
                    Text(
                        text = group.description.toString(),
                        style = MaterialTheme.typography.bodySmall.merge(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
            if (i != course.groups.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun Course.stringResource(): String {
    return stringResource(R.string.course, course)
}