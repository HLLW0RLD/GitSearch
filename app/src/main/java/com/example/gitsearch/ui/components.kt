package com.example.gitsearch.ui

import androidx.compose.animation.AnimatedVisibility
import com.example.gitsearch.R
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign


@Composable
fun SearchAppBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    onValueChange: ((String) -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    var isSearching by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
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

            AnimatedVisibility(
                visible = isSearching,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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
                                IconButton(onClick = { query = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = Color.White
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
                                    contentDescription = "Clear",
                                    tint = Color.White
                                )
                            }
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = !isSearching,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier,
                        textAlign = TextAlign.Start
                    )

                    IconButton(onClick = {
                        isSearching = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserRow(user: UserModel, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
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
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Перейти"
            )
        }
    }
}

@Composable
fun RepoRow(repo: RepoModel) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
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
                    .clickable { expanded = !expanded }
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(repo.name, fontWeight = FontWeight.Bold)
                    if (!expanded) {
                        Text(
                            "Подробнее",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${repo.stars}", fontSize = 12.sp)
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("${repo.watchers}", fontSize = 12.sp)
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("${repo.forks}", fontSize = 12.sp)
                    Icon(
                        Icons.Default.Share,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Развернуть"
                )
            }

            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = repo.ownerAvatarUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(Modifier.width(8.dp))

                    Column {
                        Text("Автор: ${repo.ownerLogin}", fontSize = 12.sp, color = Color.Gray)
                        Text(
                            "Создан: ${repo.createdAt.take(10)} | Обновлён: ${repo.updatedAt.take(10)}",
                            fontSize = 12.sp, color = Color.Gray
                        )
                    }
                }
                repo.description?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(it, fontSize = 13.sp)
                }
                Text(repo.htmlUrl, fontSize = 12.sp, color = Color.Blue)
            }
        }
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

