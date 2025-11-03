package ca.qc.cgodin.miniprojet3.model

object AuthUtils {

    var utilisateur: String? = null
    fun makeAuthKey(identifiant: String, motDePasse: String): String {
        val digits = motDePasse.takeLast(5)
        return identifiant + digits
    }

    fun getUser(prenom: String, nom: String){
        utilisateur = "$prenom $nom"
    }
}