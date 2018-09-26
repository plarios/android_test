package com.aroundseattle.api

import com.aroundseattle.data.foursquare.ApiResponse
import com.aroundseattle.data.foursquare.VenueResponse
import com.aroundseattle.data.foursquare.VenuesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Foursquare API service end points
 */
interface FoursquareService  {
    @GET("venues/search?v=20180920")
    fun searchVenues(@Query("client_id") clientId: String,
                     @Query("client_secret") clientSecret: String,
                     @Query("near") near: String,
                     @Query("query") query: String,
                     @Query("limit") limit: Int)
            : Call<ApiResponse<VenuesResponse>>

    @GET("venues/suggestcompletion?v=20180920")
    fun suggestCompletion(@Query("client_id") clientId: String,
                          @Query("client_secret") clientSecret: String,
                          @Query("near") near: String,
                          @Query("query") query: String,
                          @Query("limit") limit: Int)
            : Call<ApiResponse<VenuesResponse>>

    @GET("venues/{venueId}?v=20180920")
    fun getVenue(@Path("venueId") venueId: String,
                 @Query("client_id") clientId: String,
                 @Query("client_secret") clientSecret: String)
            : Call<ApiResponse<VenueResponse>>
}