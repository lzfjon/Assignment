package com.learning.assignmentocbc.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learning.assignmentocbc.R

class TransactionHistoryAdapter(

    var transactionHistoryDateList : ArrayList<String>,
    var transactionHistoryMap : MutableMap<String,ArrayList<ArrayList<String>>>?
//    var transactionDetailsAdapter : Adapter,

) : RecyclerView.Adapter<TransactionHistoryAdapter.DashboardViewHolder>(){

    class DashboardViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var transactionHistoryDate : TextView = itemView.findViewById(R.id.transactionDate)
        var cardRecyclerView : RecyclerView = itemView.findViewById(R.id.cardRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view : View = LayoutInflater.from(parent!!.context).inflate(R.layout.transaction_card,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionHistoryDateList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.transactionHistoryDate.text = transactionHistoryDateList[position]
        holder.cardRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = transactionHistoryMap?.get(transactionHistoryDateList[position])?.let {
                TransactionDetailsAdapter(
                    it
                )
            }
        }
    }


}