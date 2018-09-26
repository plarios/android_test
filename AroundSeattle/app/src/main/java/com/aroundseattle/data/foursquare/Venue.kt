package com.aroundseattle.data.foursquare

import com.google.gson.annotations.SerializedName


data class Venue(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String?,
        @SerializedName("location")
        val location: Location,
        @SerializedName("categories")
        val categories: List<Category>,
        @SerializedName("photos")
        val photos: PhotosList?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("bestPhoto")
        val bestPhoto: Photo?) {

    fun getPhoto() : Photo? {
        if (bestPhoto != null && bestPhoto.isPublic())
            return  bestPhoto
        if (photos != null) {
            for (group in photos.groups) {
                val photo = group.getFirstPublicPhoto()
                if (photo != null)
                    return photo
            }
        }
        return null
    }

    fun getPhotoUrl() : String {
        val photo = getPhoto()
        if (photo != null)
            return photo.getUrl(300, 300)
        return ""
    }

    fun getCategory() : String {
        if (!categories.isEmpty()) {
            return categories[0].name
        }
        return ""
    }

    fun getIconUrl() : String {
        if (!categories.isEmpty()) {
            return categories[0].icon.getUrl()
        }
        return ""
    }
}
