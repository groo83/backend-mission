package com.murple.chat.domain.chat.websocket

import com.murple.chat.domain.chat.application.ChatMessageProcessor
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.atomic.AtomicInteger

@Component
class WebSocketHandler(
    private val socketSessionManager: WebSocketSessionManager,
    private val chatMessageProcessor: ChatMessageProcessor
): TextWebSocketHandler() {

    private val logger = KotlinLogging.logger {}
    private val sessionIdCounter = AtomicInteger(1) // 세션 ID 부여용

    override fun afterConnectionEstablished(session: WebSocketSession) {
        // 임의의 userid로 사용자 인증 대체
        val userId = sessionIdCounter.getAndUpdate { current ->
            current + 1
        }.toString()
        session.attributes["userId"] = userId

        socketSessionManager.addSession(session)
        logger.info { "Connection Established: ${session.id}" }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error { "Transport Error ${session.id}: ${exception.message}" }
        // 오류 발생 시 세션 정리
        socketSessionManager.removeSession(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        chatMessageProcessor.processMessage(session, message)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        socketSessionManager.removeSession(session)
        logger.info { "Connection Closed: ${session.id}" }
    }
}