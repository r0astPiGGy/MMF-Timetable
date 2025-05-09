package com.rodev.mmf_timetable.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview
@Composable
private fun TopBarPreview() {
    com.rodev.mmf_timetable.core.designsystem.theme.MMF_TimetableTheme {
        TimetableTopAppBar(title = "Расписание", subTitle = "Неделя 2", onMenuButtonClick = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableTopAppBar(
    title: String,
    subTitle: String? = null,
    onMenuButtonClick: () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {
        IconButton(
            onClick = onMenuButtonClick,
        ) {
            Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
        }
    }
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                if (subTitle != null) {
                    Text(
                        text = subTitle,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        },
        navigationIcon = navigationIcon
    )
}