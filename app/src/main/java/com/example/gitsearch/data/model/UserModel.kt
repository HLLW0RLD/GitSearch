package com.example.gitsearch.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String
) : SearchModel()
