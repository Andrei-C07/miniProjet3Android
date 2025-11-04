package ca.qc.cgodin.miniprojet3.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ca.qc.cgodin.miniprojet3.databinding.FragmentModifierSuccursaleBinding
import ca.qc.cgodin.miniprojet3.network.RetrofitInstance
import kotlinx.coroutines.launch
import kotlin.toString

class ModifierSuccursaleFragment : Fragment() {

    private lateinit var binding: FragmentModifierSuccursaleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModifierSuccursaleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments?.let { ModifierSuccursaleFragmentArgs.fromBundle(it) } ?: return
        val aut = args.aut
        val ville = args.ville
        val oldBudget = args.budget

        binding.tvVille.text = "Modifier la succursale de $ville"
        binding.etBudget.setText(oldBudget.toString())

        binding.btnModifier.setOnClickListener {
            val newBudget = binding.etBudget.text.toString().toIntOrNull()
            if (newBudget == null) {
                Toast.makeText(requireContext(), "Budget invalide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val deleteResponse = RetrofitInstance.api.retirerSuccursale(aut, ville)
                    if (deleteResponse.isSuccessful) {
                        RetrofitInstance.api.ajouterSuccursale(aut, ville, newBudget)
                        Toast.makeText(requireContext(), "Succursale mise à jour", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(requireContext(), "Erreur lors de la suppression", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Erreur réseau", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnAnnuler.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}