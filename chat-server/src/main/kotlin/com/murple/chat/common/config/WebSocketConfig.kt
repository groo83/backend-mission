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

    /**
     * 추후 클라이언트와 함께 STOMP 구현
     */
    /*
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker(STIMPLE_BROKER_TOPIC)

    }

    companion object {
        private const val STIMPLE_BROKER_TOPIC: String = "/topic";
        private const val PUBLISH: String = "/app";
    }
    */
}