package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class Attendance(
    @SerializedName("id") var id: String? = null,
    @SerializedName("createAt") var createAt: String? = null,
    @SerializedName("member") var member: Member? = Member(),
    @SerializedName("trainer") var trainer: Trainer? = Trainer(),
    @SerializedName("slot") var slot: Lesson? = Lesson()
)
