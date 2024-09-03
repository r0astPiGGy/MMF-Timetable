package com.rodev.mmf_timetable.di

import com.rodev.mmf_timetable.data.source.local.TimetableDao
import com.rodev.mmf_timetable.data.source.local.TimetableDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun providesTimetableDao(
        database: TimetableDatabase
    ): TimetableDao = database.timetableDao

}