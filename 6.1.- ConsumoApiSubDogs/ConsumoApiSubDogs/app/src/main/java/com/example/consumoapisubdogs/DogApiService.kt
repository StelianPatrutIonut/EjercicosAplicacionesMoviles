package com.example.consumoapisubdogs

import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("breeds/list/all")
    fun getBreeds(): retrofit2.Call<DogApiResponse>

    @GET("breed/{breed}/images/random")
    fun getBreedRandomImage(
        @Path("breed") breed: String ): retrofit2.Call<DogRazaResponse>

    @GET("breed/{breed}/{subbreed}/images")
    fun getSubBreedRandomImage(
        @Path("breed") breed: String, @Path("subbreed") subBreed: String ): retrofit2.Call<DogSubRazaResponse>




}

