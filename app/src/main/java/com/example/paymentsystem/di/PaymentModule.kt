package com.example.paymentsystem.di

import android.content.Context
import android.widget.Toast
import com.example.paymentsystem.R
import com.example.paymentsystem.data.repositoryImpl.PaymentRepositoryImpl
import com.example.paymentsystem.domain.repository.PaymentRepository
import com.example.paymentsystem.domain.retrofit.Services
import com.example.paymentsystem.domain.usesCases.PaymentUsesCases
import com.example.paymentsystem.ui.presentation.PaymentViewModel
import com.stripe.android.PaymentConfiguration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

val paymentModule = module {
    single { androidContext() }
    single { initPaymentConfiguration(get()) }
    single { HttpLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideServicesApi(get()) }
    single<PaymentRepository> {
        PaymentRepositoryImpl(get())
    }
    single {
        PaymentUsesCases(get())
    }
    viewModel {
        PaymentViewModel(get())
    }
}

fun initPaymentConfiguration(androidContext: Context) {
    Toast.makeText(androidContext, "init payment", Toast.LENGTH_LONG).show()
    return PaymentConfiguration.init(
        androidContext,
        publishableKey = R.string.public_key.toString()
    )
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl("https://api.stripe.com/v1/")
        .client(okHttpClient).addConverterFactory(ScalarsConverterFactory.create())
        .build()

}

fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient().newBuilder()
        .addInterceptor(httpLoggingInterceptor).build()

}

fun provideServicesApi(retrofit: Retrofit): Services = retrofit.create(Services::class.java)
