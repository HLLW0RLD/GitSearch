package com.example.gitsearch.data.model

import com.example.gitsearch.data.remote.GitApi.RepoContent

data class  RepoContentModel(
    val name: String,
    val path: String,
    val type: String,
    val htmlUrl: String?
)

fun RepoContent.toRepoContentModel() = RepoContentModel(
    name = name,
    path = path,
    type = type,
    htmlUrl = htmlUrl
)

fun List<RepoContent>.toRepoContentModels(): List<RepoContentModel> =
    this.map { it.toRepoContentModel() }