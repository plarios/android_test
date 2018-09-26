package com.aroundseattle.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.aroundseattle.data.FavoriteVenue


@Database(
        entities = [
            FavoriteVenue::class
        ],
        version = 1,
        exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao
}