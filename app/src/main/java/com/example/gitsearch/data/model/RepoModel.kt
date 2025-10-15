package com.example.gitsearch.data.model

import com.example.gitsearch.data.remote.GitApi.Repo
import kotlinx.serialization.Serializable

@Serializable
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
    val description: String? = null
) : SearchModel()

fun Repo.toModel() = RepoModel(
    name = name,
    stars = stargazersCount,
    watchers = watchersCount,
    forks = forksCount,
    ownerLogin = owner.login,
    ownerAvatarUrl = owner.avatarUrl,
    htmlUrl = htmlUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    description = description
)