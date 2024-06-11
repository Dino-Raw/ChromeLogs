package com.dinoraw.chromelogs.data.repository

import com.dinoraw.chromelogs.domain.model.Config
import com.dinoraw.chromelogs.domain.model.WebSocketState
import com.dinoraw.chromelogs.data.source.remote.ClientWebSocket
import com.dinoraw.chromelogs.domain.model.GestureServiceResult
import com.dinoraw.chromelogs.domain.model.Message
import com.dinoraw.chromelogs.domain.repository.ClientWebSocketRepository
import com.dinoraw.chromelogs.util.Const.MESSAGE_KEY_GESTURE
import com.dinoraw.chromelogs.util.Const.MESSAGE_KEY_OPEN
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ClientWebSocketRepositoryImpl @Inject constructor(
    private val clientWebSocket: ClientWebSocket,
    private val defaultMessage: Message,
): ClientWebSocketRepository {
    override suspend fun connectToServer(config: Config): Result<WebSocketState> =
        clientWebSocket.connect(config)

    override suspend fun sendMessageToServer(message: Message): Result<Unit> =
        clientWebSocket.send(message)

    override fun receiveMessageFromServer(): Flow<Result<Message>> =
        clientWebSocket.receive()

    override suspend fun disconnectFromServer(): Result<Unit> =
        clientWebSocket.disconnect()

    override suspend fun sendGestureServiceResult(gestureServiceResult: GestureServiceResult): Result<Unit> =
        clientWebSocket.send(
            defaultMessage.copy(
                key = MESSAGE_KEY_GESTURE,
                value = Json.encodeToString(gestureServiceResult)
        ))

    override suspend fun sendOpenedPackage(packageName: String): Result<Unit> =
        clientWebSocket.send(
            defaultMessage.copy(
                key = MESSAGE_KEY_OPEN,
                value = packageName
            )
        )

}