package ca.qc.cgodin.miniprojet3.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ca.qc.cgodin.miniprojet3.databinding.FragmentLoginBinding
import ca.qc.cgodin.miniprojet3.model.AuthUtils
import ca.qc.cgodin.miniprojet3.model.LoginValidator
import ca.qc.cgodin.miniprojet3.repository.AuthRepository
import ca.qc.cgodin.miniprojet3.network.RetrofitInstance
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(AuthRepository(RetrofitInstance.api))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConnecter.setOnClickListener {
            val identifiant = binding.etIdentifiant.text.toString().trim()
            val motDePasse = binding.etMdp.text.toString().trim()

            val validation = LoginValidator.validate(identifiant, motDePasse)

            when {
                !validation.isIdentifiantValid -> {
                    Toast.makeText(requireContext(), "Identifiant invalide (7 chiffres)", Toast.LENGTH_SHORT).show()
                }
                !validation.isPasswordValid -> {
                    Toast.makeText(requireContext(), "Mot de passe invalide (1–6 lettres + 5 chiffres)", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Make auth key
                    val authKey = AuthUtils.makeAuthKey(identifiant, motDePasse)

                    // Start login request
                    viewModel.login(identifiant, motDePasse)

                    // Observe login state
                    lifecycleScope.launch {
                        viewModel.loginState.collectLatest { state ->
                            when (state) {
                                is LoginUiState.Success -> {
                                    // Build full user name safely
                                    val prenom = state.prenom ?: "Utilisateur"
                                    val nom = state.nom ?: ""
                                    val utilisateur = "$prenom $nom"

                                    val action = LoginFragmentDirections
                                        .actionLoginFragmentToHomeFragment(
                                            key = authKey,
                                            utilisateur = utilisateur
                                        )
                                    findNavController().navigate(action)

                                    // Optional: show welcome toast
                                    Toast.makeText(requireContext(), "Bienvenue $utilisateur", Toast.LENGTH_LONG).show()
                                }
                                is LoginUiState.Error -> {
                                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                                }
                                is LoginUiState.Loading -> {
                                    Toast.makeText(requireContext(), "Connexion en cours...", Toast.LENGTH_SHORT).show()
                                }
                                is LoginUiState.Idle -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}
