package fptu.capstone.gymmanagesystemstaff.repositories

import fptu.capstone.gymmanagesystemstaff.model.Attendance
import fptu.capstone.gymmanagesystemstaff.network.AttendanceApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject

class AttendanceRepository @Inject constructor(private val attendanceApiService: AttendanceApiService) {

    suspend fun createAttendance(trainerId: String, slotId: String): Attendance {
        val trainerIdPart = trainerId.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
        val slotIdPart = slotId.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
        return attendanceApiService.createAttendance(trainerId = trainerIdPart, slotId = slotIdPart)
    }
}