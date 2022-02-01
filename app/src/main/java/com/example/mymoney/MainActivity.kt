package com.example.mymoney

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


class MainActivity : AppCompatActivity() {
    private lateinit var transaction: List<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var db:AppDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transaction= arrayListOf()


        transactionAdapter =TransactionAdapter(transaction)
        linearLayoutManager= LinearLayoutManager(this)
        db = Room.databaseBuilder(this,AppDB::class.java,"transition").build()

        rcy_budget.apply {
            adapter = transactionAdapter
            layoutManager =linearLayoutManager
        }


        fab_btn.setOnClickListener{
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }


    }



    private fun fetchAll(){
        GlobalScope.launch{
          //  db.transactionDao().insertAll(Transaction(0,"Ice cream",-10.0,""))
         transaction= db.transactionDao().getAll()

            runOnUiThread{
                updateDashboard()
                transactionAdapter.setData(transaction)

            }
        }
    }
    private fun updateDashboard(){
        val totalAmount :Double = transaction.map {
            it.amount
        }.sum().absoluteValue


        val budgetAmount: Double = transaction.filter{
            it.amount > 0
        }.map{
            it.amount
        }.sum()

        val expensive: Double = budgetAmount - totalAmount

        budget.text = "%.2f".format(totalAmount)
        balance.text = "%.2f".format(budgetAmount)
        expense.text = "%.2f".format(expensive)
    }
    override fun onResume() {
        super.onResume()
        fetchAll()
    }
}