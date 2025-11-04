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

data class Succursale(
    val _id: String,
    val AccessMDP: Long,
    val Ville: String,
    val Budget: Int,
    val __v: Int
)

data class ListSuccResponse(
    val statut: String,
    val succursales: List<Succursale>?
)

data class BudgetResponse(
    val statut: String,
    val succursale: Succursale?
)

data class AjoutResponse(
    val statut: String,
)

interface RetrofitService {
    @FormUrlEncoded
    @POST("students/Connexion")
    suspend fun login(
        @Field("Mat") matricule: String,
        @Field("MDP") motDePasse: String
    ):Response<LoginResponse>

    @FormUrlEncoded
    @POST("succursales/Succursale-Liste")
    suspend fun listeSuccursales(
        @Field("Aut") aut: String
    ): Response<ListSuccResponse>

    @FormUrlEncoded
    @POST("succursales/Succursale-Budget")
    suspend fun getBudget(
        @Field("Aut") aut: String,
        @Field("Ville") ville: String
    ): Response<BudgetResponse>

    @FormUrlEncoded
    @POST("succursales/Succursale-Ajout")
    suspend fun ajouterSuccursale(
        @Field("Aut") aut: String,
        @Field("Ville") ville: String,
        @Field("Budget") budget: Int,
    ): Response<AjoutResponse>
}