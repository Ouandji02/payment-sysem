package com.example.paymentsystem.data.repositoryImpl

import android.content.Context
import android.widget.Toast
import com.example.paymentsystem.domain.model.PaymentIntent
import com.example.paymentsystem.domain.model.Response
import com.example.paymentsystem.domain.repository.PaymentRepository
import com.example.paymentsystem.domain.retrofit.Services
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback

class PaymentRepositoryImpl(private val services: Services) : PaymentRepository {

    private val serviceRetrofit: Services = services
    override suspend fun createPaymentIntent(
        paymentIntent: PaymentIntent,
        context: Context
    ) =
        callbackFlow {
            val result =
                serviceRetrofit.createPaymentIntent(paymentIntent.amount, paymentIntent.currency)
            result.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                    if (response.isSuccessful) trySend(Response.Success(response.body()))
                    else {
                        Toast.makeText(context,"is not successfull", Toast.LENGTH_LONG).show()
                        trySend(Response.Error(response.message()))
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(context,"failed ${t.message.toString()}", Toast.LENGTH_LONG).show()
                    trySend(Response.Error(message = t.message.toString()))
                }
            })

            awaitClose { result }
        }
}