package com.example.gitsearch.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.gitsearch.data.model.RepoModel
import com.example.gitsearch.data.model.UserModel
import com.example.gitsearch.ui.RepoRow
import com.example.gitsearch.ui.SearchAppBar
import com.example.gitsearch.ui.ShimmerContainer
import com.example.gitsearch.ui.UserRow
import com.example.gitsearch.ui.screen.viewModel.MainViewModel
import com.example.gitsearch.ui.screen.viewModel.SearchState
import com.example.gitsearch.utils.LogUtils.debugLog
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object Main : Screen

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = koinViewModel()
) {

    val searchState by mainViewModel.searchState.collectAsState()
    val searchQuery by mainViewModel.searchQuery.collectAsState()
    val pagingItems = mainViewModel.searchResults.collectAsLazyPagingItems()

    var searchActive by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp)
        ) {
            SearchAppBar(
                title = "Git Search",
                onSearchClick = {
                    mainViewModel.search()
                },
                onValueChange = {
                    mainViewModel.onQueryChanged(it)
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            when (searchState) {
                is SearchState.Loading -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(10) {
                            ShimmerContainer(isLoading = true, modifier = Modifier.fillMaxWidth()) {}
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
                is SearchState.Empty -> {
                    Text(
                        "Здесь пока пусто",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }
                is SearchState.Error -> {
                    val msg = (searchState as SearchState.Error).message
                    Text(
                        text = "Повторить загрузку?\nОшибка: $msg",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickable {
                                mainViewModel.search()
                            },
                        color = Color.Red
                    )
                }
                is SearchState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(pagingItems.itemCount) { index ->
                            val item = pagingItems[index]
                            ShimmerContainer(isLoading = false) {
                                when (item) {
                                    is UserModel -> UserRow(item)
                                    is RepoModel -> RepoRow(item)
                                    null -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

