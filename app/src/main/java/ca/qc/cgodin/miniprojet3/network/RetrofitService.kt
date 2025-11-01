package ca.qc.cgodin.miniprojet3.network

import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Field

interface RetrofitService {
    @FormUrlEncoded
    @POST("authentification")
    suspend fun login(
        @Field("matricule") matricule: String,
        @Field("motdepasse") motDePasse: String
    ):Response<String>
}