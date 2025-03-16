package com.murple.chat.common.dto

import com.murple.chat.common.code.ErrorCode

data class ErrorResponse(
    val status: Int,       // HTTP 상태 코드
    val errorCode: String, // (선택) 에러 코드
    val message: String,    // 에러 메시지
    val violations: List<Violation>? = null
) {
    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(errorCode.status, errorCode.code, errorCode.message)
        }
    }
}

data class Violation(
    val field: String,
    val message: String
)
