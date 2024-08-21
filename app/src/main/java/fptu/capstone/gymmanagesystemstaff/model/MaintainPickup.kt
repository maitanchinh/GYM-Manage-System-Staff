package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class MaintainPickup (
    @SerializedName("id"    ) var id    : String? = null,
    @SerializedName("date"  ) var date  : String? = null,
    @SerializedName("image" ) var image : String? = null,
    var beAbleToUpdate : Boolean = true,
    var beAbleToDelete : Boolean = true
)