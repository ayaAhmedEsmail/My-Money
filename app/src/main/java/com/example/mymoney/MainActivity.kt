package com.example.mymoney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var transaction: ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transaction= arrayListOf(
                Transaction("Received budget",400.0),
                Transaction("closes",-1400.0),
                Transaction(" bonus",800.0),
                Transaction(" school",-4000.0),
                Transaction("books",-100.0)
        )
        transactionAdapter =TransactionAdapter(transaction)
        linearLayoutManager= LinearLayoutManager(this)

        rcy_budget.apply {
            adapter = transactionAdapter
            layoutManager =linearLayoutManager
        }

        updateDashboard()

    }



    private fun updateDashboard(){
        val totalAmount :Double = transaction.map {
            it.amount
        }.sum()
        val budgetAmount: Double = transaction.filter{
            it.amount>0
        }.map{it.amount}.sum()
        val expensive: Double = totalAmount-budgetAmount

        budget.text = "%.2f".format(totalAmount)
        balance.text = "%.2f".format(budgetAmount)
        expense.text = "%.2f".format(expensive)
    }
}