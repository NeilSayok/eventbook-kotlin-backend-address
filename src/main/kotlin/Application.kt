package com.eventbook

import com.eventbook.database.configureDatabases
import com.eventbook.di.configureFrameworks
import com.eventbook.logging.configureMonitoring
import com.eventbook.plugins.configureRouting
import com.eventbook.security.cors.configureHTTP
import com.eventbook.security.ratelimiter.configureAdministration
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun main() {
    val host = BuildConfig.BASE_URL
    val port = System.getenv("PORT")?.toInt() ?: BuildConfig.PORT.toInt()

    embeddedServer(
        Netty, port = port,
        host = host,
        module = Application::module
    )
        .start(wait = true)

}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureAdministration()
    configureDatabases()
    configureFrameworks()
    configureRouting()
}
