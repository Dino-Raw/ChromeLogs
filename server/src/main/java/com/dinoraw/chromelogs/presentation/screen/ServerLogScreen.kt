package com.dinoraw.chromelogs.presentation.screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.dinoraw.chromelogs.domain.model.ClientGestureData
import com.dinoraw.chromelogs.presentation.component.LogItem

@Composable
fun ServerLogScreen(
    navController: NavHostController,
    viewModel: ServerLogViewModel = hiltViewModel(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val gestureList by viewModel.gestureDataListState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    val context: Context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.message.collect { mes ->
            if(mes != "") {
                Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }

    ServerLogScreen(
        navigateUp = navController::navigateUp,
        gestureList = gestureList,
        clearLog = viewModel::clear,
        listState = listState,
        modifier = modifier

    )
}

@Composable
fun ServerLogScreen(
    navigateUp: () -> Unit = { },
    gestureList: List<ClientGestureData> = listOf(),
    clearLog: () -> Unit,
    listState: LazyListState,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    LaunchedEffect(key1 = gestureList.size) {
        if (gestureList.isNotEmpty())
            listState.scrollToItem(gestureList.size - 1)
    }

    Box(
        modifier = modifier
    ) {
        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(0.9f)
            ) {
                items(
                    gestureList,
                    key = { it.id }
                ) {
                    LogItem(clientGestureData = it, modifier = Modifier.fillMaxWidth().padding(10.dp))
                }
            }

            Row (
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxWidth()
            ) {
                Button(onClick = navigateUp) {
                    Text(text = "Back")
                }

                Button(onClick = clearLog) {
                    Text(text = "Clear")
                }
            }
        }
    }
}