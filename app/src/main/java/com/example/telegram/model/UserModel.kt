package com.example.telegram.model

data class UserModel(
    val id: String = "",
    var usereame: String = "",
    var bio: String = "",
    var fullname: String = "",
    var state: String = "",
    var phone: String = "",
    var photoUrl: String = "empty"
)
