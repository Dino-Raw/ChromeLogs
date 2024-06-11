package com.dinoraw.chromelogs.domain.model

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@Entity
data class ClientGestureData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ip: String,
    @Embedded
    val gestureServiceResult: GestureServiceResult,
)