package com.eventbook.address.utils

import com.eventbook.address.common.base.BaseResponse
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun String.extractPlaceholders(): List<String> {
    val regex = """\{\{(.*?)\}\}""".toRegex()
    return regex.findAll(this).map { it.groupValues[1] }.toList()
}


suspend fun RoutingCall.requestHandler(
    customExceptionHandler: (suspend (Exception) -> Unit)? = null, body: (suspend () -> Unit)
) {
    try {
        body()
    } catch (e: Exception) {
        if (customExceptionHandler != null) customExceptionHandler(e)
        else {
            if (e is MissingParamException) {
                e.field.forEach { f ->
                    this.respond(
                        HttpStatusCode.BadRequest,
                        BaseResponse.ExceptionResponse(
                            HttpStatusCode.BadRequest,
                            "Parameter : \"$f\" is missing", status = HttpStatusCode.BadRequest
                        )
                    )
                }
            } else {
                this@requestHandler.respond(
                    HttpStatusCode.InternalServerError,
                    BaseResponse.ExceptionResponse(
                        HttpStatusCode.InternalServerError,
                        e.message, status = HttpStatusCode.InternalServerError
                    )
                )
            }
        }
    }
}

public open class MissingParamException(message: String, vararg val field: String) :
    BadRequestException(message = message)