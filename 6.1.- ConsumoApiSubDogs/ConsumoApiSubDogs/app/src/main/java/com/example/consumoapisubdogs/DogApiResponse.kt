package com.example.consumoapisubdogs

data class DogApiResponse(
    val status: String,
    val message: Map<String, List<String>>
)