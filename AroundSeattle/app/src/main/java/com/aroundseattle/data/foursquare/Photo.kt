package com.aroundseattle.data.foursquare

import com.google.gson.annotations.SerializedName

data class Photo(
        @SerializedName("id")
        val id: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("suffix")
        val suffix: String,
        @SerializedName("width")
        val width: Int,
        @SerializedName("height")
        val height: Int,
        @SerializedName("visibility")
        val visibility: String
) {
    fun getUrl(width: Int=0, height: Int=0) : String {
        var w = width
        var h = height
        if (w == 0)
            w = this.width
        if (h == 0)
            h = this.height
        return prefix + w +"x" + h + suffix
    }

    fun isPublic() : Boolean {
        return  "public".equals(visibility)
    }
}