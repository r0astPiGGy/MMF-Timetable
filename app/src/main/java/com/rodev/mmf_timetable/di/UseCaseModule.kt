package com.rodev.mmf_timetable.di

import android.content.Context
import android.content.SharedPreferences
import com.rodev.mmf_timetable.domain.repository.TimetableRepository
import com.rodev.mmf_timetable.domain.service.StudyPlanService
import com.rodev.mmf_timetable.domain.service.TimetableService
import com.rodev.mmf_timetable.domain.use_case.GetCoursesUseCase
import com.rodev.mmf_timetable.domain.use_case.GetCurrentWeekUseCase
import com.rodev.mmf_timetable.domain.use_case.GetTodayLessonsUseCase
import com.rodev.mmf_timetable.domain.use_case.GetLastFetchedTimetableUseCase
import com.rodev.mmf_timetable.domain.use_case.GetTimetableUseCase
import com.rodev.mmf_timetable.domain.use_case.GetWidgetStateUseCase
import com.rodev.mmf_timetable.domain.use_case.IsLessonAvailableUseCase
import com.rodev.mmf_timetable.domain.use_case.LoadUserInfoUseCase
import com.rodev.mmf_timetable.domain.use_case.SaveUserInfoUseCase
import com.rodev.mmf_timetable.domain.use_case.SetLastFetchedTimetableUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {


    private fun Context.preferences(): SharedPreferences {
        return getSharedPreferences("app", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideSetLastFetchedTimetableUseCase(
        @ApplicationContext context: Context
    ): SetLastFetchedTimetableUseCase {
        return SetLastFetchedTimetableUseCase(context.preferences())
    }

    @Provides
    fun provideGetLastFetchedTimetableUseCase(
        @ApplicationContext context: Context,
        timetableRepository: TimetableRepository
    ): GetLastFetchedTimetableUseCase {
        return GetLastFetchedTimetableUseCase(context.preferences(), timetableRepository)
    }

    @Provides
    fun provideGetCurrentWeekUseCase(
        studyPlanService: StudyPlanService,
        loadUserInfoUseCase: LoadUserInfoUseCase
    ): GetCurrentWeekUseCase {
        return GetCurrentWeekUseCase(studyPlanService, loadUserInfoUseCase)
    }

    @Provides
    fun provideIsLessonAvailableUseCase(
        loadUserInfoUseCase: LoadUserInfoUseCase,
        currentWeekUseCase: GetCurrentWeekUseCase
    ): IsLessonAvailableUseCase {
        return IsLessonAvailableUseCase(loadUserInfoUseCase, currentWeekUseCase)
    }

    @Provides
    fun provideSaveUserInfoUseCase(
        @ApplicationContext context: Context
    ): SaveUserInfoUseCase {
        return SaveUserInfoUseCase(context.preferences())
    }

    @Provides
    fun provideLoadUserInfoUseCase(
        @ApplicationContext context: Context,
        timetableService: TimetableService
    ): LoadUserInfoUseCase {
        return LoadUserInfoUseCase(context.preferences(), timetableService)
    }

    @Provides
    fun provideGetWrappedTimetableUseCase(
        getLastFetchedTimetableUseCase: GetLastFetchedTimetableUseCase,
        getTodayLessonsUseCase: GetTodayLessonsUseCase
    ): GetWidgetStateUseCase {
        return GetWidgetStateUseCase(
            getLastFetchedTimetableUseCase,
            getTodayLessonsUseCase
        )
    }

    @Provides
    @Singleton
    fun provideGetCoursesUseCase(timetableService: TimetableService): GetCoursesUseCase {
        return GetCoursesUseCase(timetableService)
    }

    @Provides
    @Singleton
    fun provideGetCurrentTimetableUseCase(
        isLessonAvailableUseCase: IsLessonAvailableUseCase
    ): GetTodayLessonsUseCase {
        return GetTodayLessonsUseCase(isLessonAvailableUseCase)
    }

    @Provides
    @Singleton
    fun provideGetTimetableUseCase(
        timetableRepository: TimetableRepository,
        service: TimetableService,
        setLastFetchedTimetableUseCase: SetLastFetchedTimetableUseCase
    ): GetTimetableUseCase {
        return GetTimetableUseCase(
            repository = timetableRepository,
            service = service,
            setLastFetchedTimetable = setLastFetchedTimetableUseCase
        )
    }

}