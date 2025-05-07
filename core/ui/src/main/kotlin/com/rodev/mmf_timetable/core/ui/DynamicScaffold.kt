package com.rodev.mmf_timetable.core.ui

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.util.UUID

@Preview
@Composable
private fun Prev() {

}

private data class ScaffoldState(
    val topAppBar: (@Composable (String?) -> Unit)?,
    val title: String? = null
)

private data class IdentifiableState(
    val id: String,
    val state: ScaffoldState
)

private class DynamicScaffoldState(
    private val default: ScaffoldState
) {

    private val stack = mutableStateListOf<IdentifiableState>()

    val state by derivedStateOf { stack.lastOrNull()?.state ?: default }

    private fun stateOf(state: ScaffoldState): ScaffoldState {
        return state.copy(
            topAppBar = state.topAppBar ?: default.topAppBar,
            title = state.title ?: default.title
        )
    }

    fun push(id: String, state: ScaffoldState) {
        Log.i("@@@" , "Trying to push state with id $id")
        val last = stack.lastOrNull()
        if (last?.id == id) {
            val newState = stateOf(state)
            stack[stack.lastIndex] = IdentifiableState(id, newState)
            return
        }

        val wrongState = stack.firstOrNull { it.id == id }
        require(wrongState == null) { "Found illegal state" }

        stack.add(IdentifiableState(id, stateOf(state)))
    }

    fun pop(id: String?) {
        if (id == null && stack.isNotEmpty()) {
            stack.removeAt(stack.lastIndex)
            return
        }
        Log.i("@@@", "Removing $id")
        stack.removeIf { it.id == id }
    }
}

private val LocalDynamicScaffoldState = compositionLocalOf<DynamicScaffoldState> { throw IllegalStateException() }

@Composable
fun DynamicScaffold(
    modifier: Modifier = Modifier,
    topAppBar: @Composable (String?) -> Unit,
    key: Any? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val state = remember { DynamicScaffoldState(ScaffoldState(topAppBar)) }
    val current = state.state

//    LaunchedEffect(key) {
//        state.pop(null)
//    }

    CompositionLocalProvider(LocalDynamicScaffoldState provides state) {
        Scaffold(modifier = modifier, topBar = { current.topAppBar?.invoke(current.title) }, content = content)
    }
}

@Composable
fun DynamicScaffoldPortal(
    topAppBar: (@Composable (String?) -> Unit)? = null,
    title: String? = null,
) {
    val state = LocalDynamicScaffoldState.current
    val id = remember { UUID.randomUUID().toString() }
    DisposableEffect(topAppBar, title) {
        state.push(id, ScaffoldState(topAppBar = topAppBar, title = title))
        onDispose {
            state.pop(id)
        }
    }
}