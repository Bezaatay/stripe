package com.example.stripe_deneme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.stripe_deneme.Utils.PUBLISHABLE_KEY
import com.example.stripe_deneme.apı.ApiUtilities
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var paymentSheet: PaymentSheet
    lateinit var customerId: String
    lateinit var ephemeralKey: String
    lateinit var clientSecret: String
    lateinit var prizeTxt : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PaymentConfiguration.init(this, PUBLISHABLE_KEY)

        getCustomerId()

        val btn = findViewById<Button>(R.id.button)

        btn.setOnClickListener {
            paymentFlow()
        }

        paymentSheet = PaymentSheet(this@MainActivity, this::onPaymentSheetResult)
    }

    private fun paymentFlow() {
       paymentSheet.presentWithPaymentIntent(
           clientSecret,
           PaymentSheet.Configuration(
               "Beyza Atay",
               PaymentSheet.CustomerConfiguration(
                   customerId,ephemeralKey
               )
           )
       )
    }
    private var apiInterface = ApiUtilities.getApiInterface()
    private fun getCustomerId() {
        lifecycleScope.launch(Dispatchers.IO) {

            val res = apiInterface.getCostumer()
            withContext(Dispatchers.Main) {

                if (res.isSuccessful && res.body() != null) {
                    customerId = res.body()!!.id
                    Log.e("costumerid", res.body()!!.id)
                    getEphemeralKey(customerId)
                }
            }
        }
    }

    private fun getEphemeralKey(customerId: String) {
        lifecycleScope.launch(Dispatchers.IO) {

            val res = apiInterface.getEphemeralKey(customerId)

            withContext(Dispatchers.Main) {

                if (res.isSuccessful && res.body() != null) {
                    ephemeralKey=res.body()!!.id
                    getPaymentIntent(customerId, ephemeralKey)
                    Log.e("ephemeralkey", res.body()!!.id)
                }
            }
        }
    }

    private fun getPaymentIntent(customerId: String, ephemeralKey: String) {

        lifecycleScope.launch(Dispatchers.IO) {

            val res = apiInterface.getPaymentIntent(customerId)

            withContext(Dispatchers.Main) {

                if (res.isSuccessful && res.body() != null) {
                    clientSecret = res.body()!!.id
                    Log.e("clientSecret", res.body()!!.id)
                    Toast.makeText(this@MainActivity,"proceed for payment",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
            }
            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
            }
        }
    }
}