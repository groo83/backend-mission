package com.murple.chat.domain.user

import com.murple.chat.common.entity.BaseEntity
import com.murple.chat.domain.user.enums.Gender
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(length = 1024)
    @field:Email(message = "올바르지 않은 이메일 형식입니다.")
    var email: String? = null,

    @Column(nullable = false)
    @field:Size(min = 1, max = 1024, message = "이름은 최소 1글자에서 1024글자 사이여야 합니다.")
    var name: String,

    var age: Int? = null,

    @Enumerated(EnumType.STRING)
    var gender: Gender? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    @field:Size(max = 8, message = "전화번호는 최대 8개까지 입력 가능합니다.")
    var phones: MutableList<Phone> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    @field:Size(max = 8, message = "주소는 최대 8개까지 입력 가능합니다.")
    var addresses: MutableList<Address> = mutableListOf(),

): BaseEntity()