package ca.qc.cgodin.miniprojet3.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.qc.cgodin.miniprojet3.R
import ca.qc.cgodin.miniprojet3.databinding.FragmentAjoutSuccursaleBinding
import ca.qc.cgodin.miniprojet3.network.RetrofitInstance
import ca.qc.cgodin.miniprojet3.repository.SuccursaleRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AjoutSuccursaleFragment : Fragment() {

    private lateinit var binding: FragmentAjoutSuccursaleBinding
    private val args: AjoutSuccursaleFragmentArgs by navArgs()

    private val viewModel: AjoutSuccursaleViewModel by viewModels {
        AjoutSuccursaleViewModelFactory(SuccursaleRepository(RetrofitInstance.api))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAjoutSuccursaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val autKey = args.aut

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnAjouter.setOnClickListener {
            val ville = binding.etVille.text.toString().trim()
            val budgetText = binding.etBudget.text.toString().trim()

            if (ville.isEmpty() || budgetText.isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val budget = budgetText.toIntOrNull()
            if (budget == null) {
                Toast.makeText(requireContext(), "Budget invalide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.ajouter(autKey, ville, budget)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.ajoutState.collectLatest { state ->
                when (state) {
                    is AjoutUiState.Loading -> binding.tvStatus.text = "Ajout en cours..."
                    is AjoutUiState.Success -> {
                        binding.tvStatus.text = state.message
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    is AjoutUiState.Error -> {
                        binding.tvStatus.text = state.message
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }
}