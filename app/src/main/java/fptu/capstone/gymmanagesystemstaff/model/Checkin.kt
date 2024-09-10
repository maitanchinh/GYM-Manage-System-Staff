package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class Checkin(
    @SerializedName("id"     ) var id     : String? = null,
    @SerializedName("date"   ) var date   : String? = null,
    @SerializedName("member" ) var member : Member? = null
)
