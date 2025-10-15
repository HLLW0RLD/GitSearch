package com.example.gitsearch.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gitsearch.data.model.RepoModel
import com.example.gitsearch.data.model.UserModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.gitsearch.R
import com.example.gitsearch.ui.theme.AppColors
import com.example.gitsearch.utils.toReadableDate


@Composable
fun SearchAppBar(
    title: String,
    needSearch: Boolean = true,
    onBackClick: (() -> Unit)? = null,
    onValueChange: ((String) -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    var isSearching by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    val transition = updateTransition(targetState = isSearching, label = "searchTransition")
    val searchWidth by transition.animateDp(
        label = "widthAnim",
        transitionSpec = { tween(durationMillis = 350, easing = FastOutSlowInEasing) }
    ) { expanded ->
        if (expanded) 280.dp else 48.dp
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(
                onClick = {
                    onBackClick?.invoke()
                    if (isSearching) isSearching = false
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = Color.White
                )
            }

            if (needSearch) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(!isSearching){
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleLarge,
                            color = AppColors.textPrimary,
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(Modifier.size(8.dp))

                    Box(
                        modifier = Modifier
                            .width(searchWidth)
                            .clip(RoundedCornerShape(20.dp))
                            .background(AppColors.background),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (isSearching) {
                            TextField(
                                value = query,
                                onValueChange = {
                                    query = it
                                    onValueChange?.invoke(query)
                                },
                                placeholder = { Text("Поиск по GitHub...") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp)),
                                leadingIcon = {
                                    if (query.isNotEmpty()) {
                                        IconButton(onClick = {
                                            query = ""
                                            onValueChange?.invoke(query)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "",
                                                tint = AppColors.textPrimary
                                            )
                                        }
                                    }
                                },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            onSearchClick?.invoke()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "",
                                            tint = AppColors.textPrimary
                                        )
                                    }
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = AppColors.background,
                                    unfocusedContainerColor = AppColors.background,
                                    focusedTextColor = AppColors.textPrimary,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                )
                            )
                        } else {
                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.Center),
                                onClick = { isSearching = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "",
                                    tint = AppColors.textPrimary
                                )
                            }
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        color = AppColors.textPrimary,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}

@Composable
fun UserRow(user: UserModel, onOpenClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onOpenClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = user.login,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                color = AppColors.textPrimary
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "",
                tint = AppColors.accentPrimary
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RepoRow(
    repo: RepoModel,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onOpenClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onExpandChange(!expanded) }
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandChange(!expanded) }
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(repo.name, fontWeight = FontWeight.Bold)
                    if (!expanded) {
                        Text(
                            text = stringResource(R.string.more),
                            fontSize = 12.sp,
                            color = AppColors.accentPrimary
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .background(
                            color = AppColors.accentPrimary,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = AppColors.background,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "${repo.stars}",
                        fontSize = 12.sp,
                        color = AppColors.background
                    )
                    Spacer(Modifier.width(6.dp))

                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = AppColors.background,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "${repo.watchers}",
                        fontSize = 12.sp,
                        color = AppColors.background
                    )
                    Spacer(Modifier.width(6.dp))

                    Icon(
                        Icons.Default.Menu,
                        contentDescription = null,
                        tint = AppColors.background,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "${repo.forks}",
                        fontSize = 12.sp,
                        color = AppColors.background
                    )
                }
            }

            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onOpenClick()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        AsyncImage(
                            model = repo.ownerAvatarUrl,
                            contentDescription = "",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.width(8.dp))

                        Column {
                            Text(
                                text = stringResource(R.string.author, repo.ownerLogin),
                                fontSize = 12.sp,
                                color = AppColors.textSecondary
                            )
                            Text(
                                text = stringResource(
                                    R.string.created,
                                    repo.createdAt.take(10).toReadableDate()
                                ),
                                fontSize = 12.sp,
                                color = AppColors.textSecondary
                            )
                            Text(
                                text = stringResource(
                                    R.string.updated,
                                    repo.updatedAt.take(10).toReadableDate()
                                ),
                                fontSize = 12.sp,
                                color = AppColors.textSecondary
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "",
                            tint = AppColors.accentPrimary
                        )
                    }
                }
                repo.description?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.description, it),
                        fontSize = 13.sp,
                        color = AppColors.textPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, count: Int, text: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = AppColors.accentPrimary
            )
            Text(
                text = count.toString(),
                color = AppColors.textPrimary,
            )
        }
        Text(
            text = text,
            fontSize = 10.sp,
            color = AppColors.textPrimary
        )
    }
}

@Composable
fun ShimmerContainer(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (isLoading) {
        val transition = rememberInfiniteTransition(label = "")
        val translateAnim by transition.animateFloat(
            0f, 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing)
            ),
            label = ""
        )

        val brush = Brush.linearGradient(
            listOf(
                Color.Black.copy(alpha = 0.6f),
                Color.Gray.copy(alpha = 0.3f),
                Color.LightGray.copy(alpha = 0.6f)
            ),
            start = Offset(translateAnim - 1000f, 0f),
            end = Offset(translateAnim, 0f)
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(brush)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }
        }
    } else {
        content()
    }
}

