package com.example.bogazhackatonproject.pages.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bogazhackatonproject.MainActivity
import com.example.bogazhackatonproject.R
import com.example.bogazhackatonproject.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding:FragmentLoginBinding?=null
    private val binding get() = _binding!!
    private val loginViewModel by lazy { LoginViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding= FragmentLoginBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            signIn()
        }
        observeLiveData()
    }

    private fun observeLiveData(){
        with(binding){
            loginViewModel.isLogin.observe(viewLifecycleOwner){
                when(it){
                    ApiStatus.DONE->{
                        progressBarLogin.visibility=View.GONE
                        startActivity(Intent(requireContext(),MainActivity::class.java))
                        requireActivity().finish()
                        Toast.makeText(requireContext(),"Giriş başarılı",Toast.LENGTH_SHORT).show()
                    }
                    ApiStatus.LOADING->progressBarLogin.visibility=View.VISIBLE
                    else->{
                        progressBarLogin.visibility=View.GONE
                        Toast.makeText(requireContext(),"Bir hata oluştu",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun signIn(){
        val email=binding.editLoginMail.text.toString().trim()
        val password=binding.editLoginPass.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()){
            loginViewModel.login(email, password)
        }else{
            Toast.makeText(requireContext(),"Tüm alanları doldurunuz",Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}