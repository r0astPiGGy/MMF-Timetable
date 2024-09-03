package com.rodev.mmf_timetable.di

import com.rodev.mmf_timetable.data.repository.OfflineFirstTimetableRepository
import com.rodev.mmf_timetable.data.repository.OfflineFirstUserDataRepository
import com.rodev.mmf_timetable.data.source.network.TimetableNetworkImpl
import com.rodev.mmf_timetable.domain.repository.TimetableRepository
import com.rodev.mmf_timetable.domain.repository.UserDataRepository
import com.rodev.mmf_timetable.domain.service.TimetableNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    @Binds
    internal abstract fun bindsTimetableNetworkDataSource(
        timetableNetwork: TimetableNetworkImpl
    ): TimetableNetworkDataSource
}