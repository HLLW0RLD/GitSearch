package com.example.gitsearch.data.model

data class UserModel(
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String
) : SearchModel()
