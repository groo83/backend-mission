package com.murple.chat.domain.user

import com.murple.chat.common.entity.BaseEntity
import com.murple.chat.domain.user.enums.Label
import jakarta.persistence.*
import jakarta.validation.constraints.Pattern

@Entity
data class Phone(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,

    @Column(nullable = false)
    @field:Pattern(
        regexp = "^\\+\\d{1,15}\$",
        message = "전화번호는 E.164 형식(+국가코드와 숫자)이어야 합니다. (e.g. +821012345678)"
    )
    var number: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    var countryCode: String? = null,

    @Column(nullable = false)
    var isCertified: Boolean = false,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val label: Label? = null

): BaseEntity() {
    companion object {
        const val COUNTRY_CODE_KR = "+82"
        const val COUNTRY_CODE_US = "+1"
        const val COUNTRY_CODE_JP = "+81"
        const val COUNTRY_CODE_CN = "+86"
    }

    init {
        if (countryCode == null) {
            countryCode = extractCountryCode(number) // 전화번호에서 국가 코드 추출
        }
    }

    private fun extractCountryCode(phoneNumber: String): String? {
        return when {
            phoneNumber.startsWith(COUNTRY_CODE_KR) -> "KR"
            phoneNumber.startsWith(COUNTRY_CODE_US) -> "US"
            phoneNumber.startsWith(COUNTRY_CODE_JP) -> "JP"
            phoneNumber.startsWith(COUNTRY_CODE_CN) -> "CN"
            else -> null // 그 외 국가번호는 자동 추출하지 않음
        }
    }
}