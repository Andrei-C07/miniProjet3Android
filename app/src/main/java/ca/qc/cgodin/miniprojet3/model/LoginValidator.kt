package ca.qc.cgodin.miniprojet3.model

object LoginValidator {
    private val identifiantRegex = Regex("^\\d{7}$")
    private val passwordRegex = Regex("^[A-Za-z]{1,6}\\d{5}$")

    fun validate(identifiant: String, password: String): LoginValidationState {
        val isIdentifiantValid = identifiant.matches(identifiantRegex)
        val isPasswordValid = password.matches(passwordRegex)
        return LoginValidationState(isIdentifiantValid, isPasswordValid)
    }
}