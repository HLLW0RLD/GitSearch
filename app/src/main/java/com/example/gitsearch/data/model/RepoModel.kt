package com.example.gitsearch.data.model

data class RepoModel(
    val name: String,
    val stars: Int,
    val watchers: Int,
    val forks: Int,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val htmlUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val description: String?
) : SearchModel()
