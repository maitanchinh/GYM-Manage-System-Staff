package fptu.capstone.gymmanagesystemstaff.repositories

import fptu.capstone.gymmanagesystemstaff.network.CheckinApiService
import javax.inject.Inject

class CheckinRepository @Inject constructor(private val checkinApiService: CheckinApiService) {
    suspend fun checkin(memberId: String) = checkinApiService.checkin(memberId)
}