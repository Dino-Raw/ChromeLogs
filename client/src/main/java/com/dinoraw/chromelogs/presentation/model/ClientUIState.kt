package com.dinoraw.chromelogs.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class ClientUIState (
    val buttonStartStopText: String,
    val buttonStartStopEnabled: Boolean,
    val buttonStartStopOnClick: () -> Unit,
    val buttonConfigEnabled: Boolean,
)
