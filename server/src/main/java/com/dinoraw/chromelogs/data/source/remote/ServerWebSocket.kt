package com.dinoraw.chromelogs.data.source.remote

import android.util.Log
import com.dinoraw.chromelogs.data.model.Connection
import com.dinoraw.chromelogs.domain.model.ClientGestureData
import com.dinoraw.chromelogs.domain.model.Config
import com.dinoraw.chromelogs.domain.model.GestureServiceResult
import com.dinoraw.chromelogs.domain.model.Message
import com.dinoraw.chromelogs.util.Const.CHROME_PACKAGE_NAME
import com.dinoraw.chromelogs.util.Const.MESSAGE_CLOSE_SESSION
import com.dinoraw.chromelogs.util.Const.MESSAGE_KEY_GESTURE
import com.dinoraw.chromelogs.util.Const.MESSAGE_KEY_OPEN
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.origin
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.Duration
import java.util.Collections
import javax.inject.Inject

class ServerWebSocket @Inject constructor(
    private val defaultClientGestureData: ClientGestureData
) {
    companion object {
        var instance: NettyApplicationEngine? = null
    }

    private var _clientGestureDataStateFlow: MutableStateFlow<ClientGestureData?> = MutableStateFlow(null)
    val clientGestureDataStateFlow: StateFlow<ClientGestureData?> get() = _clientGestureDataStateFlow.asStateFlow()

    fun start(config: Config): Result<Unit> = runCatching {
        instance = embeddedServer(
            factory = Netty,
            host = config.ip,
            port = config.port,
        ) {
            configurePlugin()
            configureRouting()
        }.start(false)
    }

    fun stop(): Result<Unit> = runCatching {
        if (instance != null) {
            instance!!.stop()
            instance = null
        }
    }

    private fun Application.configurePlugin() {
        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(150)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
    }

    private fun Application.configureRouting() {
        routing {
            val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
            webSocket {
                val thisConnection = Connection(this)
                connections += thisConnection
                var sendMessageJob: Job? = null
                incoming.consumeEach { frame ->
                    val receivedText = if (frame is Frame.Text) frame.readText() else ""

                     if (receivedText.contains("key") && receivedText.contains("value")) {
                        val mes = Json.decodeFromString<Message>(receivedText)
                        when (mes.key) {
                            MESSAGE_CLOSE_SESSION -> {
                                connections.remove(thisConnection)
                                thisConnection.session.cancel()
                            }
                            MESSAGE_KEY_GESTURE -> {
                                _clientGestureDataStateFlow.update { defaultClientGestureData.copy(
                                    ip = call.request.origin.remoteAddress,
                                    gestureServiceResult = Json.decodeFromString<GestureServiceResult>(mes.value)
                                ) }
                            }
                            MESSAGE_KEY_OPEN -> {
                                if (mes.value == CHROME_PACKAGE_NAME && sendMessageJob == null) {
                                        sendMessageJob = launch {
                                            while (true) {
                                                val startEndCoordinate ="${(20..80).random().toFloat() / 100}:${(20..80).random().toFloat() / 100}"
                                                thisConnection.session.send(startEndCoordinate)
                                                delay(500)
                                            }
                                        }
                                } else if (mes.value != CHROME_PACKAGE_NAME && sendMessageJob != null) {
                                    sendMessageJob?.cancel()
                                    sendMessageJob = null
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}



