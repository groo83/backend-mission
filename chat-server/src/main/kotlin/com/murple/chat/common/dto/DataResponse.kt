package com.murple.chat.common.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DataResponse<T> @JsonCreator constructor(
    @JsonProperty("data") val data: T?,
    val responseCode: Int = 200,
    val responseMessage: String = "Success"
) : BaseResDto(responseCode, responseMessage) {
    companion object {
        fun <T> create(data: T): DataResponse<T> = DataResponse(data)

        fun <T> create(data: T, message: String): DataResponse<T> = DataResponse(data, responseMessage = message)

        fun <T> empty(): DataResponse<T?> = DataResponse(null)

        fun <T> of(data: T, message: String): DataResponse<T> = DataResponse(data, responseMessage = message)
    }
}