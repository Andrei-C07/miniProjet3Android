package ca.qc.cgodin.miniprojet3.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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

          binding.btnReinitialiser.setOnClickListener {

            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.api.reinitialiser(autKey)

                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            Toast.makeText(requireContext(), "Toutes les succursales ont été supprimées.", Toast.LENGTH_SHORT).show()
                            adapter.updateList(emptyList())
                            val list = response.body()?.succursales ?: emptyList()
                            binding.txtNbSucursales.text =
                                getString(R.string.txtNbSucursales, list.size)
                        } else {
                            Toast.makeText(requireContext(), "Erreur lors de la réinitialisation.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("Reinitialiser", "Code: ${response.code()} | Error: ${response.errorBody()?.string()}")
                        Toast.makeText(requireContext(), "Échec de la requête (${response.code()})", Toast.LENGTH_SHORT).show()
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        adapter = SuccursaleAdapter(
            onItemClick = { succursale ->
                val action = ListeSucursalesDirections
                    .actionHomeFragmentToBudgetFragment(
                        ville = succursale.Ville,
                        aut = autKey
                    )
                findNavController().navigate(action)
            },
            onDeleteClick = { succursale ->

                retirerSuccursale(autKey, succursale.Ville)
            },
            onModifyClick = { succursale ->
                findNavController().navigate(
                    R.id.action_homeFragment_to_modifierSuccursaleFragment,
                    bundleOf(
                        "aut" to autKey,
                        "ville" to succursale.Ville,
                        "budget" to succursale.Budget
                    )
                )
            }
        )

        binding.fabAdd.setOnClickListener {
            val action = ListeSucursalesDirections
                .actionHomeFragmentToAjoutSuccursaleFragment(autKey)
            findNavController().navigate(action)
        }
        binding.rvSucursales.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSucursales.adapter = adapter
        binding.rvSucursales.visibility = View.VISIBLE

        viewModel.viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.listeSuccursales(autKey)
                if (response.isSuccessful) {
                    val list = response.body()?.succursales ?: emptyList()
                    binding.txtNbSucursales.text = if (list.isEmpty()) {
                        "Aucune succursale enregistrée"
                    } else {
                        getString(R.string.txtNbSucursales, list.size)
                    }
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
                        binding.txtNbSucursales.text = if (list.isEmpty()) {
                            "Aucune succursale enregistrée"
                        } else {
                            getString(R.string.txtNbSucursales, list.size)
                        }
                        binding.rvSucursales.visibility = View.VISIBLE
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

    private fun retirerSuccursale(autKey: String, ville: String) {

        val updatedList = adapter.items.filter { it.Ville != ville }
        adapter.updateList(updatedList)
        binding.txtNbSucursales.text =
            if (updatedList.isEmpty()) "Aucune succursale enregistrée"
            else getString(R.string.txtNbSucursales, updatedList.size)

        viewModel.viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.retirerSuccursale(autKey, ville)
                if (!(response.isSuccessful && response.body()?.statut == "OK")) {
                    // Revert if failed
                    val listResponse = RetrofitInstance.api.listeSuccursales(autKey)
                    val originalList = listResponse.body()?.succursales ?: emptyList()
                    adapter.updateList(originalList)
                    binding.txtNbSucursales.text =
                        getString(R.string.txtNbSucursales, originalList.size)
                }
            } catch (e: Exception) {
                val listResponse = RetrofitInstance.api.listeSuccursales(autKey)
                val originalList = listResponse.body()?.succursales ?: emptyList()
                adapter.updateList(originalList)
                binding.txtNbSucursales.text =
                    getString(R.string.txtNbSucursales, originalList.size)
            }
        }
    }

}
