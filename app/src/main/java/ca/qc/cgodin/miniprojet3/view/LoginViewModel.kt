package ca.qc.cgodin.miniprojet3.view

import android.util.Log
import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.cgodin.miniprojet3.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val prenom: String, val nom: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun login(matricule: String, motDePasse: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            try {
                val result = repository.authenticate(matricule, motDePasse)

                when {
                    result.startsWith("OK;") -> {
                        val parts = result.split(";")
                        val prenom = parts.getOrNull(1) ?: ""
                        val nom = parts.getOrNull(2) ?: ""
                        _loginState.value = LoginUiState.Success(prenom, nom)
                    }

                    result.startsWith("PASOK;") ->
                        _loginState.value = LoginUiState.Error("Utilisateur inconnu")

                    result.startsWith("ERREUR") ->
                        _loginState.value = LoginUiState.Error("Erreur du serveur")

                    result.startsWith("EXCEPTION") ->
                        _loginState.value = LoginUiState.Error("Erreur réseau")

                    else ->
                        //_loginState.value = LoginUiState.Error("Réponse inattendue du serveur: $result")
                        Log.i("Login",result)
                }

            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error("Erreur réseau : ${e.message}")
            }
        }
    }
}
