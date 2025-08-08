package com.eventbook.address

import com.eventbook.address.database.configureDatabases
import com.eventbook.address.di.configureFrameworks
import com.eventbook.address.logging.configureMonitoring
import com.eventbook.address.plugins.configureRouting
import com.eventbook.address.security.cors.configureHTTP
import com.eventbook.address.security.ratelimiter.configureAdministration
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
