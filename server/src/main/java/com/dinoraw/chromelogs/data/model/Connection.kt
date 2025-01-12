package com.dinoraw.chromelogs.data.model

import io.ktor.websocket.DefaultWebSocketSession
import java.util.concurrent.atomic.AtomicInteger

class Connection(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val name = "user${lastId.getAndIncrement()}"
}