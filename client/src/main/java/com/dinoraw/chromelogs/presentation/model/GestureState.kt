package com.dinoraw.chromelogs.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class GestureServiceState (
    val openedApplication: String,
    val isActive: Boolean,
)