package com.rodev.mmf_timetable.di

import android.content.Context
import androidx.room.Room
import com.rodev.mmf_timetable.data.repository.TimetableRepositoryImpl
import com.rodev.mmf_timetable.data.service.StudyPlanServiceImpl
import com.rodev.mmf_timetable.data.service.TimetableServiceImpl
import com.rodev.mmf_timetable.data.source.TimetableDao
import com.rodev.mmf_timetable.data.source.TimetableDatabase
import com.rodev.mmf_timetable.domain.repository.TimetableRepository
import com.rodev.mmf_timetable.domain.service.StudyPlanService
import com.rodev.mmf_timetable.domain.service.TimetableService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTimetableService(): TimetableService {
        return TimetableServiceImpl()
    }

    @Provides
    @Singleton
    fun provideTimetableRepository(timetableDao: TimetableDao): TimetableRepository {
        return TimetableRepositoryImpl(timetableDao)
    }

    @Provides
    @Singleton
    fun provideStudyPlanService(): StudyPlanService {
        return StudyPlanServiceImpl()
    }

    @Provides
    @Singleton
    fun provideTimetableDao(database: TimetableDatabase): TimetableDao {
        return database.timetableDao
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): TimetableDatabase {
        return Room.databaseBuilder(
            appContext,
            TimetableDatabase::class.java,
            "Timetable"
        ).fallbackToDestructiveMigration().build()
    }
}