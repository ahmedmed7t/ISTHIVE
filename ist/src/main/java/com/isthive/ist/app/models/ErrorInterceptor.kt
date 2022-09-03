package com.isthive.ist.app.models

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

internal class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        try {
            val response = chain.proceed(request)
            val bodyString = response.body!!.string()

            return response.newBuilder()
                .body(bodyString.toResponseBody(response.body?.contentType()))
                .build()
        } catch (e: Exception) {
          return Response.Builder()
              .request(request)
              .protocol(Protocol.HTTP_1_1)
              .code(405)
              .message("Please check your network connection")
              .body("{${e}}".toResponseBody(null)).build()
        }
    }
}