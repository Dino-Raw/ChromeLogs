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
import com.dinoraw.chromelogs.presentation.model.ClientNavigationDestination
import com.dinoraw.chromelogs.presentation.model.ClientUIState
import com.dinoraw.chromelogs.util.accessibilityServiceSettingEnabled
import com.dinoraw.chromelogs.util.openBrowser

@Composable
fun ClientScreen(
    navController: NavHostController,
    viewModel: ClientViewModel = hiltViewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val clientUIState by viewModel.clientUIStateFlow.collectAsStateWithLifecycle()
    val context: Context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.message.collect { mes ->
            if(mes != "") {
                Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }

    ClientScreen(
        navigateToConfigScreen = { navController.navigate(ClientNavigationDestination.ClientConfigScreen.route) },
        navigateToOpenSettingDialog = { navController.navigate(ClientNavigationDestination.OpenSettingDialog.route) },
        clientUIState = clientUIState,
        modifier = modifier,
    )
}

@Composable
fun ClientScreen(
    navigateToConfigScreen: () -> Unit = { },
    navigateToOpenSettingDialog: () -> Unit = { },
    clientUIState: ClientUIState = ClientUIState("", true, { }, true),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navigateToConfigScreen() },
            enabled = clientUIState.buttonConfigEnabled,
        ) {
            Text(text = "Config")
        }

        Button(
            onClick = {
                if (!context.accessibilityServiceSettingEnabled()) {
                    navigateToOpenSettingDialog()
                } else {
                    clientUIState.buttonStartStopOnClick.invoke()
                }

            },
            enabled = clientUIState.buttonStartStopEnabled
        ) {
            Text(text = clientUIState.buttonStartStopText)
        }
    }

    if (clientUIState.buttonStartStopText == "Stop")
        context.openBrowser()
}