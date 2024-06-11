package com.dinoraw.chromelogs.presentation.screen

import androidx.lifecycle.viewModelScope
import com.dinoraw.chromelogs.domain.model.ClientGestureData
import com.dinoraw.chromelogs.domain.repository.ServerGestureRepository
import com.dinoraw.chromelogs.presentation.component.ParentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerLogViewModel @Inject constructor(
    private val serverGestureRepository: ServerGestureRepository
): ParentViewModel() {
    private var _gestureDataListState: MutableStateFlow<List<ClientGestureData>> = MutableStateFlow(listOf())
    val gestureDataListState: StateFlow<List<ClientGestureData>> = _gestureDataListState.asStateFlow()

    private val gestureDataFlow = viewModelScope.launch(Dispatchers.IO) {
        serverGestureRepository.getGesture().collect {
            it.onSuccess { newData ->
                _gestureDataListState.update { newData }
            }.onFailure {
                _message.update { "Failed to get logs" }
            }
        }
    }

    fun clear() {
        viewModelScope.launch(Dispatchers.IO) {
            serverGestureRepository.deleteGesture().onFailure {
                _message.update { "Failed to clear logs" }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        gestureDataFlow.cancel()
    }
}