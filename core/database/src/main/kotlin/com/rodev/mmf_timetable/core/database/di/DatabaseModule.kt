package com.rodev.mmf_timetable.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesTimetableDatabase(
        @ApplicationContext appContext: Context
    ): com.rodev.mmf_timetable.core.database.TimetableDatabase = Room.databaseBuilder(
        appContext,
        com.rodev.mmf_timetable.core.database.TimetableDatabase::class.java,
        "timetable-database"
    ).fallbackToDestructiveMigration().build()

}