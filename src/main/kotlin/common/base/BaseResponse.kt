package com.eventbook.common.base

import com.fasterxml.jackson.annotation.JsonInclude
import io.ktor.http.*

sealed class BaseResponse<out T>(
    open val status: HttpStatusCode
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class SuccessResponse<T>(
        val data: T? = null,
        val message: String? = null,
        override val status: HttpStatusCode = HttpStatusCode.OK
    ) : BaseResponse<T>(status)

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class ExceptionResponse<T>(
        val exception: T? = null,
        val message: String? = null,
        override val status: HttpStatusCode = HttpStatusCode.ExpectationFailed
    ) : BaseResponse<T>(status)


}