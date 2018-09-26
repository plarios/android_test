package com.aroundseattle.data.foursquare

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("pluralName")
    val pluralName: String,
    @SerializedName("icon")
    val icon: Icon,
    @SerializedName("primary")
    val primary: Boolean
)