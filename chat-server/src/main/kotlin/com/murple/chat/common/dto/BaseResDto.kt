package com.murple.chat.common.dto

open class BaseResDto(
    val code: Int = 200,            // 응답 코드 (기본값: 200)
    val message: String = "Success" // 결과 메시지 (기본값: "Success")
) {
    companion object {
        fun ok(): BaseResDto = BaseResDto()
    }

    override fun toString(): String {
        return "BaseResDto(code=$code, message='$message')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseResDto) return false
        return code == other.code && message == other.message
    }

    override fun hashCode(): Int {
        return 31 * code + message.hashCode()
    }
}
