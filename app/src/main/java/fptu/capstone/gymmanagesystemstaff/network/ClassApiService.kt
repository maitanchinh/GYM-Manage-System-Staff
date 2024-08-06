package fptu.capstone.gymmanagesystemstaff.network

import fptu.capstone.gymmanagesystemstaff.model.Classes
import fptu.capstone.gymmanagesystemstaff.model.GClass
import retrofit2.http.GET
import retrofit2.http.Path

interface ClassApiService{
    @GET("classes")
    suspend fun getClasses(): Classes

    @GET("classes/{id}")
    suspend fun getClassById(@Path("id") id: String): GClass
}