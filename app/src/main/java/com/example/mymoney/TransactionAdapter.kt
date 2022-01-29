package com.example.mymoney

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.transaction_layout.view.*

class TransactionAdapter(private  val transaction:ArrayList<Transaction>):
        RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(view: View):RecyclerView.ViewHolder(view){
        val label:TextView= view.txt_label
        val amount:TextView= view.txt_amount

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        //inflate transaction
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout,parent,false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction:Transaction = transaction[position]
        val context:Context =holder.amount.context

        if(transaction.amount >=0 ){
            holder.amount.text= "+ $%.2f".format(transaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.green))
        }else{
            holder.amount.text= "- $%.2f".format(Math.abs(transaction.amount))
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
        }
        holder.label.text = transaction.label
    }

    override fun getItemCount(): Int {
        return  transaction.size
    }
}