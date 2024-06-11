package com.dinoraw.chromelogs.presentation.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dinoraw.chromelogs.domain.model.Config
import com.dinoraw.chromelogs.domain.repository.ServerConfigRepository
import com.dinoraw.chromelogs.presentation.component.ParentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ServerConfigViewModel @Inject constructor(
    private val serverConfigRepository: ServerConfigRepository,
    private val defaultConfig: Config,
): ParentViewModel() {
    var ip: String = defaultConfig.ip
        private set
    var port by mutableStateOf(defaultConfig.port.toString())
        private set

    private val configFlow = viewModelScope.launch(Dispatchers.IO) {
        serverConfigRepository.getConfig().collect { result ->
            result.onSuccess { config ->
                withContext(Dispatchers.Main) {
                    ip = config.ip
                    port = config.port.toString()
                }
            }.onFailure {
                _message.update { "Failed to get configuration" }
            }
        }
    }

    fun changePort(value: String) {
        port = value
    }

    fun saveConfig() {
        viewModelScope.launch(Dispatchers.IO) {
            serverConfigRepository.setConfig(config = defaultConfig.copy(port = port.toInt())).onFailure {
                _message.update { "Failed to save configuration" }
            }
        }
    }

    override fun onCleared() {
        configFlow.cancel()
        super.onCleared()
    }
}