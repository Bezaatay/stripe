package com.example.stripe_deneme.model

data class CustomerModel(
    val id: String
)
data class PaymentIntentModel(
    val id: String,
    val client_secret : String
)
