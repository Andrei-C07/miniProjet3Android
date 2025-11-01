package ca.qc.cgodin.miniprojet3.model

data class LoginValidationState(
    val isIdentifiantValid: Boolean = false,
    val isPasswordValid: Boolean = false
)
