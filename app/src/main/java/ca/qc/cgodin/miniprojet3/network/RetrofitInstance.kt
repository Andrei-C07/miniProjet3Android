package ca.qc.cgodin.miniprojet3.network

import com.google.firebase.appdistribution.gradle.ApiService
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.jvm.java

object RetrofitInstance {
    private const val BASE_URL = "https://succursales.onrender.com/"

    val api: RetrofitService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }
}