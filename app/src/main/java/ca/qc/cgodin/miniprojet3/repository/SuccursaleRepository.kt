package ca.qc.cgodin.miniprojet3.repository

import ca.qc.cgodin.miniprojet3.network.AjoutResponse
import ca.qc.cgodin.miniprojet3.network.BudgetResponse
import ca.qc.cgodin.miniprojet3.network.RetrofitService

class SuccursaleRepository(private val api: RetrofitService) {

    suspend fun getBudget(aut: String, ville: String): BudgetResponse? {
        return try {
            val response = api.getBudget(aut, ville)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception){
            null
        }
    }

    suspend fun ajouterSuccursale(aut: String, ville: String, budget: Int): String? {
        return try {
            val response = api.ajouterSuccursale(aut, ville, budget)
            if (response.isSuccessful){
                response.body()?.statut ?: "ERREUR"
            } else {
                "ERREUR_HTTP${response.code()}"
            }
        } catch (e: Exception){
            "EXCEPTION_${e.message}"
        }

    }
}