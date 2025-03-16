package com.murple.chat.common.exception

import com.murple.chat.common.code.ErrorCode
import com.murple.chat.common.dto.ErrorResponse
import com.murple.chat.common.dto.Violation
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.io.IOException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(e.errorCode.status)
            .body(ErrorResponse.of(e.errorCode))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidationException(e: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val violations = extractViolations(e)

        val errorResponse = ErrorResponse(
            status = ErrorCode.INVALID_PARAMETER.status,
            errorCode = ErrorCode.INVALID_PARAMETER.code,
            message = ErrorCode.INVALID_PARAMETER.message,
            violations = violations
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errorResponse)
    }

    @ExceptionHandler(IOException::class)
    fun handleIOException(ex: IOException?): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            ErrorCode.IO_ERROR.status,
            ErrorCode.IO_ERROR.code,
            ErrorCode.IO_ERROR.message
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse)
    }

    private inline fun <reified T : Throwable> extractViolations(e: T): List<Violation> {
        return when (e) {
            is ConstraintViolationException -> e.constraintViolations.map {
                Violation(
                    field = it.propertyPath.toString(),
                    message = it.message
                )
            }
            else -> emptyList()
        }
    }
}