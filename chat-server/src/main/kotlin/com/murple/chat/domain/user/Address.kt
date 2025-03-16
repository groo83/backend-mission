package com.murple.chat.domain.user

import com.murple.chat.common.entity.BaseEntity
import com.murple.chat.domain.user.enums.Label
import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
data class Address(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(length = 1024, nullable = false)
    @get:Size(max = 1024, message = "주소는 최대 1024자까지 가능합니다.")
    val address: String,

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    val label: Label? = null
): BaseEntity()
