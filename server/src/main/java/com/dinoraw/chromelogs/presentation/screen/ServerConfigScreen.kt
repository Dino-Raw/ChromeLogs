package com.dinoraw.chromelogs.presentation.screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun ServerConfigScreen(
    navController: NavHostController,
    viewModel: ServerConfigViewModel = hiltViewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.message.collect { mes ->
            if(mes != "") {
                Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }

    ServerConfigScreen(
        navigateUp = navController::navigateUp,
        ip = viewModel.ip,
        port = viewModel.port,
        changePort = viewModel::changePort,
        saveConfig = viewModel::saveConfig,
        modifier = modifier,
    )
}

@Composable
fun ServerConfigScreen(
    navigateUp: () -> Unit = { },
    ip: String = "127.0.0.1",
    port: String = "8080",
    changePort: (newIp: String) -> Unit = { },
    saveConfig: () -> Unit = { },
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Card (modifier) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "IP $ip",
            )
            Text(
                text = "Enter a free port:"
            )
            TextField(
                value = port,
                onValueChange = { newPort -> changePort(newPort.filter { it.isDigit() }) },
                label = { Text(text = "Port") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(
                onClick = {
                    saveConfig()
                    navigateUp()
                }
            ) { Text("Save") }
        }
    }
}