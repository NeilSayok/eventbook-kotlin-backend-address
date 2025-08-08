package com.eventbook.logging

import com.eventbook.BuildConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object Log {
    private val logPrinter: Logger = LoggerFactory.getLogger(BuildConfig.APPLICATION_NAME)

    fun i(tag: String, message: String) {
        logPrinter.info("$tag : $message")
    }

    fun e(tag: String, message: String) {
        logPrinter.error("$tag : $message")
    }

    fun d(tag: String, message: String) {
        logPrinter.debug("$tag : $message")
    }

    fun w(tag: String, message: String) {
        logPrinter.warn("$tag : $message")
    }

    fun v(tag: String, message: String) {
        logPrinter.trace("$tag : $message")
    }
}
