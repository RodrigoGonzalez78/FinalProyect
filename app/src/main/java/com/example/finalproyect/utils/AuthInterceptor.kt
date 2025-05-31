package com.example.finalproyect.utils

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferenceManager: PreferenceManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { preferenceManager.getAuthToken().first() }
        val request = chain.request().newBuilder()

        if (token.isNotEmpty()) {
            request.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}