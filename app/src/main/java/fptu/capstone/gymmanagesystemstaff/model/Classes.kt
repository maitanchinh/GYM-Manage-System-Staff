package fptu.capstone.gymmanagesystemstaff.model

import com.google.gson.annotations.SerializedName

data class Classes(

    @SerializedName("pagination") var pagination: Pagination? = Pagination(),
    @SerializedName("data") var classes: ArrayList<GClass> = arrayListOf()

)