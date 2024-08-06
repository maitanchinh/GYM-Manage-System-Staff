package fptu.capstone.gymmanagesystemstaff.utils

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import fptu.capstone.gymmanagesystemstaff.model.AuthResponse
import fptu.capstone.gymmanagesystemstaff.model.User
import java.io.IOException
import javax.inject.Inject

class SessionManager @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    fun saveAuthResponse(authResponse: AuthResponse) {
        sharedPreferences.edit() {
            putString("TOKEN_KEY", authResponse.accessToken).apply()
            putString("USER_KEY", gson.toJson(authResponse.user)).apply()
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString("TOKEN_KEY", null)
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString("USER_KEY", null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("TOKEN_KEY") && sharedPreferences.contains("USER_KEY")
    }

//    fun isLoggedIn(): Boolean {
//        return getToken() != null
//    }

    fun clearAccessToken() {
        try {
            sharedPreferences.edit().remove("TOKEN_KEY").apply()
            sharedPreferences.edit().remove("USER_KEY").apply()
        } catch (e: IOException) {
            // Handle IOException appropriately
            e.printStackTrace()
        }
    }
}
