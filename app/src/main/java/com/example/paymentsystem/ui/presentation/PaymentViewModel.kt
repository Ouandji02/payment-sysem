package com.example.paymentsystem.ui.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paymentsystem.R
import com.example.paymentsystem.domain.model.PaymentIntent
import com.example.paymentsystem.domain.model.Response
import com.example.paymentsystem.domain.usesCases.PaymentUsesCases
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.launch

class PaymentViewModel(val usesCases: PaymentUsesCases) : ViewModel() {
    var paymentResponse by mutableStateOf<Response<String>>(Response.Loading)
    fun createPaymentIntent(paymentIntent: PaymentIntent, context: Context) =
        viewModelScope.launch {
            usesCases.createPaymentIntent(paymentIntent, context).collect {
                paymentResponse = it
            }
        }

    fun presentPaymentSheet(client_secret: String, paymentSheet: PaymentSheet) {
        paymentSheet.presentWithPaymentIntent(
            client_secret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Ydol",
                customer = PaymentSheet.CustomerConfiguration(id="cus_Mi2zNfXwPFQS10", ephemeralKeySecret = "ephkey_1LycusBz57N7RKGau7QIWtA4"),
            )
        )
    }
}
