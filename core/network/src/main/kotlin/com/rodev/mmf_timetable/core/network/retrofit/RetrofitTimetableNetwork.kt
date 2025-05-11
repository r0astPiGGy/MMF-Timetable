package com.rodev.mmf_timetable.core.network.retrofit

import com.rodev.mmf_timetable.core.network.BuildConfig
import com.rodev.mmf_timetable.core.network.TimetableNetworkDataSource
import com.rodev.mmf_timetable.core.network.model.NetworkClassroom
import com.rodev.mmf_timetable.core.network.model.NetworkCourse
import com.rodev.mmf_timetable.core.network.model.NetworkLesson
import com.rodev.mmf_timetable.core.network.model.NetworkSubgroupSubject
import com.rodev.mmf_timetable.core.network.model.NetworkTeacher
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton


private interface RetrofitTimetableNetworkApi {
    @GET("teacher")
    suspend fun getTeachers(): List<NetworkTeacher>

    @GET("course")
    suspend fun getCourses(): List<NetworkCourse>

    @GET("classroom")
    suspend fun getClassrooms(): List<NetworkClassroom>

    @GET("lesson/group/{group}")
    suspend fun getLessons(@Path("group") group: String): List<NetworkLesson>

    @GET("teacher/{teacherId}/lessons")
    suspend fun getTeacherLessons(@Path("teacherId") teacherId: String): List<NetworkLesson>

    @GET("subgroup-subject/group/{group}/all")
    suspend fun getSubgroupSubjects(@Path("group") group: String): List<NetworkSubgroupSubject>

}

@Singleton
internal class RetrofitTimetableNetwork @Inject constructor(
    json: Json,
    okHttpCallFactory: dagger.Lazy<Call.Factory>
) : TimetableNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .callFactory { okHttpCallFactory.get().newCall(it) }
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .build()
        .create(RetrofitTimetableNetworkApi::class.java)

    override suspend fun getTeachers(): List<NetworkTeacher> =
        networkApi.getTeachers()

    override suspend fun getCourses(): List<NetworkCourse> =
        networkApi.getCourses()

    override suspend fun getClassrooms(): List<NetworkClassroom> =
        networkApi.getClassrooms()

    override suspend fun getLessons(group: String): List<NetworkLesson> =
        networkApi.getLessons(group)

    override suspend fun getTeacherLessons(teacherId: Long): List<NetworkLesson> =
        networkApi.getTeacherLessons(teacherId.toString())

    override suspend fun getSubgroupSubjects(group: String): List<NetworkSubgroupSubject> =
        networkApi.getSubgroupSubjects(group)

}