package com.example.gitsearch.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitsearch.data.model.RepoModel
import com.example.gitsearch.data.model.SearchModel
import com.example.gitsearch.data.model.UserModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CombinedPagingSource(
    private val api: GitApi,
    private val query: String
) : PagingSource<Int, SearchModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchModel> {
        val page = params.key ?: 1
        return try {
            val (usersResp, reposResp) = coroutineScope {
                val users = async { api.searchUsers(query, page) }
                val repos = async { api.searchRepos(query, page) }
                users.await() to repos.await()
            }

            val users = usersResp.items.map {
                UserModel(
                    login = it.login,
                    avatarUrl = it.avatarUrl,
                    htmlUrl = it.htmlUrl
                )
            }

            val repos = reposResp.items.map {
                RepoModel(
                    name = it.name,
                    stars = it.stargazersCount,
                    watchers = it.watchersCount,
                    forks = it.forksCount,
                    ownerLogin = it.owner.login,
                    ownerAvatarUrl = it.owner.avatarUrl,
                    htmlUrl = it.htmlUrl,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                    description = it.description
                )
            }

            val combined = (users + repos).sortedBy {
                when (it) {
                    is UserModel -> it.login.lowercase()
                    is RepoModel -> it.name.lowercase()
                }
            }

            LoadResult.Page(
                data = combined,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (combined.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchModel>): Int? = null
}
