package fptu.capstone.gymmanagesystemstaff.network

import fptu.capstone.gymmanagesystemstaff.model.FilterRequestBody
import fptu.capstone.gymmanagesystemstaff.model.Maintain
import fptu.capstone.gymmanagesystemstaff.model.MaintainPickup
import fptu.capstone.gymmanagesystemstaff.model.MaintainResult
import fptu.capstone.gymmanagesystemstaff.model.Response
import fptu.capstone.gymmanagesystemstaff.utils.RequiresAuth
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface MaintenanceApiService {
    @POST("maintains/filter")
    suspend fun getMaintains(@Body filterRequestBody: FilterRequestBody): Response<Maintain>

    @POST("maintain-pickups/filter")
    suspend fun getMaintainPickups(@Body filterRequestBody: FilterRequestBody): Response<MaintainPickup>

    @POST("maintain-results/filter")
    suspend fun getMaintainResults(@Body filterRequestBody: FilterRequestBody): Response<MaintainResult>

    @GET("maintain-results/{id}")
    suspend fun getMaintainResultById(@Path("id") id: String): MaintainResult

    @Multipart
    @POST("maintains")
    suspend fun addMaintain(
        @Part("staffId") staffId: RequestBody,
        @Part("equipmentId") equipmentId: RequestBody,
        @Part("description") description: RequestBody,
        @Part("date") date: RequestBody,
    ): Maintain

    @RequiresAuth
    @Multipart
    @POST("maintain-pickups")
    suspend fun addMaintainPickup(
        @Part("maintainId") maintainId: RequestBody,
        @Part image: MultipartBody.Part?,
    ): MaintainPickup

    @RequiresAuth
    @Multipart
    @POST("maintain-results")
    suspend fun addMaintainResult(
        @Part("maintainId") maintainId: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("date") date: RequestBody,
        @Part("cost") cost: Double,
        @Part invoiceImage: MultipartBody.Part?,
    ): MaintainResult

    @RequiresAuth
    @DELETE("maintain-pickups/{id}")
    suspend fun deleteMaintainPickup(@Path("id") id: String) : MaintainPickup

    @RequiresAuth
    @DELETE("maintain-results/{id}")
    suspend fun deleteMaintainResult(@Path("id") id: String) : MaintainResult
}