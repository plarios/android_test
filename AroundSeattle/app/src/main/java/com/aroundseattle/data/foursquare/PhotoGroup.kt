package com.aroundseattle.data.foursquare

import com.google.gson.annotations.SerializedName

data class PhotoGroup(
        @SerializedName("type")
        val type: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("count")
        val count: Int,
        @SerializedName("items")
        val items: List<Photo>
) {
    fun getFirstPublicPhoto() : Photo? {
        for (photo in items) {
            if (photo.isPublic())
                return  photo
        }
        return null
    }
}