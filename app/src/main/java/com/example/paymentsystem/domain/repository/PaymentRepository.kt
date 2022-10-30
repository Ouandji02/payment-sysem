package com.example.paymentsystem.domain.repository

import android.content.Context
import com.example.paymentsystem.domain.model.PaymentIntent
import com.example.paymentsystem.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    suspend fun createPaymentIntent(paymentIntent: PaymentIntent, context: Context): Flow<Response<String>>
}