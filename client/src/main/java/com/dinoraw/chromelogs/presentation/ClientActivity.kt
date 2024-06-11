package com.dinoraw.chromelogs.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.dinoraw.chromelogs.presentation.component.AlertDialogOpenSetting
import com.dinoraw.chromelogs.presentation.model.ClientNavigationDestination
import com.dinoraw.chromelogs.presentation.screen.ClientConfigScreen
import com.dinoraw.chromelogs.presentation.screen.ClientScreen
import com.dinoraw.chromelogs.ui.theme.ChromeLogsTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ClientActivity : ComponentActivity() {
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

    @Composable
    fun Navigation(innerPadding: PaddingValues) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = ClientNavigationDestination.ClientScreen.route) {
            composable(ClientNavigationDestination.ClientScreen.route) {
                ClientScreen(
                    navController = navController,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                )
            }
            dialog(ClientNavigationDestination.ClientConfigScreen.route) {
                ClientConfigScreen(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                )
            }
            dialog(ClientNavigationDestination.OpenSettingDialog.route) {
                AlertDialogOpenSetting(
                    navigateUp = { navController.navigateUp() },
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}



