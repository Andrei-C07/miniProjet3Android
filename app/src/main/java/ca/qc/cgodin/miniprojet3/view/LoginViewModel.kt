package ca.qc.cgodin.miniprojet3.view

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
            val result = repository.authenticate(matricule, motDePasse)

            if (result == null) {
                _loginState.value = LoginUiState.Error("Erreur réseau")
                return@launch
            }

            when (result.statut) {
                "OK" -> {
                    val prenom = result.student?.Prenom ?: ""
                    val nom = result.student?.Nom ?: ""
                    _loginState.value = LoginUiState.Success(prenom, nom)
                }
                "PASOK" -> {
                    _loginState.value = LoginUiState.Error("Utilisateur inconnu")
                }
                else -> {
                    _loginState.value = LoginUiState.Error("Erreur inattendue")
                }
            }
        }
    }
}
