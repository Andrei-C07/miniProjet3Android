package ca.qc.cgodin.miniprojet3.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.cgodin.miniprojet3.network.RetrofitInstance
import ca.qc.cgodin.miniprojet3.network.Succursale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SuccursalesUiState {
    object Idle : SuccursalesUiState()
    object Loading : SuccursalesUiState()
    data class Success(val list: List<Succursale>) : SuccursalesUiState()
    data class Error(val message: String) : SuccursalesUiState()
}

class ListeSuccursalesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SuccursalesUiState>(SuccursalesUiState.Idle)
    val uiState: StateFlow<SuccursalesUiState> = _uiState

    fun fetchSuccursales(autKey: String) {
        viewModelScope.launch {
            _uiState.value = SuccursalesUiState.Loading
            try {
                val response = RetrofitInstance.api.listeSuccursales(autKey)
                if (response.isSuccessful && response.body()?.statut == "OK") {
                    val list = response.body()?.succursales ?: emptyList()
                    _uiState.value = SuccursalesUiState.Success(list)
                } else {
                    _uiState.value = SuccursalesUiState.Error("Erreur de chargement (${response.code()})")
                }
            } catch (e: Exception) {
                _uiState.value = SuccursalesUiState.Error("Erreur réseau : ${e.message}")
            }
        }
    }
}
