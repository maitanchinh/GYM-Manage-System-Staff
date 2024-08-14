package fptu.capstone.gymmanagesystemstaff.network

import fptu.capstone.gymmanagesystemstaff.model.Checkin
import retrofit2.http.POST
import retrofit2.http.Path

interface CheckinApiService {
    @POST("checkin/{memberId}")
    suspend fun checkin(@Path("memberId")memberId: String) : Checkin

}