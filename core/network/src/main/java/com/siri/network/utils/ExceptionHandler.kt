package com.siri.network.utils

interface ExceptionHandler {
    fun getReadableMessage(exception: Exception): String
}

