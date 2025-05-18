package com.siri.network.utils

import android.content.Context
import com.siri.network.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class NetworkExceptionHandler  @Inject constructor(
    @ApplicationContext private val context: Context
) : ExceptionHandler {

    override fun getReadableMessage(exception: Exception): String {
        return when (exception) {
            is IOException -> "Network connection error. Please check your internet."
            else -> "Unknown error occurred. Please try again."
        }
    }
}