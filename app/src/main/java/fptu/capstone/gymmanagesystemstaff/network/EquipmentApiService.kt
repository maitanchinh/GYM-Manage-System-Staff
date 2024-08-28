package fptu.capstone.gymmanagesystemstaff.network

import fptu.capstone.gymmanagesystemstaff.model.Equipment
import fptu.capstone.gymmanagesystemstaff.model.FilterRequestBody
import fptu.capstone.gymmanagesystemstaff.model.Response
import fptu.capstone.gymmanagesystemstaff.model.SlotEquipment
import fptu.capstone.gymmanagesystemstaff.utils.RequiresAuth
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EquipmentApiService {
    @POST("admin/equipments/filter")
    suspend fun getEquipments(@Body filterRequestBody: FilterRequestBody) : Response<Equipment>

    @POST("slot-equipments/filter")
    suspend fun getSlotEquipments(@Body filterRequestBody: FilterRequestBody) : Response<SlotEquipment>

    @DELETE("slot-equipments/{id}")
    suspend fun deleteSlotEquipment(@Path("id") id: String) : SlotEquipment

    @GET("slot-equipments/{id}")
    suspend fun getSlotEquipmentById(@Path("id") id: String) : SlotEquipment

    @RequiresAuth
    @PUT("slot-equipments/accept-borrow")
    suspend fun acceptBorrow(@Query("id")id: String) : SlotEquipment

    @RequiresAuth
    @PUT("slot-equipments/accept-repay")
    suspend fun acceptRepay(@Query("id")id: String) : SlotEquipment

    @RequiresAuth
    @PUT("slot-equipments/reject-borrow")
    suspend fun rejectBorrow(@Query("id")id: String, @Body body: RejectBody) : SlotEquipment

    @RequiresAuth
    @PUT("slot-equipments/reject-repay")
    suspend fun rejectRepay(@Query("id")id: String, @Body body: RejectBody) : SlotEquipment
}

data class RejectBody(
    val reason: String
)
