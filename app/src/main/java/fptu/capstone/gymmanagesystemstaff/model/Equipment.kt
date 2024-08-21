package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class Equipment(
    @SerializedName("id"           ) var id           : String? = null,
    @SerializedName("name"         ) var name         : String? = null,
    @SerializedName("description"  ) var description  : String? = null,
    @SerializedName("thumbnailUrl" ) var thumbnailUrl : String? = null,
    @SerializedName("status"       ) var status       : String? = null
)
