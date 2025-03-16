package com.murple.chat.domain.user.infrastructure

import com.murple.chat.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findUserById(id: Long): User?
}