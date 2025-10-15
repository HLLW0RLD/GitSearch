package com.example.gitsearch.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.gitsearch.R
import com.example.gitsearch.data.model.RepoModel
import com.example.gitsearch.data.model.UserModel
import com.example.gitsearch.ui.RepoRow
import com.example.gitsearch.ui.SearchAppBar
import com.example.gitsearch.ui.ShimmerContainer
import com.example.gitsearch.ui.UserRow
import com.example.gitsearch.ui.screen.viewModel.MainViewModel
import com.example.gitsearch.ui.screen.viewModel.SearchState
import com.example.gitsearch.ui.theme.AppColors
import com.example.gitsearch.utils.LocalNavController
import com.example.gitsearch.utils.LogUtils.debugLog
import com.example.gitsearch.utils.openUrl
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object Main : Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val navController = LocalNavController.current

    val searchState by mainViewModel.searchState.collectAsState()
    val searchQuery by mainViewModel.searchQuery.collectAsState()
    val pagingItems = mainViewModel.searchResults.collectAsLazyPagingItems()
    val expandedStates by mainViewModel.expandedStates.collectAsState()

    Scaffold(
        topBar = {
            SearchAppBar(
                title = stringResource(R.string.app_title),
                onSearchClick = { mainViewModel.search() },
                onValueChange = { mainViewModel.onQueryChanged(it) }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(AppColors.background)
            ) {
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
                            text = stringResource(R.string.empty_screen_title),
                            modifier = Modifier.align(Alignment.Center),
                            color = AppColors.textPrimary
                        )
                    }
                    is SearchState.Error -> {
                        val msg = (searchState as SearchState.Error).message
                        Text(
                            text = stringResource(R.string.error_screen_title, msg),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clickable { mainViewModel.search() },
                            color = AppColors.error
                        )
                    }
                    is SearchState.Success -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(pagingItems.itemCount) { index ->
                                val item = pagingItems[index]
                                ShimmerContainer(isLoading = false) {
                                    when (item) {
                                        is UserModel -> {
                                            UserRow(item) {
                                                item.htmlUrl.openUrl(context)
                                            }
                                        }
                                        is RepoModel -> {
                                            val key = item.htmlUrl
                                            val expanded = expandedStates[key] == true
                                            RepoRow(
                                                repo = item,
                                                expanded = expanded,
                                                onExpandChange = { mainViewModel.toggleExpanded(key, it) },
                                                onOpenClick = {
                                                    navController.navigate(
                                                        RepoDetails(item.name, item.ownerLogin).route()
                                                    )
                                                }
                                            )
                                        }
                                        null -> {}
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    )
}

