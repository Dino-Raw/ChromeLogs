package com.dinoraw.chromelogs.data.source.remote

import android.util.Log
import com.dinoraw.chromelogs.domain.model.Config
import com.dinoraw.chromelogs.domain.model.Message
import com.dinoraw.chromelogs.domain.model.WebSocketState
import com.dinoraw.chromelogs.util.Const.MESSAGE_CLOSE_SESSION
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ClientWebSocket @Inject constructor(
    private val client: HttpClient,
    private val defaultMessage: Message,
) {
    private var session: WebSocketSession? = null

    suspend fun connect(config: Config): Result<WebSocketState> {
        try {
            if (session != null) {
                session?.close()
                session = null
            }
            session = client.webSocketSession {
                url.host = config.ip
                url.port = config.port
            }
            return Result.success(WebSocketState.CONNECTED)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun send(message: Message): Result<Unit> = try {
        session!!.send(Frame.Text(Json.encodeToString<Message>(message)))
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }


    fun receive(): Flow<Result<Message>> = try {
        session?.incoming
            ?.receiveAsFlow()
            ?.filter { it is Frame.Text }
            ?.map { receiveFrame ->
                val data = (receiveFrame as Frame.Text).readText()
                Result.success(defaultMessage.copy(value = data))
            } ?: flow {  }
    } catch (e: Exception) {
        flow { Result.failure<Message>(e) }
    }

    suspend fun disconnect(): Result<Unit> = try {
        send(defaultMessage.copy(key = MESSAGE_CLOSE_SESSION))
        session?.close()
        session = null
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}