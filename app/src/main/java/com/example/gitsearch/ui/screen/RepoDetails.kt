package com.example.gitsearch.ui.screen

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
object RepoDetails : Screen

enum class ItemType { FILE, FOLDER }

@Composable
fun RepoDetailsScreen() {

}
