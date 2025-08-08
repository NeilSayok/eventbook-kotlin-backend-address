package com.eventbook.address.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*


val KtorClient by lazy {
    HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }

        engine {
            requestTimeout = 30 * 1000  // Set request timeout
        }

    }
}