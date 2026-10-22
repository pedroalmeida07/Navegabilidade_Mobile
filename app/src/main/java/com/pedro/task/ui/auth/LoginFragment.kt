package com.pedro.task.ui.auth

import android.os.Bundle
import android.util.Log.e
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.pedro.task.R
import com.pedro.task.databinding.FragmentLoginBinding
import com.pedro.task.util.showBottomSheet


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        initListener()
    }

    private fun initListener(){
        binding.btnLogin.setOnClickListener {
            validateData()
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_registerFragment2)
        }

        binding.btnRecover.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_recoverAccountFragment3)
        }
    }

    private fun validateData(){
        val email = binding.campoEmail.text.toString().trim()
        val senha = binding.campoSenha.text.toString().trim()
        if (email.isNotBlank()){
            if (senha.isNotBlank()){
                binding.progressBar.isVisible = true
                loginUser(email,senha)
            } else{
                showBottomSheet(message = getString(R.string.password_empty))
            }
        } else{
            showBottomSheet(message = getString(R.string.email_empty))
        }
    }

    private fun loginUser(email:String, password: String){
        try {
           auth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener { task ->
                   if (task.isSuccessful){
                       findNavController().navigate(R.id.action_global_homeFragment)
                   }else{
                       binding.progressBar.isVisible = false
                       Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                   }
               }
        }catch (e: Exception){
            Toast.makeText(requireContext(),e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}