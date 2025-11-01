package ca.qc.cgodin.miniprojet3.repository

import ca.qc.cgodin.miniprojet3.network.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// retourne le string du server : "OK;Prenom;Nom" ou "PASOK;" ou "ERREUR;"
class AuthRepository(private val api: RetrofitService) {

    suspend fun authenticate(matricule: String, motDePasse: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.login(matricule, motDePasse)
                if (response.isSuccessful) {
                    response.body()?.trim() ?: ""
                } else {
                    "ERREUR_HTTP_${response.code()}"
                }
            } catch (e: Exception) {
                "EXCEPTION_${e.message}"
            }
        }
    }
}

