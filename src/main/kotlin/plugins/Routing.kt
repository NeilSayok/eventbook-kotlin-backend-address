package com.eventbook.plugins

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)  // Pretty print JSON
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)  // Ignore extra fields in JSON
            setSerializationInclusion(JsonInclude.Include.NON_NULL) // Ignore nulls
        }
    }

    install(ShutDownUrl.ApplicationCallPlugin) {
        shutDownUrl = "/application/shutdown"
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }

    routing {
        swaggerUI(
            path = "swagger",
            swaggerFile = "swagger/documentation.yaml",
        )

        get("/") {
            call.respondRedirect("/swagger")
        }
    }
}
