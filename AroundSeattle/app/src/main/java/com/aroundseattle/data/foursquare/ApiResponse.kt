package com.aroundseattle.data.foursquare

import com.google.gson.annotations.SerializedName


data class ApiResponse<T>(
        @SerializedName("response")
        val response: T?=null,
        var error: Throwable?=null)
{
    companion object {
        fun <T> success(data: T?) : ApiResponse<T> {
            return ApiResponse(data)
        }

        fun <T> error(err: Throwable) : ApiResponse<T> {
            return ApiResponse(error = err)
        }
    }
}