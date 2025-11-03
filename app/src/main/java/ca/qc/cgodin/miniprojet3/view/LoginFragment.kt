package ca.qc.cgodin.miniprojet3.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ca.qc.cgodin.miniprojet3.databinding.FragmentLoginBinding
import ca.qc.cgodin.miniprojet3.model.AuthUtils
import ca.qc.cgodin.miniprojet3.model.LoginValidator
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ca.qc.cgodin.miniprojet3.repository.AuthRepository
import ca.qc.cgodin.miniprojet3.network.RetrofitInstance

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var authKeyForNavigation: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(AuthRepository(RetrofitInstance.api))
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
                    authKeyForNavigation = AuthUtils.makeAuthKey(identifiant, motDePasse)
                    //Log.d("Login", authKeyForNavigation)
                    viewModel.login(identifiant, motDePasse)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.loginState.collectLatest { state ->
                when (state) {
                    is LoginUiState.Idle -> Unit
                    is LoginUiState.Loading -> {
                        Toast.makeText(requireContext(), "Connexion en cours...", Toast.LENGTH_SHORT).show()
                    }
                    is LoginUiState.Success -> {
                        Toast.makeText(requireContext(),
                            "Bienvenue ${state.prenom} ${state.nom}",
                            Toast.LENGTH_LONG
                        ).show()
                        // TODO: navigate to ListeSuccursalesFragment
                        authKeyForNavigation?.let { key ->
                            val action = LoginFragmentDirections
                                .actionLoginFragmentToHomeFragment(key)
                            findNavController().navigate(action)
                        } ?: run {
                            Log.e("NAV", "authKeyForNavigation was null, cannot navigate with key.")
                        }

                    }
                    is LoginUiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}