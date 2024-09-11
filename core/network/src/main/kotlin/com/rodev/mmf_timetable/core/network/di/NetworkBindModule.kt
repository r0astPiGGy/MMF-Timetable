package com.rodev.mmf_timetable.core.network.di

import com.rodev.mmf_timetable.core.network.TimetableNetworkDataSource
import com.rodev.mmf_timetable.core.network.retrofit.RetrofitTimetableNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkBindModule {

    @Binds
    fun binds(impl: RetrofitTimetableNetwork): TimetableNetworkDataSource
}