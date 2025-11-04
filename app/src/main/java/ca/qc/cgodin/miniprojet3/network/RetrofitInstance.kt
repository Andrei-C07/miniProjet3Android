package ca.qc.cgodin.miniprojet3.network

import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

object RetrofitInstance {
    private const val BASE_URL = "https://succursales.onrender.com/"

    val api: RetrofitService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }
}