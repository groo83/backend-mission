package com.murple.chat.domain.chat.application

import com.murple.chat.domain.chat.websocket.WebSocketSessionManager
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component
class ChatMessageProcessor (
    private val chatService: ChatService,
    private val socketSessionManager: WebSocketSessionManager
){
    private val logger = KotlinLogging.logger {}

    fun processMessage(session: WebSocketSession, message: TextMessage) {

        logger.info { "${session.id} send messege: ${message.payload}" }

        val mentionedUsers = chatService.extractMentionedUsers(message.payload)

        try {
            if (mentionedUsers.isNotEmpty()) {
                // 특정 세션id에게만 전송
                val resultUsers = chatService.getMentionedUsers(mentionedUsers)
                if (resultUsers.isNotEmpty()) {
                    resultUsers.forEach { user ->
                        socketSessionManager.sendMessageToUser(user.id.toString(), message.payload)
                    }
                    return
                }
            }
            socketSessionManager.broadcastMessage(message.payload)

        } catch (e: Exception) {
            logger.error { "Failed processing message: ${e.message}" }
        }
    }
}