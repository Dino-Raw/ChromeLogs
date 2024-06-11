package com.dinoraw.chromelogs.domain.repository

import com.dinoraw.chromelogs.domain.model.ClientGestureData
import kotlinx.coroutines.flow.Flow

interface ServerGestureRepository {
    fun setGesture(gestureData: ClientGestureData): Result<Unit>
    fun getGesture(): Flow<Result<List<ClientGestureData>>>
    fun deleteGesture(): Result<Unit>
}