package com.dinoraw.chromelogs.presentation.screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.dinoraw.chromelogs.presentation.model.ServerNavigationDestination
import com.dinoraw.chromelogs.presentation.model.ServerUIState

@Composable
fun ServerScreen(
    navController: NavHostController,
    viewModel: ServerViewModel = hiltViewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val serverUIState by viewModel.serverUIStateFlow.collectAsStateWithLifecycle()

    val context: Context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.message.collect { mes ->
            if(mes != "") {
                Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }
    ServerScreen(
        serverUIState = serverUIState,
        navigateToConfigScreen = { navController.navigate(ServerNavigationDestination.ServerConfigScreen.route) },
        navigateToLogScreen = { navController.navigate(ServerNavigationDestination.ServerLogScreen.route) },
        modifier = modifier,
    )
}

@Composable
fun ServerScreen(
    serverUIState: ServerUIState = ServerUIState("", { }, true),
    navigateToConfigScreen: () -> Unit = { },
    navigateToLogScreen: () -> Unit = { },
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navigateToConfigScreen() },
            enabled = serverUIState.buttonConfigEnabled
        ) {
            Text(text = "Config")
        }

        Button(
            onClick = { serverUIState.buttonStartStopOnClick() },
        ) {
            Text(text = serverUIState.buttonStartStopText)
        }

        Button(
            onClick = { navigateToLogScreen() },
        ) {
            Text(text = "Logs")
        }
    }
}