package com.rodev.mmf_timetable.di

import com.rodev.mmf_timetable.data.repository.OfflineFirstTimetableRepository
import com.rodev.mmf_timetable.data.repository.OfflineFirstUserDataRepository
import com.rodev.mmf_timetable.domain.repository.TimetableRepository
import com.rodev.mmf_timetable.domain.repository.UserDataRepository
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