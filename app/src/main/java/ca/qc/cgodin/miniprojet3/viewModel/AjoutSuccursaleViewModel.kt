package ca.qc.cgodin.miniprojet3.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.cgodin.miniprojet3.repository.SuccursaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class AjoutUiState {
    object Idle : AjoutUiState()
    object Loading : AjoutUiState()
    data class Success(val message: String) : AjoutUiState()
    data class Error(val message: String) : AjoutUiState()
}

class AjoutSuccursaleViewModel(private val repository: SuccursaleRepository) : ViewModel() {

    private val _ajoutState = MutableStateFlow<AjoutUiState>(AjoutUiState.Idle)
    val ajoutState: StateFlow<AjoutUiState> = _ajoutState

    fun ajouter(aut: String, ville: String, budget: Int) {
        viewModelScope.launch {
            _ajoutState.value = AjoutUiState.Loading

            val result = repository.ajouterSuccursale(aut, ville, budget)
            when (result) {
                "OKI" -> _ajoutState.value = AjoutUiState.Success("Succursale ajouter avec succes")
                else -> _ajoutState.value = AjoutUiState.Error("Erreur: $result")
            }
        }
    }
}