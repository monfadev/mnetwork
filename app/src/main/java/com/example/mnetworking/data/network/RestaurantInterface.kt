package com.example.mnetworking.data.network

import com.example.mnetworking.data.response.RestaurantResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RestaurantInterface {
    //    @GET("api/users")
    @GET("detail/{id}")
    fun getRestaurant(@Path("id") id: String): Call<RestaurantResponse>
}