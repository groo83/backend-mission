package com.murple.chat.common.config

import com.murple.chat.domain.chat.websocket.WebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.*

@Configuration
@EnableWebSocket
class WebSocketConfig(private val webSocketHandler: WebSocketHandler) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(webSocketHandler, "/").setAllowedOrigins("*")
    }
}