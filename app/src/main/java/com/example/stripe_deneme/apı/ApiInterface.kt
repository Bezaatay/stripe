package com.example.stripe_deneme.apı

import com.example.stripe_deneme.Utils.SECRET_KEY
import com.example.stripe_deneme.model.CustomerModel
import com.example.stripe_deneme.model.PaymentIntentModel
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @Headers("Authorization: Bearer $SECRET_KEY")
    @POST("/v1/customers")
    suspend fun getCostumer(): Response<CustomerModel>


    @Headers(
        "Authorization: Bearer $SECRET_KEY",
        "Stripe-Version: 2024-04-10"
    )
    @POST("/v1/ephemeral_keys")
    suspend fun getEphemeralKey(@Query(
        "customer") customer: String
    ): Response<CustomerModel>


    @Headers("Authorization: Bearer $SECRET_KEY")
    @POST("/v1/payment_intents")
    suspend fun getPaymentIntent(
        @Query("customer") customer: String,
        @Query("amount") amount: String ="9999" ,
        @Query("currency") currency: String = "usd",
    ): Response<PaymentIntentModel>
}