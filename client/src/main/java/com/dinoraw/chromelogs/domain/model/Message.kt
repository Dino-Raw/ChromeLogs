package com.dinoraw.chromelogs.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Message(
    val key: String,
    val value: String
)
