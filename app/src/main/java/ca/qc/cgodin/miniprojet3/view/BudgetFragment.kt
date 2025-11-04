package ca.qc.cgodin.miniprojet3.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ca.qc.cgodin.miniprojet3.R
import ca.qc.cgodin.miniprojet3.databinding.FragmentBudgetBinding
import ca.qc.cgodin.miniprojet3.network.RetrofitInstance
import ca.qc.cgodin.miniprojet3.repository.SuccursaleRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BudgetFragment : Fragment() {

    private lateinit var binding: FragmentBudgetBinding
    private val viewModel: BudgetViewModel by viewModels {
        BudgetViewModelFactory(SuccursaleRepository(RetrofitInstance.api))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = BudgetFragmentArgs.fromBundle(requireArguments())
        val ville = args.ville
        val aut = args.aut

        viewModel.fetchBudget(aut, ville)
        Log.d("BudgetDebug", "Ville: $ville, Aut: $aut")
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        lifecycleScope.launch {
            viewModel.budgetState.collectLatest { state ->
                when (state) {
                    is BudgetUiState.Loading -> binding.tvBudget.text = "Chargement..."
                    is BudgetUiState.Success -> {
                        val s = state.succursale
                        binding.tvVille.text = s.Ville
                        binding.tvBudget.text = "Budget: ${s.Budget}$"
                    }
                    is BudgetUiState.Error -> binding.tvBudget.text = state.message
                    else -> Unit
                }
            }
        }
    }
}
