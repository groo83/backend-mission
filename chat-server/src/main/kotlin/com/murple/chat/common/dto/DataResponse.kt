package com.murple.chat.common.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DataResponse<T> @JsonCreator constructor(
    @JsonProperty("data") val data: T?
) : BaseResDto() {
    companion object {
        fun <T> create(data: T): DataResponse<T> = DataResponse(data)
    }
}