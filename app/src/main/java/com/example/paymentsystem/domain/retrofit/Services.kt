package com.example.paymentsystem.domain.retrofit

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST


interface Services {
    @FormUrlEncoded
    @POST("payment_intents")
    @Headers(
        "Accept:application/json", "Content-Type:application/x-www-form-urlencoded",
        "Authorization: Bearer sk_test_51K3P3dBz57N7RKGacIo7sghVPlZxUvTxTUzk5I1TXiziP3OImQlicCo5UWXIpbgajT158K9OCwF9B1al1CsXpEly00Bv0yzcrQ",
    )
    fun createPaymentIntent(
        @Field("amount") amount: Int,
        @Field("currency") currency: String
    ): Call<String>
}