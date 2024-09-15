package com.rodev.mmf_timetable.core.data.di

import com.rodev.mmf_timetable.core.data.OfflineFirstTimetableRepository
import com.rodev.mmf_timetable.core.data.OfflineFirstUserDataRepository
import com.rodev.mmf_timetable.core.data.TimetableRepository
import com.rodev.mmf_timetable.core.data.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsTimetableRepository(
        timetableRepository: OfflineFirstTimetableRepository
    ): TimetableRepository

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository
    ): UserDataRepository

}