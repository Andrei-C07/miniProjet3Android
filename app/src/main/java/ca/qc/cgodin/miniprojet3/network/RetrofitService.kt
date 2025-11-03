package ca.qc.cgodin.miniprojet3.network

import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Field

data class Utilisateur(
    val Matricule: Int,
    val Nom: String,
    val Prenom: String,
    val token: String
)

data class LoginResponse(
    val statut: String,
    val student: Utilisateur?
)

interface RetrofitService {
    @FormUrlEncoded
    @POST("students/Connexion")
    suspend fun login(
        @Field("Mat") matricule: String,
        @Field("MDP") motDePasse: String
    ):Response<LoginResponse>
}