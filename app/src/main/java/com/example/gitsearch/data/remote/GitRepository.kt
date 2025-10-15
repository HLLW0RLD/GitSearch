package com.example.gitsearch.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitsearch.data.model.RepoContentModel
import com.example.gitsearch.data.model.RepoModel
import com.example.gitsearch.data.model.SearchModel
import com.example.gitsearch.data.model.toModel
import com.example.gitsearch.data.model.toRepoContentModels
import kotlinx.coroutines.flow.Flow

class GitRepository(private val api: GitApi) {

    fun search(query: String): Flow<PagingData<SearchModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                CombinedPagingSource(api, query)
            }
        ).flow
    }

    suspend fun getRepo(owner: String, repo: String): RepoModel {
        return api.getRepo(owner, repo).toModel()
    }

    suspend fun getRepoContents(owner: String, repo: String, path: String = ""): List<RepoContentModel> {
        return api.getRepoContents(owner, repo, path).toRepoContentModels()
    }
}