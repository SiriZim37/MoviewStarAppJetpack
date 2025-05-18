package com.siri.data.utils

interface CacheValidator {
    suspend fun isValid(): Boolean
}