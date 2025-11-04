package ca.qc.cgodin.miniprojet3.repository

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
}