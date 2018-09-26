package com.aroundseattle.data.foursquare

import com.google.gson.annotations.SerializedName

data class PhotosList(
        @SerializedName("count")
        val count: Int,
        @SerializedName("groups")
        val groups: List<PhotoGroup>
)