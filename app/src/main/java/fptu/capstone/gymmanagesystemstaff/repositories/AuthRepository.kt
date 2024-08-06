package fptu.capstone.gymmanagesystemstaff.repositories

import fptu.capstone.gymmanagesystemstaff.model.AuthResponse
import fptu.capstone.gymmanagesystemstaff.model.LoginRequest
import fptu.capstone.gymmanagesystemstaff.network.AuthApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApiService: AuthApiService) {
    suspend fun login(email: String, password: String): AuthResponse {
        val loginRequest = LoginRequest(email, password)
        return authApiService.login(loginRequest)
    }
}