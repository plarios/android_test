package com.aroundseattle.api

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.aroundseattle.data.Configuration
import com.aroundseattle.data.foursquare.ApiResponse
import com.aroundseattle.data.foursquare.Venue
import com.aroundseattle.data.foursquare.VenueResponse
import com.aroundseattle.data.foursquare.VenuesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class FoursquareDataProvider @Inject constructor(val config: Configuration,
                                                 val foursquareService: FoursquareService)
{
    fun suggestCompletion(query: String, limit: Int) : ApiResponse<VenuesResponse>? {
        try {
            val response = foursquareService.suggestCompletion(
                    config.clientId,
                    config.clientSecret,
                    "Seattle,WA",
                    query,
                    limit).execute()
            return response.body()
        } catch (t: Throwable) {
            return  ApiResponse.error(t)
        }
    }

    fun searchVenues(query: String, limit: Int) : LiveData<ApiResponse<VenuesResponse>> {
        val result = MutableLiveData<ApiResponse<VenuesResponse>>()
        foursquareService.searchVenues(
                config.clientId,
                config.clientSecret,
                "Seattle,WA",
                query,
                limit).enqueue(object : Callback<ApiResponse<VenuesResponse>> {
            override fun onResponse(call: Call<ApiResponse<VenuesResponse>>, response: Response<ApiResponse<VenuesResponse>>) {
                result.postValue(response.body())
            }

            override fun onFailure(call: Call<ApiResponse<VenuesResponse>>, t: Throwable) {
                result.postValue(ApiResponse.error(t))
            }
        })
        return result
    }

    fun getVenue(venueId: String) : LiveData<ApiResponse<VenueResponse>> {
        val result = MutableLiveData<ApiResponse<VenueResponse>>()
        foursquareService.getVenue(venueId, config.clientId,  config.clientSecret)
                .enqueue(object : Callback<ApiResponse<VenueResponse>> {
                    override fun onResponse(call: Call<ApiResponse<VenueResponse>>, response: Response<ApiResponse<VenueResponse>>) {
                        result.postValue(response.body())
                    }

                    override fun onFailure(call: Call<ApiResponse<VenueResponse>>, t: Throwable) {
                        result.postValue(ApiResponse.error(t))
                    }
                })
        return result
    }
}