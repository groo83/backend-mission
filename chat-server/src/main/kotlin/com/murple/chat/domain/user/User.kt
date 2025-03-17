package com.murple.chat.domain.user

import com.murple.chat.common.entity.BaseEntity
import com.murple.chat.domain.user.dto.UserReqDto
import com.murple.chat.domain.user.enums.Gender
import com.murple.chat.domain.user.enums.Label
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
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
    @field:Size(min = 3, max = 1024, message = "이름은 최소 3글자에서 1024글자 사이여야 합니다.")
    var name: String,

    @field:Min(0, message = "나이는 0 이상이어야 합니다.")
    var age: Int? = null,

    @Enumerated(EnumType.STRING)
    var gender: Gender? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    @field:Size(max = 8, message = "전화번호는 최대 8개까지 입력 가능합니다.")
    var phones: MutableList<Phone> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    @field:Size(max = 8, message = "주소는 최대 8개까지 입력 가능합니다.")
    var addresses: MutableList<Address> = mutableListOf(),

): BaseEntity() {

    companion object {
        fun from(reqDto: UserReqDto): User {
            val user = getUserBasic(reqDto)
            getUserAddress(user, reqDto)
            getUserPhone(user, reqDto)
            return user;
        }

        private fun getUserBasic(reqDto: UserReqDto):User {
            return User(
                name = reqDto.name,
                age = reqDto.age,
                gender = reqDto.gender?.let { Gender.of(it) },
                email = reqDto.email
            )
        }

        private fun getUserAddress(user: User, reqDto: UserReqDto) {
            user.addresses = reqDto.addresses?.map { dto ->
                Address(
                    label = Label.of(dto.label),
                    address = dto.address,
                    user = user
                )
            }?.toMutableList() ?: mutableListOf()
        }

        private fun getUserPhone(user: User, reqDto: UserReqDto) {
            user.phones = reqDto.phones?.map { dto ->
                Phone(
                    label = Label.of(dto.label),
                    number = dto.number,
                    countryCode = dto.countryCode,
                    isCertified = dto.isCertified,
                    user = user
                )
            }?.toMutableList() ?: mutableListOf()
        }
    }

    fun update(
        name: String,
        age: Int?,
        gender: Gender?,
        email: String?,
        phones: MutableList<Phone>,
        addresses: MutableList<Address>
    ) {
        this.name = name
        this.age = age
        this.gender = gender
        this.email = email
        this.phones = phones
        this.addresses = addresses
    }
}