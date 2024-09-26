package com.rodev.mmf_timetable.core.data.di

import com.rodev.mmf_timetable.core.data.repository.CourseRepository
import com.rodev.mmf_timetable.core.data.repository.OfflineFirstLessonRepository
import com.rodev.mmf_timetable.core.data.repository.OfflineFirstUserDataRepository
import com.rodev.mmf_timetable.core.data.repository.LessonRepository
import com.rodev.mmf_timetable.core.data.repository.OfflineFirstCourseRepository
import com.rodev.mmf_timetable.core.data.repository.OfflineFirstSubgroupRepository
import com.rodev.mmf_timetable.core.data.repository.SubgroupRepository
import com.rodev.mmf_timetable.core.data.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsCourseRepository(
        courseRepository: OfflineFirstCourseRepository
    ): CourseRepository

    @Binds
    internal abstract fun bindsLessonRepository(
        timetableRepository: OfflineFirstLessonRepository
    ): LessonRepository

    @Binds
    internal abstract fun bindsSubgroupRepository(
        courseRepository: OfflineFirstSubgroupRepository
    ): SubgroupRepository

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository
    ): UserDataRepository

}