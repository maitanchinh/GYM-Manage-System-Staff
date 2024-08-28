package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class Equipment(
    @SerializedName("id"           ) var id           : String? = null,
    @SerializedName("name"         ) var name         : String? = null,
    @SerializedName("description"  ) var description  : String? = null,
    @SerializedName("thumbnailUrl" ) var thumbnailUrl : String? = null,
    @SerializedName("status"       ) var status       : String? = null
)

data class SlotEquipment(
    @SerializedName("id"        ) var id        : String?    = null,
    @SerializedName("status"    ) var status    : String?    = null,
    @SerializedName("createAt"  ) var createAt  : String?    = null,
    @SerializedName("updateAt"  ) var updateAt  : String?    = null,
    @SerializedName("creator"   ) var creator   : Trainer?   = Trainer(),
    @SerializedName("equipment" ) var equipment : Equipment? = Equipment(),
    @SerializedName("slot"      ) var slot      : Lesson?      = Lesson()
)
