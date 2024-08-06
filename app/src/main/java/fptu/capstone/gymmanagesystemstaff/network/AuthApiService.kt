package fptu.capstone.gymmanagesystemstaff.network

import fptu.capstone.gymmanagesystemstaff.model.AuthResponse
import fptu.capstone.gymmanagesystemstaff.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse
}