package com.example.appdefinitiva10

data class RandomUser(
    val results: List<UserResult>
)

data class UserResult(
    val picture: UserPicture
)

data class UserPicture(
    val medium: String
)

