package com.dinoraw.chromelogs.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class GestureServiceResult (
    val startOfHorizontalSwipe: Int,
    val endOfHorizontalSwipe: Int,
    val startOfVerticalSwipe: Int,
    val endOfVerticalSwipe: Int,
    val maxHeight: Int,
    val maxWidth: Int,
)