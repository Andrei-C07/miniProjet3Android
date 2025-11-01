package ca.qc.cgodin.miniprojet3.model

object AuthUtils {
    fun makeAuthKey(identifiant: String, motDePasse: String): String {
        val digits = motDePasse.takeLast(5)
        return identifiant + digits
    }
}