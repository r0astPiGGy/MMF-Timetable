package com.rodev.mmf_timetable.di

import com.rodev.mmf_timetable.data.service.StudyPlanServiceImpl
import com.rodev.mmf_timetable.data.source.network.TimetableNetworkImpl
import com.rodev.mmf_timetable.domain.service.StudyPlanService
import com.rodev.mmf_timetable.domain.service.TimetableNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // TODO REMOVE
    @Provides
    @Singleton
    fun provideStudyPlanService(): StudyPlanService {
        return StudyPlanServiceImpl()
    }
}