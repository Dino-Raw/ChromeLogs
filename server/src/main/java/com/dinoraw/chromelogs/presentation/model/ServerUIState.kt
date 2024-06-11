package com.dinoraw.chromelogs.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class ServerUIState (
    val buttonStartStopText: String,
    val buttonStartStopOnClick: () -> Unit,
    val buttonConfigEnabled: Boolean,
)