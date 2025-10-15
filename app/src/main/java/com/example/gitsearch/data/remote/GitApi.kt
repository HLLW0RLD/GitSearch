package com.example.gitsearch.data.remote

import com.example.gitsearch.data.model.RepoModel
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitApi {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30
    ): SearchUsersResponse

    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30
    ): SearchReposResponse

    @GET("repos/{owner}/{repo}")
    suspend fun getRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Repo

    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getRepoContents(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String = ""
    ): List<RepoContent>

    @Serializable
    data class User(
        val login: String,
        @SerializedName("avatar_url") val avatarUrl: String,
        @SerializedName("html_url") val htmlUrl: String
    )

    @Serializable
    data class Repo(
        val owner: User,
        val name: String,
        val description: String?,
        @SerializedName("full_name") val fullName: String,
        @SerializedName("watchers_count") val watchersCount: Int,
        @SerializedName("stargazers_count") val stargazersCount: Int,
        @SerializedName("forks_count") val forksCount: Int,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("updated_at") val updatedAt: String,
        @SerializedName("html_url") val htmlUrl: String
    )

    @Serializable
    data class RepoContent(
        val name: String,
        val path: String,
        val type: String,
        @SerializedName("html_url") val htmlUrl: String?
    )

    data class SearchUsersResponse(val items: List<User>)

    data class SearchReposResponse(val items: List<Repo>)
}