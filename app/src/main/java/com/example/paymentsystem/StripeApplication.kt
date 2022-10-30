package com.example.paymentsystem

import android.app.Application
import com.example.paymentsystem.di.paymentModule
import com.stripe.android.PaymentConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class StripeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@StripeApplication)
            modules(listOf(paymentModule))
        }
        PaymentConfiguration.init(
            context = this,
            publishableKey = R.string.public_key.toString()
        )
    }
}