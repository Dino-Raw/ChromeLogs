package com.dinoraw.chromelogs.presentation.component

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class ParentViewModel: ViewModel() {
    protected var _message: MutableStateFlow<String> = MutableStateFlow("")
    val message: StateFlow<String> get() = _message.asStateFlow()

    fun clearMessage() {
        _message.update { "" }
    }
}