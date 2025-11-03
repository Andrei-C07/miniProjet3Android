package ca.qc.cgodin.miniprojet3.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ca.qc.cgodin.miniprojet3.databinding.ListeSucursalesBinding
import ca.qc.cgodin.miniprojet3.model.AuthUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.navigation.fragment.navArgs

class ListeSucursales : Fragment() {

    private lateinit var binding: ListeSucursalesBinding
    private val viewModel: ListeSuccursalesViewModel by viewModels()
    private lateinit var adapter: SuccursaleAdapter

    private val args: ListeSucursalesArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListeSucursalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SuccursaleAdapter()
        binding.rvSucursales.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSucursales.adapter = adapter

        val autKey = args.key
        viewModel.fetchSuccursales(autKey)

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is SuccursalesUiState.Idle -> {}
                    is SuccursalesUiState.Loading ->
                        Toast.makeText(requireContext(), "Chargement...", Toast.LENGTH_SHORT).show()
                    is SuccursalesUiState.Success ->
                        adapter.updateList(state.list)
                    is SuccursalesUiState.Error ->
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
