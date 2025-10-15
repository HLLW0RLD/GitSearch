package com.example.gitsearch.ui.screen.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitsearch.data.model.RepoModel
import com.example.gitsearch.data.remote.GitApi
import com.example.gitsearch.data.remote.GitRepository
import com.example.gitsearch.ui.screen.RepoContentItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepoDetailsViewModel(
    private val repository: GitRepository
) : ViewModel() {

    private val _repoState = MutableStateFlow<RepoModel?>(null)
    val repoState = _repoState

    private val _repoContentState = MutableStateFlow<List<RepoContentItem>>(emptyList())
    val repoContentState = _repoContentState
    private val pathStack = mutableListOf<String>()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack = _canGoBack

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun onBackPressed(ownerLogin: String, repoName: String): Boolean {
        if (pathStack.size > 1) {
            pathStack.removeLast()
            _canGoBack.value = pathStack.size > 1
            loadRepoContents(pathStack.last(), ownerLogin, repoName)
            return true
        }
        return false
    }

    fun loadRepoContents(path: String, ownerLogin: String, repoName: String) {
        if (pathStack.lastOrNull() != path) pathStack.add(path)
        _canGoBack.value = pathStack.size > 1

        viewModelScope.launch {
            try {
                val items = repository.getRepoContents(ownerLogin, repoName, path)
                _repoContentState.value = items.map { apiItem ->
                    if (apiItem.type == "dir") RepoContentItem.Dir(apiItem.name, apiItem.path)
                    else RepoContentItem.File(apiItem.name, apiItem.path)
                }
            } catch (e: Exception) {
                _repoContentState.value = emptyList()
            }
        }
    }

    fun loadRepo(owner: String, repoName: String) {
        viewModelScope.launch {
            try {
                val repoDetails = repository.getRepo(owner, repoName)
                _repoState.value = repoDetails
            } catch (e: Exception) {

            }
        }
    }
}