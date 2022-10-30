package com.example.paymentsystem.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaymentIntent(
    @SerializedName("currency")
    @Expose
    val currency: String = "eur",
    @SerializedName("amount")
    @Expose
    val amount: Int = 100000
)
