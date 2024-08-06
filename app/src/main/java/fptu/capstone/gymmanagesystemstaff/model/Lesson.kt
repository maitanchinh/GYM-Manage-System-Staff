package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class Lesson(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("startTime") var startTime: String? = null,
    @SerializedName("endTime") var endTime: String? = null,
    @SerializedName("classId") var classId: String? = null,
    @SerializedName("feedbackStatus") var feedbackStatus: Boolean? = false,
    var isAttendance: Boolean = false,
    var isPast: Boolean = false
)
