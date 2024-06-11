package com.dinoraw.chromelogs.presentation.screen

import androidx.lifecycle.viewModelScope
import com.dinoraw.chromelogs.domain.model.Config
import com.dinoraw.chromelogs.domain.model.WebSocketState
import com.dinoraw.chromelogs.domain.repository.ClientConfigRepository
import com.dinoraw.chromelogs.domain.repository.ClientWebSocketRepository
import com.dinoraw.chromelogs.presentation.component.ParentViewModel
import com.dinoraw.chromelogs.presentation.model.ClientUIState
import com.dinoraw.chromelogs.presentation.service.GestureService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor (
    private val clientWebSocketRepository: ClientWebSocketRepository,
    private val clientConfigRepository: ClientConfigRepository,
    private val defaultClientUIState: ClientUIState,
    private val defaultConfig: Config,
) : ParentViewModel() {
    private var gestureObserver: Job? = null
    private var serverReceiver: Job? = null
    private var config: Config = defaultConfig

    private var _clientUIStateFlow: MutableStateFlow<ClientUIState> = MutableStateFlow(
        defaultClientUIState.copy(buttonStartStopOnClick = { start() })
    )
    val clientUIStateFlow: StateFlow<ClientUIState> get() = _clientUIStateFlow.asStateFlow()

    private val configFlow = viewModelScope.launch(Dispatchers.IO) {
        clientConfigRepository.getConfig().collect {  result ->
            result.onSuccess {  newConfig ->
                config = newConfig
            }.onFailure {
                _message.update { "Failed to get configuration" }
            }
        }
    }

    private fun start() {
        viewModelScope.launch {
            clientWebSocketRepository.connectToServer(config)
                .onSuccess {
                    webSocketStateHandler(it)
                }.onFailure {
                    _message.update { "Failed to connect to server" }
                }
        }
    }

    private fun stop() {
        viewModelScope.launch {
            clientWebSocketRepository.disconnectFromServer().onSuccess {
                webSocketStateHandler(WebSocketState.DISCONNECTED)
            }
        }
    }

    private fun webSocketStateHandler(webSocketState: WebSocketState) {
        when (webSocketState) {
            WebSocketState.DISCONNECTED -> {
                serverReceiver?.cancel()
                serverReceiver = null

                _clientUIStateFlow.update { it.copy(
                    buttonStartStopText = "Start",
                    buttonStartStopEnabled = true,
                    buttonStartStopOnClick = { start() },
                    buttonConfigEnabled = true,
                ) }
            } WebSocketState.CONNECTING -> {
            _clientUIStateFlow.update { it.copy(
                buttonStartStopText = "Start",
                buttonStartStopEnabled = false,
                buttonStartStopOnClick = { },
                buttonConfigEnabled = false,
            ) }
        } WebSocketState.CONNECTED -> {
            observeGestureService()
            observeReceiver()

            _clientUIStateFlow.update { it.copy(
                buttonStartStopText = "Stop",
                buttonStartStopEnabled = true,
                buttonStartStopOnClick = { stop() },
                buttonConfigEnabled = false,
            ) }
        } }
    }

    private fun observeGestureService() {
        if (gestureObserver == null)
            gestureObserver = viewModelScope.launch {
                GestureService.instance?.serviceState?.collect {
                    clientWebSocketRepository.sendOpenedPackage(it.openedApplication)
                    if (!it.isActive) this.cancel()
                }
            }
    }

    private fun observeReceiver() {
        if (serverReceiver == null)
            serverReceiver = viewModelScope.launch {
                clientWebSocketRepository.receiveMessageFromServer().collect { result ->
                    result.onSuccess { mes ->
                        GestureService.instance?.onSwipe(
                            startY = mes.value.substringBefore(":").toFloat(),
                            endY = mes.value.substringAfter(":").toFloat(),
                        )?.also {
                            clientWebSocketRepository.sendGestureServiceResult(it)
                        }
                    }

                }
            }
    }

    override fun onCleared() {
        configFlow.cancel()
        serverReceiver?.cancel()
        serverReceiver = null
        super.onCleared()
    }
}