package com.rodev.mmf_timetable.core.network.retrofit;

import com.rodev.mmf_timetable.core.network.BuildConfig
import okhttp3.Interceptor;
import okhttp3.Response

class AuthInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request().newBuilder()
        currentRequest.addHeader("Authorization", "Bearer " + BuildConfig.API_KEY)

        val newRequest = currentRequest.build()
        return chain.proceed(newRequest)
    }
}