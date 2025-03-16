package com.murple.chat.domain.user.enums

enum class Gender(val code: String) {
    MALE("MALE"),
    FEMALE("FEMALE"),
    ;

    companion object {
        fun of(code: String?): Gender {
            requireNotNull(code) { "code 값이 null일 수 없습니다." }

            return values().find { it.code == code }
                ?: throw IllegalArgumentException("일치하는 값이 없습니다.")
        }
    }
}