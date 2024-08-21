package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class MaintainResult(
    @SerializedName("id"           ) var id           : String? = null,
    @SerializedName("date"         ) var date         : String? = null,
    @SerializedName("maintainId"   ) var maintainId   : String? = null,
    @SerializedName("image"        ) var image        : String? = null,
    @SerializedName("cost"         ) var cost         : Int?    = null,
    @SerializedName("invoiceImage" ) var invoiceImage : String? = null,
    @SerializedName("status"       ) var status       : String? = null,
    @SerializedName("createAt"     ) var createAt     : String? = null
)
