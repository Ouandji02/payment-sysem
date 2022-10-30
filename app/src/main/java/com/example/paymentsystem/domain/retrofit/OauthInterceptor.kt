package com.example.paymentsystem.domain.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class OauthInterceptor(private val tokenType: String, private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "$tokenType $token").build()
        return chain.proceed(request)
    }
}