package com.example.stripe_deneme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.stripe_deneme.Utils.PUBLISHABLE_KEY
import com.example.stripe_deneme.Utils.SECRET_KEY
import com.example.stripe_deneme.databinding.ActivityMain2Binding
import com.stripe.android.model.CardParams
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.ConfirmSetupIntentParams
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var paymentLauncher: PaymentLauncher
    lateinit var paymentSheet: PaymentSheet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        init()
    }
    fun init() {
        paymentLauncher =
            PaymentLauncher.create(
                this,
                // set publish key here
                "pk_test_51PCGvJK9zvRLQtty46wOaiFg0szrx9hrL59g77bTicgUltZlLI5KpMkmmgz7L8TM6YmsZGPRtu90KoQB2wVuTOp800uHvyI0F3"
            ) { paymentResult ->
                Toast.makeText(this, paymentResult.toString(), Toast.LENGTH_LONG).show()
                if (paymentResult is PaymentResult.Failed) {
                    paymentResult.throwable.printStackTrace()
                }
            }


        // Confirm the PaymentIntent with the card widget
        binding.payButton.setOnClickListener {
//            binding.cardFormWidget.cardParams?.let { params ->
//                doPaymentIntent(params)
            doPaymentSheet()
//            }
        }
    }

    private fun doSetupIntent(cardParams: CardParams) {
        val confirmParams = ConfirmSetupIntentParams.create(
            paymentMethodCreateParams = PaymentMethodCreateParams.createCard(cardParams),
            clientSecret = binding.etClientSecret.text.toString(),
        )
        paymentLauncher.confirm(confirmParams)
    }

    private fun doPaymentIntent(cardParams: CardParams) {
        val confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
            clientSecret = binding.etClientSecret.text.toString(),
            paymentMethodCreateParams = PaymentMethodCreateParams.createCard(cardParams),
        )
        paymentLauncher.confirm(confirmParams)
    }

    private fun doPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            binding.etClientSecret.text.toString(),
            PaymentSheet.Configuration(
                merchantDisplayName = "My merchant name",
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = true
            )
        )
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
            }

            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
                paymentSheetResult.error.printStackTrace()
            }

            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
            }
        }
    }
}