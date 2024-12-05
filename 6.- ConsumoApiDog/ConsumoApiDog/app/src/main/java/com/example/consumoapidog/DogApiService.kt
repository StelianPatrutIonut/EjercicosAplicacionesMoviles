package com.example.consumoapidog

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("api/breed/{breed}/images/random")

    fun getRandomImage(@Path("breed") breed: String): Call<DogApiResponse>
}

