package com.dinoraw.chromelogs.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.dinoraw.chromelogs.presentation.model.ServerNavigationDestination
import com.dinoraw.chromelogs.presentation.screen.ServerConfigScreen
import com.dinoraw.chromelogs.presentation.screen.ServerLogScreen
import com.dinoraw.chromelogs.presentation.screen.ServerScreen
import com.dinoraw.chromelogs.ui.theme.ChromeLogsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChromeLogsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(innerPadding)
                }
            }
        }
    }
}

@Composable
fun Navigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ServerNavigationDestination.ServerScreen.route) {
        composable(ServerNavigationDestination.ServerScreen.route) {
            ServerScreen(
                navController = navController,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            )
        }
        dialog(ServerNavigationDestination.ServerConfigScreen.route) {
            ServerConfigScreen(
                navController = navController,
                modifier = Modifier.padding(innerPadding).background(color = Color.White)
            )
        }

        composable(ServerNavigationDestination.ServerLogScreen.route) {
            ServerLogScreen(
                navController = navController,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
        }
    }
}