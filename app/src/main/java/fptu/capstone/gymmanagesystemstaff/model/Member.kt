package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("id"          ) var id          : String? = null,
    @SerializedName("email"       ) var email       : String? = null,
    @SerializedName("name"        ) var name        : String? = null,
    @SerializedName("status"      ) var status      : String? = null,
    @SerializedName("phone"       ) var phone       : String? = null,
    @SerializedName("gender"      ) var gender      : String? = null,
    @SerializedName("rank"        ) var rank        : String? = null,
    @SerializedName("dateOfBirth" ) var dateOfBirth : String? = null,
    @SerializedName("avatarUrl"   ) var avatarUrl   : String? = null,
    @SerializedName("createAt"    ) var createAt    : String? = null,
    @SerializedName("validDate"   ) var validDate   : String? = null
)
