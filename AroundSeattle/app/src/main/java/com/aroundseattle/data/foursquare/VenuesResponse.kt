package com.aroundseattle.data.foursquare

import com.google.gson.annotations.SerializedName


data class VenuesResponse(
        @SerializedName("minivenues")
        val minivenues: List<Venue>?,
        @SerializedName("venues")
        val venues: List<Venue>?
)
