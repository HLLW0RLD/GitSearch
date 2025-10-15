package com.example.gitsearch.ui.screen

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gitsearch.ui.screen.viewModel.RepoDetailsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.gitsearch.ui.SearchAppBar
import com.example.gitsearch.ui.theme.AppColors
import com.example.gitsearch.utils.LocalNavController
import com.example.gitsearch.utils.toReadableDate
import androidx.core.net.toUri
import com.example.gitsearch.R
import com.example.gitsearch.ui.StatItem

@Serializable
data class RepoDetails(
    val repoName: String = "",
    val ownerLogin: String = ""
) : Screen

sealed class RepoContentItem {
    data class File(val name: String, val path: String) : RepoContentItem()
    data class Dir(val name: String, val path: String) : RepoContentItem()
}
const val BASE_URL_PERT = "/blob/master/"

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun RepoDetailsScreen(
    repoName: String,
    ownerLogin: String,
    repoDetailsViewModel: RepoDetailsViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val navController = LocalNavController.current

    val repoState by repoDetailsViewModel.repoState.collectAsState()
    val contentState by repoDetailsViewModel.repoContentState.collectAsState()
    val canGoBack by repoDetailsViewModel.canGoBack.collectAsState()

    LaunchedEffect(Unit) {
        repoDetailsViewModel.loadRepo(ownerLogin, repoName)
        repoDetailsViewModel.loadRepoContents("", ownerLogin, repoName)
    }

    Scaffold(
        topBar = {
            SearchAppBar(
                title = repoState?.name ?: "Загрузка...",
                needSearch = false,
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    ) { padding ->
        repoState?.let { r ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .background(AppColors.background),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AsyncImage(
                        model = r.ownerAvatarUrl,
                        contentDescription = "",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                    Column {
                        Text(text = r.ownerLogin, fontWeight = FontWeight.Bold)
                        Text(
                            text = r.name,
                            color = AppColors.textPrimary
                        )
                    }
                }

                r.description?.let {
                    Text(text = it)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatItem(
                        icon = Icons.Default.Star,
                        count = r.stars,
                        text = stringResource(R.string.stars)
                    )
                    StatItem(
                        icon = Icons.Default.Menu,
                        count = r.forks,
                        text = stringResource(R.string.forks)
                    )
                    StatItem(
                        icon = Icons.Default.AccountCircle,
                        count = r.watchers,
                        text = stringResource(R.string.watchers)
                    )
                }

                Text(
                    text = "Создан: ${r.createdAt.toReadableDate()}",
                    color = AppColors.textPrimary
                )
                Text(
                    text = "Обновлён: ${r.updatedAt.toReadableDate()}",
                    color = AppColors.textPrimary
                )
                Spacer(Modifier.height(8.dp))

                AnimatedVisibility(canGoBack) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                repoDetailsViewModel.onBackPressed(ownerLogin, repoName)
                            },
                        text = "назад",
                        color = AppColors.accentPrimary
                    )
                }
                contentState.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                AppColors.surface,
                                RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                when (item) {
                                    is RepoContentItem.Dir -> repoDetailsViewModel.loadRepoContents(
                                        item.path,
                                        ownerLogin,
                                        repoName
                                    )

                                    is RepoContentItem.File -> {
                                        val url = "${r.htmlUrl}${BASE_URL_PERT}${item.path}"
                                        context.startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                url.toUri()
                                            )
                                        )
                                    }
                                }
                            }
                            .background(AppColors.background)
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (item) {
                                is RepoContentItem.Dir -> Icons.Default.ArrowDropDown
                                is RepoContentItem.File -> Icons.Default.Done
                            },
                            contentDescription = null,
                            tint = AppColors.accentPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = when (item) {
                                is RepoContentItem.Dir -> item.name
                                is RepoContentItem.File -> item.name
                            },
                            color = AppColors.textPrimary
                        )
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
