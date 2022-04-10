package com.example.bogazhackatonproject.pages.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

enum class ApiStatus { DONE, LOADING, ERROR }

class LoginViewModel:ViewModel() {

    private val auth:FirebaseAuth= FirebaseAuth.getInstance()

    private val _isLogin=MutableLiveData<ApiStatus>()
    val isLogin:LiveData<ApiStatus>
        get() = _isLogin

    fun login(email:String,password:String){
        _isLogin.value=ApiStatus.LOADING
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                _isLogin.value=ApiStatus.DONE
            }else{
                _isLogin.value=ApiStatus.ERROR
            }
        }
    }

}