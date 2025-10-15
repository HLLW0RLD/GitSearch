package com.example.gitsearch.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.gitsearch.ui.screen.Main
import com.example.gitsearch.ui.screen.MainScreen
import com.example.gitsearch.ui.screen.RepoDetails
import com.example.gitsearch.ui.screen.RepoDetailsScreen
import com.example.gitsearch.ui.screen.Screen
import com.example.gitsearch.ui.screen.route
import com.example.gitsearch.ui.screen.screenComposable
import com.example.gitsearch.ui.theme.AppTheme
import com.example.gitsearch.utils.LocalNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current

            var startRoute by remember { mutableStateOf<Screen>(Main) }

            CompositionLocalProvider(LocalNavController provides navController) {
                AppTheme {
                    NavHost(
                        modifier = Modifier
                        .statusBarsPadding(),
                        navController = navController,
                        startDestination = startRoute.route()
                    ) {
                        screenComposable<Main> { MainScreen() }
                        screenComposable<RepoDetails> { RepoDetailsScreen() }
                    }
                }
            }
        }
    }
}
