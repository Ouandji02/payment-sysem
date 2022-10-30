package com.example.paymentsystem.domain.usesCases

import android.content.Context
import com.example.paymentsystem.domain.model.PaymentIntent
import com.example.paymentsystem.domain.repository.PaymentRepository

class PaymentUsesCases(private val repository: PaymentRepository) {
    suspend fun createPaymentIntent(paymentIntent: PaymentIntent,context: Context) =
        repository.createPaymentIntent(paymentIntent,context)
}