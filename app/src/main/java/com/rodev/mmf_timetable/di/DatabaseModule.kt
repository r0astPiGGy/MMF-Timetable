package com.rodev.mmf_timetable.di

import android.content.Context
import androidx.room.Room
import com.rodev.mmf_timetable.data.source.local.TimetableDatabase
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
    ): TimetableDatabase = Room.databaseBuilder(
        appContext,
        TimetableDatabase::class.java,
        "timetable-database"
    ).fallbackToDestructiveMigration().build()

}