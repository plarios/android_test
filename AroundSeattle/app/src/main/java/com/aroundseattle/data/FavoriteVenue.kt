package com.aroundseattle.data

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["venueId"])
data class FavoriteVenue(
        @SerializedName("venueId")
        val venueId: String,
        @SerializedName("favorite")
        val favorite: Boolean
)