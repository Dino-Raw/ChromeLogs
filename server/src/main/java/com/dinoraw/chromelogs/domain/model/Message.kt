package com.dinoraw.chromelogs.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Message(
    val key: String,
    val value: String
)