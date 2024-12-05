package com.example.appdefinitiva10

import retrofit2.http.GET

interface RandomUserGeneratorApi {
    @GET("api/")
    suspend fun getRandomUser(): RandomUser

}