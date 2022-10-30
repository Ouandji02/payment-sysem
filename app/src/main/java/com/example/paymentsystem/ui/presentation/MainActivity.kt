package com.example.paymentsystem.ui.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.paymentsystem.domain.model.PaymentIntent
import com.example.paymentsystem.domain.model.Response
import com.example.paymentsystem.ui.theme.PaymentSystemTheme
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONObject
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {
    lateinit var paymentSheet: PaymentSheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentSheet = PaymentSheet(this) { paymentSheetResult ->
            when (paymentSheetResult) {
                is PaymentSheetResult.Canceled -> Toast.makeText(
                    this,
                    "Canceled",
                    Toast.LENGTH_LONG
                )
                    .show()
                is PaymentSheetResult.Completed -> Toast.makeText(
                    this,
                    "Completed",
                    Toast.LENGTH_LONG
                )
                    .show()
                is PaymentSheetResult.Failed -> Toast.makeText(
                    this,
                    paymentSheetResult.error.message,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
        setContent {
            PaymentSystemTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    paymentScreen(paymentSheet = paymentSheet)
                }
            }
        }

    }

}

@Composable
fun paymentScreen(
    paymentViewModel: PaymentViewModel = getViewModel<PaymentViewModel>(),
    paymentSheet: PaymentSheet
) {
    val context = LocalContext.current
    var loading by remember {
        mutableStateOf(false)
    }
    var totalPrice by remember {
        mutableStateOf(0)
    }
    val titlesProduct = listOf("Name", "Poids", "Quantite", "Unitaire", "Total")
    val listProduct = listOf<Map<String, Any>>(
        mapOf(
            "name" to "banane", "poids" to "15kg", "unity" to 100, "quantite" to 2
        ), mapOf(
            "name" to "moto", "poids" to "500kg", "unity" to 350000, "quantite" to 1
        )
    )
    Column() {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Text(
                "Liste des produits",
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center
            )
        }
        LazyRow() {
            items(titlesProduct) {
                BoxComposable(name = it)
            }
        }
        var i by remember {
            mutableStateOf(0)
        }
        while (i < listProduct.size) {
            totalPrice += (listProduct[i].get("unity") as Int) * listProduct[i].get("quantite") as Int
            i++
        }
        LazyColumn() {
            items(listProduct) {
                item(product = it)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            BoxComposable(name = "Total")
            BoxComposable(name = totalPrice.toString())
        }
        val paymentIntent = PaymentIntent(
            currency = "eur",
            amount = (totalPrice.toString() + "00").toInt()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                loading = true
                paymentViewModel.createPaymentIntent(paymentIntent, context)
            }) {
                if (loading) CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(10.dp)
                )
                Text("Payer")
            }
        }
    }

    if (loading) when (val paymentResponse = paymentViewModel.paymentResponse) {
        is Response.Error -> {
            Toast.makeText(LocalContext.current, paymentResponse.message, Toast.LENGTH_LONG)
                .show()
            loading = false
        }
        is Response.Success -> {
            val data = paymentResponse.data
            val dataObject: JSONObject = JSONObject(data!!)
            val clientSecret = dataObject.get("client_secret") as String
            paymentViewModel.presentPaymentSheet(clientSecret, paymentSheet)
            Toast.makeText(LocalContext.current, "data", Toast.LENGTH_LONG)
                .show()
            loading = false
        }
    }
}

@Composable
fun item(product: Map<String, Any>) {
    val quantity = product.get("quantite") as Int
    val unity = product.get("unity") as Int
    val finalPrice = quantity * unity
    val name = product.get("name").toString()
    val weight = product.get("poids").toString()
    val valueTitlesProduct = listOf<String>(
        name, weight, quantity.toString(), unity.toString(), finalPrice.toString()
    )
    Surface(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        LazyRow() {
            items(valueTitlesProduct) {
                BoxComposable(name = it)
            }
        }
    }
}

@Composable
fun BoxComposable(width: Int = LocalConfiguration.current.screenWidthDp, name: String) {
    Surface(
        modifier = Modifier
            .width(width.dp / 5)
            .padding(vertical = 20.dp)
    ) {
        Text(name, style = MaterialTheme.typography.subtitle2, textAlign = TextAlign.Center)
    }
}