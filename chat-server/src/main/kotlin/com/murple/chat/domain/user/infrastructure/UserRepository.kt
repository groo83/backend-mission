package com.murple.chat.domain.user.infrastructure

import com.murple.chat.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<User, Long> {
    fun findUserById(id: Long): User?

    @Query(
        value = """
            SELECT *
            FROM (
                SELECT u.*,
                    ROW_NUMBER() OVER (PARTITION BY u.name ORDER BY u.created_at ASC) AS rn
                FROM users u
                WHERE u.name IN :names
            ) sub
            WHERE sub.rn <= 5;
        """,
        nativeQuery = true
    )
    fun findUsersByNamesOrderByCreatedAtAsc(@Param("names") names: List<String>): List<User>
}