package com.rodev.mmf_timetable.core.network.retrofit

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor(
    private val applicationContext: Context,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        return if (isNetworkAvailable(applicationContext)) {
            val maxAge = 60 // 1 minute
            originalResponse.newBuilder()
                .addHeader("Cache-control", "public, max-age = $maxAge")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * 28 // 4 weeks
            originalResponse.newBuilder()
                .addHeader("Cache-control", "public, only-if-cache max-age = $maxStale")
                .build()
        }
    }
    @Suppress("DEPRECATION")
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}
