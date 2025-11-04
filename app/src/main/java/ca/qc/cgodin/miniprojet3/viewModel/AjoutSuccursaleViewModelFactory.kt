package ca.qc.cgodin.miniprojet3.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.qc.cgodin.miniprojet3.repository.SuccursaleRepository

class AjoutSuccursaleViewModelFactory(private val repository: SuccursaleRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AjoutSuccursaleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AjoutSuccursaleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}