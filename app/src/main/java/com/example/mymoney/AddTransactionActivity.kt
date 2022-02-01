package com.example.mymoney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)



        btn_transaction.setOnClickListener {
            val label:String= label_Input.text.toString()
            val description = dec_Input.text.toString()
            val amount:Double? = amount_Input.text.toString().toDoubleOrNull()

            if(label.isEmpty())
                label_layout.error="Please add valid label"

            if (amount == null)
                amount_layout.error = "please add valid amount "
            else{
                val transaction = Transaction(0,label,amount,description)
                insert(transaction)
            }
        }

        btn_back.setOnClickListener{
            finish()
        }
    }

    private fun insert(transaction: Transaction){
        val  db:AppDB = Room.databaseBuilder(this,AppDB::class.java,"transition").build()

       GlobalScope.launch{
            db.transactionDao().insertAll(transaction)
           finish()
        }
    }


}