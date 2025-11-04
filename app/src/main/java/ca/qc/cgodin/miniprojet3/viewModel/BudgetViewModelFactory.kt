package ca.qc.cgodin.miniprojet3.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.qc.cgodin.miniprojet3.repository.SuccursaleRepository

class BudgetViewModelFactory(private val repository: SuccursaleRepository)
    : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
           @Suppress("UNCHECKED_CAST")
           return BudgetViewModel(repository) as T
       }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}