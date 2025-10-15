package com.example.gitsearch.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitsearch.data.model.SearchModel
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
}