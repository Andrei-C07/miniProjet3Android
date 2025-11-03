package ca.qc.cgodin.miniprojet3.repository

import ca.qc.cgodin.miniprojet3.network.LoginResponse
import ca.qc.cgodin.miniprojet3.network.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val api: RetrofitService) {

    suspend fun authenticate(matricule: String, motDePasse: String): LoginResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.login(matricule, motDePasse)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}
