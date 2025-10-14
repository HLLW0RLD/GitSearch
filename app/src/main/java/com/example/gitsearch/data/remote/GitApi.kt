package com.example.gitsearch.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
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



    data class User(
        val login: String,
        @SerializedName("avatar_url") val avatarUrl: String,
        @SerializedName("html_url") val htmlUrl: String
    )

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

    data class SearchUsersResponse(val items: List<User>)

    data class SearchReposResponse(val items: List<Repo>)
}