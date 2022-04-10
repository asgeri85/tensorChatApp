package com.example.bogazhackatonproject.pages.chat

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bogazhackatonproject.api.BogazApi
import com.example.bogazhackatonproject.api.BogazRetrofit
import com.example.bogazhackatonproject.model.MessageReponse
import com.example.bogazhackatonproject.model.ResponseModel
import com.example.bogazhackatonproject.pages.login.ApiStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class ChatViewModel : ViewModel() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref = database.getReference("messages")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storage:FirebaseStorage= FirebaseStorage.getInstance()

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _text=MutableLiveData<List<String>>()
    val text:LiveData<List<String>>
        get() = _text

    private val _state=MutableLiveData<ResponseModel>()
    val state:LiveData<ResponseModel>
        get() = _state

    private val liste= arrayListOf<String>()

    fun sendMessage(message: String) {
        _status.value = ApiStatus.LOADING
        val senderMail = auth.currentUser?.email
        val key = ref.push().key

        val hashMap = HashMap<String, String>()
        hashMap["gonderen"] = senderMail!!
        hashMap["alici"] = "rumeysa34@gmail.com"
        hashMap["text"] = message

        key?.let {
            ref.child(key).setValue(hashMap).addOnCompleteListener {
                if (it.isSuccessful) {
                    _status.value = ApiStatus.DONE
                } else {
                    _status.value = ApiStatus.ERROR
                }
            }
        }

    }

    fun loadMessage(){
        _status.value=ApiStatus.LOADING
        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message=snapshot.getValue(MessageReponse::class.java)
                message?.let {
                    liste.add(message.text!!)
                }
                _text.value=liste
                _status.value=ApiStatus.DONE
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun loadImage(uri:Uri){
        _status.value=ApiStatus.LOADING
        val uuid= UUID.randomUUID()
        val imageName="${uuid}.jpg"

        storage.reference.child("images").child(imageName).putFile(uri).addOnCompleteListener {
            if (it.isSuccessful){
                _status.value=ApiStatus.DONE
            }else{
                _status.value=ApiStatus.ERROR
            }
        }
    }

    fun getState(){
        viewModelScope.launch {
            try {
                _state.value=BogazRetrofit.retrofitService.getValue()
            }catch (e:Exception){

            }
        }
    }
}