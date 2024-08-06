package fptu.capstone.gymmanagesystemstaff.network

import fptu.capstone.gymmanagesystemstaff.model.Attendance
import fptu.capstone.gymmanagesystemstaff.utils.RequiresAuth
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AttendanceApiService {
    @RequiresAuth
    @Multipart
    @POST("trainer-attendances")
    suspend fun createAttendance(@Part("trainerId") trainerId: RequestBody, @Part("slotId") slotId: RequestBody) : Attendance
}

