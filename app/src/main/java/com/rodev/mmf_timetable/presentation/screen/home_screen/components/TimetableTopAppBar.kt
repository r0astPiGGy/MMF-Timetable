package com.rodev.mmf_timetable.presentation.screen.home_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodev.mmf_timetable.presentation.theme.MMF_TimetableTheme

@Preview
@Composable
private fun TopBarPreview() {
    MMF_TimetableTheme {
        Surface {
            TimetableTopAppBar(title = "Расписание") {}
        }
    }
}

@Composable
fun TimetableTopAppBar(
    title: String,
    onMenuButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onMenuButtonClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = { /*TODO*/ },
            enabled = false
        ) {}
    }
}