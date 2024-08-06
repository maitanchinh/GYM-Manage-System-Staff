package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class FilterRequestBody(
    @SerializedName("search") var search: String? = null,
    @SerializedName("memberId") var memberId: String? = null,
    @SerializedName("trainerId") var trainerId: String? = null,
    @SerializedName("classId") var classId: String? = null,
    @SerializedName("courseId") var courseId: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("to") var to: String? = null,
    @SerializedName("categoryId") var categoryId: String? = null,
    @SerializedName("orderBy") var orderBy: String? = null,
    @SerializedName("isAscending") var isAscending: Boolean? = null,
    @SerializedName("pagination") var pagination: Pagination? = Pagination()
)
