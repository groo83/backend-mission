package com.murple.chat.domain.user.enums

enum class Label(val code: String) {
    HOME("HOME"),
    WORK("WORK"),
    OTHER("OTHER"),
    ;

    companion object {
        fun of(code: String?): Label {
            requireNotNull(code) { "code 값이 null일 수 없습니다." }

            return Label.values().find { it.code == code }
                ?: throw IllegalArgumentException("일치하는 값이 없습니다.")
        }
    }

}