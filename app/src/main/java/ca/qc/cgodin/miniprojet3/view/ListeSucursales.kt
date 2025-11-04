package ca.qc.cgodin.miniprojet3.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ca.qc.cgodin.miniprojet3.databinding.ListeSucursalesBinding
import kotlinx.coroutines.launch
import androidx.navigation.fragment.navArgs
import ca.qc.cgodin.miniprojet3.R
import ca.qc.cgodin.miniprojet3.network.RetrofitInstance

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

        val utilisateur = args.utilisateur ?: "Null"
        binding.tvBienvenue.text = getString(R.string.txtWhoLogged, utilisateur)

        val autKey = args.key

        adapter = SuccursaleAdapter(
            onItemClick = { succursale ->
                // Navigate to budget/details screen
                val action = ListeSucursalesDirections
                    .actionHomeFragmentToBudgetFragment(
                        ville = succursale.Ville,
                        aut = autKey
                    )
                findNavController().navigate(action)
            },
            onDeleteClick = { succursale ->
                // Delete succursale
                retirerSuccursale(autKey, succursale.Ville)
            }
        )

        binding.fabAdd.setOnClickListener {
            val action = ListeSucursalesDirections
                .actionHomeFragmentToAjoutSuccursaleFragment(autKey)
            findNavController().navigate(action)
        }
        binding.rvSucursales.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSucursales.adapter = adapter
        binding.rvSucursales.visibility = View.GONE

        viewModel.viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.listeSuccursales(autKey)
                if (response.isSuccessful && response.body()?.statut == "OK") {
                    val list = response.body()?.succursales ?: emptyList()
                    binding.txtNbSucursales.text =
                        getString(R.string.txtNbSucursales, list.size)
                    adapter.updateList(list)
                } else {
                    binding.txtNbSucursales.text = "Erreur de chargement"
                }
            } catch (e: Exception) {
                binding.txtNbSucursales.text = "Erreur réseau"
            }
        }

        binding.btnLister.setOnClickListener {
            binding.rvSucursales.visibility = View.VISIBLE
            binding.txtNbSucursales.text = getString(R.string.txtLoading)

            viewModel.viewModelScope.launch {
                try {
                    val response = RetrofitInstance.api.listeSuccursales(autKey)
                    if (response.isSuccessful) {
                        val list = response.body()?.succursales ?: emptyList()
                        binding.txtNbSucursales.text =
                            getString(R.string.txtNbSucursales, list.size)
                        adapter.updateList(list)
                    } else {
                        binding.txtNbSucursales.text = "Erreur de chargement"
                    }
                } catch (e: Exception) {
                    binding.txtNbSucursales.text = "Erreur réseau"
                }
            }
        }
    }

    private fun retirerSuccursale(autKey: String, ville:String){
        viewModel.viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.retirerSuccursale(autKey, ville)
                if (response.isSuccessful && response.body()?.statut == "OK") {
                    // Refresh list
                    val newList = response.body()?.succursales ?: emptyList()
                    adapter.updateList(newList)
                } else {
                    binding.txtNbSucursales.text = "Erreur lors de la suppression"
                }
            } catch (e: Exception) {
                binding.txtNbSucursales.text = "Erreur réseau"
            }
        }
    }
}
