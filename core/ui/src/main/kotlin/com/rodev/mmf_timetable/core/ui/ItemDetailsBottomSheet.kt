package com.rodev.mmf_timetable.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
class ItemDetailsBottomSheetState<T>(
    val bottomSheetState: SheetState
) {
    var item: T? by mutableStateOf(null)
        private set

    val isOpen by derivedStateOf { item != null }

    fun open(item: T) {
        this.item = item
    }

    fun close() {
        item = null
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> rememberItemDetailsBottomSheetState(
    bottomSheet: SheetState = rememberModalBottomSheetState()
): ItemDetailsBottomSheetState<T> = remember(bottomSheet) {
    ItemDetailsBottomSheetState<T>(bottomSheet)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ItemDetailsBottomSheet(
    modifier: Modifier = Modifier,
    state: ItemDetailsBottomSheetState<T>,
    content: @Composable ColumnScope.(item: T) -> Unit
) {
    if (state.isOpen) {
        val item = state.item ?: return

        ModalBottomSheet(
            onDismissRequest = state::close,
            sheetState = state.bottomSheetState
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
            ) {
                content(item)
            }
        }
    }
}
