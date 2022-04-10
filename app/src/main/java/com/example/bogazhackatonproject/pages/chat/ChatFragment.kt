package com.example.bogazhackatonproject.pages.chat

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bogazhackatonproject.databinding.FragmentChatBinding
import com.example.bogazhackatonproject.pages.login.ApiStatus

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { MessageAdapter() }
    private val chatViewModel by lazy { ChatViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatViewModel.loadMessage()
        observeLiveData()
        with(binding) {
            rvMessage.layoutManager = LinearLayoutManager(requireContext())
            rvMessage.setHasFixedSize(true)
            rvMessage.adapter = adapter
            imgBtnSendMessage.setOnClickListener {
                sendText()
            }
        }
    }

    private fun observeLiveData() {
        chatViewModel.text.observe(viewLifecycleOwner) {
            it?.let {
                adapter.updateList(it)
                binding.rvMessage.scrollToPosition(it.size - 1);
            }
        }
        chatViewModel.status.observe(viewLifecycleOwner){
            when(it){
                ApiStatus.DONE->binding.progressBarChat.visibility=View.GONE
                ApiStatus.LOADING->binding.progressBarChat.visibility=View.VISIBLE
                else->binding.progressBarChat.visibility=View.GONE
            }
        }
        chatViewModel.state.observe(viewLifecycleOwner){
            if (it.sonuc==1){
                binding.rvMessage.visibility=View.INVISIBLE
                binding.imageChatDanger.visibility=View.VISIBLE
                binding.textViewDanget.visibility=View.VISIBLE
            }else{
                binding.imageChatDanger.visibility=View.GONE
                binding.textViewDanget.visibility=View.GONE
                binding.rvMessage.visibility=View.VISIBLE
            }
        }
    }

    private fun sendText() {
        val text = binding.editMessage.text.toString().trim()
        chatViewModel.sendMessage(text)
        binding.editMessage.setText("")
        chatViewModel.getState()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}