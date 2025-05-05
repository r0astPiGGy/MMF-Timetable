package com.rodev.mmf_timetable

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rodev.mmf_timetable.core.data.repository.CourseRepository
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import com.rodev.mmf_timetable.core.designsystem.theme.MMF_TimetableTheme
import com.rodev.mmf_timetable.ui.TimetableApp
import com.rodev.mmf_timetable.ui.rememberTimetableAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userRepository: UserDataRepository

    @Inject
    lateinit var courseRepository: CourseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appState = rememberTimetableAppState(
                userDataRepository = userRepository,
                courseRepository = courseRepository
            )

            MMF_TimetableTheme {
                TimetableApp(appState)
            }
        }
    }
}