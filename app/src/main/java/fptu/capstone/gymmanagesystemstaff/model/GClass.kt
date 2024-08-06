package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class GClass(

    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("thumbnailUrl") var thumbnailUrl: String? = null,
    @SerializedName("trainer") var trainer: Trainer? = Trainer(),
    @SerializedName("totalLesson") var totalLesson: Int? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("to") var to: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("totalMember") var totalMember: Int? = null,
    @SerializedName("lessonCount") var lessonCount: Int? = null,
    @SerializedName("participant") var participant: Int? = null,
)