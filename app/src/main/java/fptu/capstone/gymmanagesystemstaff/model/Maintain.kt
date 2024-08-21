package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class Maintain(
    @SerializedName("id"             ) var id             : String?         = null,
    @SerializedName("date"           ) var date           : String?         = null,
    @SerializedName("description"    ) var description    : String?         = null,
    @SerializedName("status"         ) var status         : String?         = null,
    @SerializedName("equipment"      ) var equipment      : Equipment?      = Equipment(),
    @SerializedName("maintainPickup" ) var maintainPickup : MaintainPickup? = MaintainPickup(),
    @SerializedName("staff"          ) var staff          : Staff?          = Staff()
)
