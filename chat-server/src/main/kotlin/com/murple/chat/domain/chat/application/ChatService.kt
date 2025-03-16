package com.murple.chat.domain.chat.application

import com.murple.chat.domain.user.User
import com.murple.chat.domain.user.application.UserService
import org.springframework.stereotype.Service

@Service
class ChatService (
    val userService: UserService
){
    fun extractMentionedUsers(content: String): List<String> {
        val mentionPattern = "@([\\p{Alnum}가-힣]{1,1024})".toRegex()
        return mentionPattern.findAll(content).map { it.groupValues[1] }.toList()
    }

    fun getMentionedUsers(names: List<String>): List<User> {
        return userService.findUsersByNamesOrderByCreatedAtAsc(names)
    }
}

