package com.aroundseattle.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.aroundseattle.data.FavoriteVenue
import com.aroundseattle.data.foursquare.Venue

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteVenue: FavoriteVenue)

    @Query("SELECT * FROM favoritevenue WHERE venueId = :id")
    fun findById(id: String): LiveData<FavoriteVenue>

    @Query("SELECT * from  favoritevenue")
    fun getAll(): LiveData<List<FavoriteVenue>>
}