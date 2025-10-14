package com.example.gitsearch.ui.screen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitsearch.data.model.SearchModel
import com.example.gitsearch.data.remote.GitRepository
import com.example.gitsearch.utils.LogUtils.debugLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: GitRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Empty)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<PagingData<SearchModel>>(PagingData.empty())
    val searchResults: StateFlow<PagingData<SearchModel>> = _searchResults.asStateFlow()


    fun onQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.length < 3) return
        search(query)
    }

    fun search(query: String = _searchQuery.value) {
        _searchQuery.value = query

        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            debugLog("search() query = $query")
            debugLog("search() _searchQuery = ${_searchQuery.value}")

            repository.search(query)
                .onEach { pagingData ->
                    _searchResults.value = pagingData
                    _searchState.value = SearchState.Success(query)
                }
                .catch { e ->
                    _searchState.value = SearchState.Error(e.message ?: "Ошибка при загрузке данных")
                }
                .launchIn(viewModelScope)
        }
    }
}

sealed class SearchState {
    object Empty : SearchState()
    object Loading : SearchState()
    data class Success(val query: String) : SearchState()
    data class Error(val message: String = "error") : SearchState()
}