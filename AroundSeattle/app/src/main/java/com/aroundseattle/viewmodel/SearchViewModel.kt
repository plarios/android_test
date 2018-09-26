package com.aroundseattle.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.aroundseattle.api.FoursquareDataProvider
import com.aroundseattle.data.foursquare.ApiResponse
import com.aroundseattle.data.foursquare.Venue
import com.aroundseattle.data.foursquare.VenuesResponse
import com.aroundseattle.db.FavoritesDao
import javax.inject.Inject


class SearchViewModel @Inject constructor(
        dataProvider: FoursquareDataProvider
) : ViewModel() {
    private val query = MutableLiveData<String>()
    private val limit = MutableLiveData<Int>()

    init {
        limit.value = 20
    }

    val results: LiveData<List<Venue>> = Transformations
            .switchMap(query){ search ->
                if (search.isNullOrBlank()) {
                    return@switchMap object: LiveData<List<Venue>>(){
                        init {
                            postValue(null)
                        }
                    }
                } else {
                    return@switchMap Transformations.switchMap(dataProvider.searchVenues(search, limit.value!!)){
                        response ->  list(response)
                    }
                }
            }

    fun setQuery(originalInput: String) {
        val input = originalInput.trim()
        if (input == query.value) {
            return
        }
        query.value = input
    }

    private fun list(data: ApiResponse<VenuesResponse>) : LiveData<List<Venue>> {
        val result = MutableLiveData<List<Venue>>()
        if  (data.response?.venues != null) {
            val list = ArrayList<Venue>()
            for (venue in data.response.venues) {
                list.add(venue)
            }
            result.postValue(list)
        }
        return result
    }
}
