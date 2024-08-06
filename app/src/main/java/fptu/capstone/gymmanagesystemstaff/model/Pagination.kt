package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class Pagination(

    @SerializedName("pageNumber") var pageNumber: Int? = null,
    @SerializedName("pageSize") var pageSize: Int? = null,
    @SerializedName("totalRow") var totalRow: Int? = null

)