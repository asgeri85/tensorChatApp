package com.example.bogazhackatonproject.pages.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bogazhackatonproject.databinding.CardMessageBinding

class MessageAdapter:RecyclerView.Adapter<MessageAdapter.MessageHolder>() {

    private val messages= arrayListOf<String>()

    class MessageHolder(private val cardMessageBinding: CardMessageBinding):RecyclerView.ViewHolder(cardMessageBinding.root){
        fun bind(message:String){
            cardMessageBinding.message=message
            cardMessageBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val layout=CardMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MessageHolder(layout)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val message=messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun updateList(newList:List<String>){
        messages.clear()
        messages.addAll(newList)
        notifyDataSetChanged()
    }
}