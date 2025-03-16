package com.murple.chat.domain.chat.websocket


import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Service
class WebSocketSessionManager {
    private val logger = KotlinLogging.logger {}

    private val sessions: MutableMap<String, WebSocketSession> = ConcurrentHashMap()

    fun addSession(session: WebSocketSession) {
        sessions[session.id] = session
        logger.info { "Session added: ${session.id}, UserId: ${session.attributes["userId"]}" }
        broadcastMessage("userId ${session.attributes["userId"]}님이 입장하셨습니다.")
    }

    fun removeSession(session: WebSocketSession) {
        val sessionId = session.id
        sessions.remove(sessionId)
        logger.info { "Session removed: $sessionId, UserId: ${session.attributes["userId"]}" }
        broadcastMessage("userId ${session.attributes["userId"]}님이 퇴장하셨습니다.")
    }

    fun broadcastMessage(message: String) {
        val currentSessions = sessions.values.toList() // 스냅샷 생성
        currentSessions.forEach { session ->
            sendMessage(session, message)
        }
    }

    fun sendMessageToUser(targetUserId: String, message: String) {
        sessions.values.find { it.attributes["userId"] == targetUserId }?.let { session -> // null이 아닐 때만 실행
            sendMessage(session, message)
        } ?: logger.warn { "Session not found for ID: $targetUserId" }
    }

    private fun sendMessage(session: WebSocketSession, message: String) {
        try {
            if (session.isOpen) {
                session.sendMessage(TextMessage(message))
            } else {
                logger.warn { "Session ${session.id} is not open." }
            }
        } catch (e: IllegalStateException) {
            logger.warn { "Session ${session.id} is already closed." }
        } catch (e: Exception) {
            logger.error { "Failed sendMessage ${session.id}: ${e.message}" }
        }
    }
}