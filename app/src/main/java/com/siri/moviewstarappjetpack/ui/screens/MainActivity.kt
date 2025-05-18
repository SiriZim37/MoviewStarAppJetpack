package com.siri.moviewstarappjetpack.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.siri.moviewstarappjetpack.ui.theme.MoviewStarAppJetpackTheme
import com.siri.ui.SplashScreen
import com.siri.upcoming.UpcomingMoviesScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviewStarAppJetpackTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") {
                        SplashScreen(navController = navController)
                    }
                    composable("upcoming_movies") {
                        UpcomingMoviesScreen()
                    }
                }
            }
        }
    }
}