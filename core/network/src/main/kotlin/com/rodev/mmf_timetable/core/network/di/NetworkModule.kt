package com.rodev.mmf_timetable.core.network.di

import android.content.Context
import com.rodev.mmf_timetable.core.network.TimetableNetworkDataSource
import com.rodev.mmf_timetable.core.network.model.NetworkAvailability
import com.rodev.mmf_timetable.core.network.retrofit.AuthInterceptor
import com.rodev.mmf_timetable.core.network.retrofit.CacheInterceptor
import com.rodev.mmf_timetable.core.network.retrofit.RetrofitTimetableNetwork
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesJson(): Json = Json {
        ignoreUnknownKeys = true
//        serializersModule = SerializersModule {
//            polymorphic(NetworkAvailability::class) {
//                subclass(NetworkAvailability.After)
//            }
//        }
    }

    @Provides
    @Singleton
    fun providesOkHttpCallFactory(@ApplicationContext context: Context): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        )
        .addInterceptor(CacheInterceptor(context))
        .addInterceptor(AuthInterceptor())
        .build()

}