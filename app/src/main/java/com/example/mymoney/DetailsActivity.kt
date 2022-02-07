package com.example.mymoney

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.example.mymoney.Room.AppDB
import com.example.mymoney.Room.Transaction
import kotlinx.android.synthetic.main.activity_add_transaction.amount_layout
import kotlinx.android.synthetic.main.activity_add_transaction.btn_back
import kotlinx.android.synthetic.main.activity_add_transaction.label_layout
import kotlinx.android.synthetic.main.activity_detailes.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {
    private lateinit var transaction: Transaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailes)

         transaction =intent.getSerializableExtra("transaction")as Transaction
         labelInputDetails.setText(transaction.label)
         amountInputDetails.setText(transaction.amount.toString())
         decInputDetails.setText(transaction.description)

        rootView.setOnClickListener {
            this.window.decorView.clearFocus()
            val im= getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager
            im.hideSoftInputFromWindow(it.windowToken,0)
        }


        labelInputDetails.addTextChangedListener{
            btn_updateTransaction.visibility = View.VISIBLE
            if (it!!.count()>0){
                labelInputDetails.error = null
            }
        }
        amountInputDetails.addTextChangedListener{
            btn_updateTransaction.visibility = View.VISIBLE
            if (it!!.count()>0){
                decLayoutDetails.error = null
            }
        }
        decInputDetails.addTextChangedListener{
            btn_updateTransaction.visibility = View.VISIBLE
            if (it!!.count()>0){
                decLayoutDetails.error = null
            }
        }

        // onclick update and save changes into room db
        btn_updateTransaction.setOnClickListener {
            val label = labelInputDetails.text.toString()
            val description = decInputDetails.text.toString()
            val amount = amountInputDetails.text.toString().toDoubleOrNull()

            if (label.isEmpty())
                label_layout.error = "Please add valid label"

            if (amount == null)
                amount_layout.error = "please add valid amount "
            else {
                val transaction = Transaction(transaction.id, label, amount, description)
                update(transaction)
            }
        }

        btn_back.setOnClickListener {
            finish()
        }
    }

    // update Room after editing.
    private fun update(transaction: Transaction) {
            val db = Room.databaseBuilder(
                this,
                AppDB::class.java,
                "transition").build()

            GlobalScope.launch {
                db.transactionDao().update(transaction)
                finish()
            }
    }
}