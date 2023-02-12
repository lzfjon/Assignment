package com.learning.assignmentocbc.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learning.assignmentocbc.R

class TransactionDetailsAdapter(

    var transactionDetailsList : ArrayList<ArrayList<String>>,
//    var context : Context

) : RecyclerView.Adapter<TransactionDetailsAdapter.CardViewHolder>(){

    class CardViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var detailAccountNo : TextView = itemView.findViewById(R.id.thAccountNo)
        var detailAccountHolder : TextView = itemView.findViewById(R.id.thName)
        var detailAmount : TextView = itemView.findViewById(R.id.thAmount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view : View = LayoutInflater.from(parent!!.context).inflate(R.layout.transaction_card_detail,parent,false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionDetailsList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.detailAccountHolder.text = transactionDetailsList[position][1]
        holder.detailAccountNo.text = transactionDetailsList[position][2]
        holder.detailAmount.text = transactionDetailsList[position][3]

    }
}