package com.aroundseattle.viewmodel

import android.arch.lifecycle.*
import com.aroundseattle.AppExecutors
import com.aroundseattle.api.FoursquareDataProvider
import com.aroundseattle.data.FavoriteVenue
import com.aroundseattle.data.foursquare.ApiResponse
import com.aroundseattle.data.foursquare.Venue
import com.aroundseattle.data.foursquare.VenueResponse
import com.aroundseattle.db.FavoritesDao
import javax.inject.Inject


class VenueViewModel @Inject constructor(
        dataProvider: FoursquareDataProvider,
        val appExecutors: AppExecutors,
        val favoritesDao: FavoritesDao) : ViewModel() {
    private val venueId = MutableLiveData<String>()

    val venue: LiveData<Venue> = Transformations
            .switchMap(venueId){ venue ->
                if (venue.isNullOrBlank()) {
                    object: LiveData<Venue>(){
                        init {
                            postValue(null)
                        }
                    }
                } else {
                    return@switchMap Transformations.switchMap(dataProvider.getVenue(venue)) {
                        map(it)
                    }
                }
            }

    val favoriteStatus = MediatorLiveData<FavoriteVenue>()

    fun setVenueId(id: String) {
        if (id == venueId.value) {
            return
        }
        venueId.value = id
        refreshFavoriteStatus(id)
    }

    fun toggleFavorite(venue: Venue?) {
        if (venue == null)
            return

        appExecutors.diskIO().execute(){
            val  fv: FavoriteVenue
            if (favoriteStatus.value == null) {
                fv = FavoriteVenue(venue.id, true)
            } else {
                val fav = favoriteStatus.value
                val b = fav?.favorite ?: false
                fv = FavoriteVenue(venue.id, !b)
            }
            favoritesDao.insert(fv)
            refreshFavoriteStatus(venue.id)
        }
    }

    fun refreshFavoriteStatus(id: String) {
        favoriteStatus.addSource(favoritesDao.findById(id)) {
            if (it != null) {
                favoriteStatus.postValue(it)
            } else {
                favoriteStatus.postValue(FavoriteVenue(id, false))
            }
        }
    }

    private fun map(value: ApiResponse<VenueResponse>?) : LiveData<Venue> {
        val result = MutableLiveData<Venue>()
        if (value?.response !=  null) {
            result.postValue(value.response.venue)
        }
        return result
    }
}
