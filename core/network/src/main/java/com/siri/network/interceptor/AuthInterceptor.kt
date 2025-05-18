package com.siri.network.interceptor

import com.siri.network.NetworkConstants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${API_KEY}")
            .build()
        return chain.proceed(request)
    }
}
