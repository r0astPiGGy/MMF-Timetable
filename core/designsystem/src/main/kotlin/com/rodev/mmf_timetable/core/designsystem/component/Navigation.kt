package com.rodev.mmf_timetable.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerNavItem(
    modifier: Modifier = Modifier,
    text: String,
    icon: Int,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        modifier = modifier,
        colors = NavigationDrawerItemDefaults.colors(
//            unselectedTextColor = AppColors.Neutral,
//            unselectedIconColor = AppColors.Neutral
        ),
        label = {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp
            )
        },
        selected = selected,
        icon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        onClick = onClick
    )
}