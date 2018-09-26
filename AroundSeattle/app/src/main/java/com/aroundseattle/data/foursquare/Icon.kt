package com.aroundseattle.data.foursquare

import com.google.gson.annotations.SerializedName

data class Icon(
        @SerializedName("prefix")
        var prefix: String,
        @SerializedName("suffix")
        var suffix: String
)
{
    fun getUrl(size: Int=44) = prefix + size + suffix;
}