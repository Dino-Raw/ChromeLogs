package com.dinoraw.chromelogs.domain.repository

import com.dinoraw.chromelogs.domain.model.Config
import com.dinoraw.chromelogs.domain.model.GestureServiceResult
import com.dinoraw.chromelogs.domain.model.Message
import com.dinoraw.chromelogs.domain.model.WebSocketState
import kotlinx.coroutines.flow.Flow

interface ClientWebSocketRepository {
    suspend fun connectToServer(config: Config): Result<WebSocketState>
    suspend fun sendMessageToServer(message: Message): Result<Unit>
    fun receiveMessageFromServer(): Flow<Result<Message>>
    suspend fun disconnectFromServer(): Result<Unit>
    suspend fun sendGestureServiceResult(gestureServiceResult: GestureServiceResult): Result<Unit>
    suspend fun sendOpenedPackage(packageName: String): Result<Unit>
}