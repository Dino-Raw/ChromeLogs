package com.dinoraw.chromelogs.presentation.screen


import androidx.lifecycle.viewModelScope
import com.dinoraw.chromelogs.data.source.remote.ServerWebSocket
import com.dinoraw.chromelogs.domain.model.Config
import com.dinoraw.chromelogs.domain.repository.ServerConfigRepository
import com.dinoraw.chromelogs.domain.repository.ServerGestureRepository
import com.dinoraw.chromelogs.presentation.component.ParentViewModel
import com.dinoraw.chromelogs.presentation.model.ServerUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerViewModel @Inject constructor(
    private val serverConfigRepository: ServerConfigRepository,
    private val serverGestureRepository: ServerGestureRepository,
    private val defaultConfig: Config,
    private val defaultServerUIState: ServerUIState,
    private val serverWebSocket: ServerWebSocket,
) : ParentViewModel() {
    private var config: Config = defaultConfig
    private var clientGestureDataStateFlow: Job? = null

    private var _serverUIStateFlow: MutableStateFlow<ServerUIState> = MutableStateFlow(
        defaultServerUIState.copy(
            buttonStartStopOnClick = { start() })
    )
    val serverUIStateFlow: StateFlow<ServerUIState> get() = _serverUIStateFlow.asStateFlow()

    private val configFlow = viewModelScope.launch(Dispatchers.IO) {
        serverConfigRepository.getConfig().collect {  result ->
            result.onSuccess {  newConfig ->
                config = newConfig
            }.onFailure {
                _message.update { "Failed to get configuration" }
            }
        }
    }

    private fun start() {
        viewModelScope.launch {
            serverWebSocket.start(config).onSuccess {
                _serverUIStateFlow.update { it.copy(
                    buttonStartStopText = "Stop",
                    buttonConfigEnabled = false,
                    buttonStartStopOnClick = { stop() }
                ) }

                if (clientGestureDataStateFlow == null)
                    clientGestureDataStateFlow = viewModelScope.launch {
                        serverWebSocket.clientGestureDataStateFlow.collect {
                            launch(Dispatchers.IO) {
                                if (it != null) {
                                    serverGestureRepository.setGesture(it)
                                }
                            }

                        }
                    }
            }.onFailure {
                _message.update { "Failed to start the server" }
            }
        }
    }

    private fun stop() {
        viewModelScope.launch {
            serverWebSocket.stop().onSuccess {
                _serverUIStateFlow.update { it.copy(
                    buttonStartStopText = "Start",
                    buttonConfigEnabled = true,
                    buttonStartStopOnClick = { start() }
                ) }
            }.onFailure {
                _message.update { "Failed to stop the server" }
            }
        }
    }

    override fun onCleared() {
        configFlow.cancel()
        super.onCleared()
    }
}