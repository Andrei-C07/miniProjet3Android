package ca.qc.cgodin.miniprojet3.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.cgodin.miniprojet3.network.Succursale
import ca.qc.cgodin.miniprojet3.repository.SuccursaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BudgetViewModel(private val repo: SuccursaleRepository) : ViewModel() {

    private val _budgetState = MutableStateFlow<BudgetUiState>(BudgetUiState.Idle)
    val budgetState: StateFlow<BudgetUiState> = _budgetState

    fun fetchBudget(aut: String, ville: String) {
        viewModelScope.launch {
            _budgetState.value = BudgetUiState.Loading
            val result = repo.getBudget(aut, ville)
            if (result?.statut == "OK") {
                _budgetState.value = BudgetUiState.Success(result.succursale!!)
            } else {
                _budgetState.value = BudgetUiState.Error("Erreur de chargement du budget.")
            }
        }
    }
}

sealed class BudgetUiState {
    object Idle : BudgetUiState()
    object Loading : BudgetUiState()
    data class Success(val succursale: Succursale) : BudgetUiState()
    data class Error(val message: String) : BudgetUiState()
}