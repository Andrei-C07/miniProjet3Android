package ca.qc.cgodin.miniprojet3.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ca.qc.cgodin.miniprojet3.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConnecter.setOnClickListener {
            val matricule = binding.etIdentifiant.text.toString().trim()
            val motDePasse = binding.etMdp.text.toString().trim()

            // if matricule regex ou mot de passe regex invalid, toast message, two separate if statements
            // if auth details is wrong, toast message
            val autKey = makeAuthKey(matricule, motDePasse)
            //ViewModel.login(matricule, motDePasse)
        }
    }

    private fun makeAuthKey(matricule: String, motDePasse: String): String {
        val digits = motDePasse.takeLast(5)
        return matricule + digits
    }

}