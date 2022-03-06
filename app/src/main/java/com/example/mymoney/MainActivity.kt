package com.example.mymoney

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.mymoney.Room.AppDB
import com.example.mymoney.Room.Transaction
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


class MainActivity : AppCompatActivity() {
    private lateinit var transaction: List<Transaction>
    private lateinit var oldTransaction: List<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var db: AppDB
    private lateinit var deletedTransaction: Transaction

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.coordinator)
        val navView:NavigationView= findViewById(R.id.nav_view)

        toggle= ActionBarDrawerToggle(this, drawerLayout,R.string.nav_open,R.string.nav_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_reminder -> {
                    Toast.makeText(applicationContext, "Reminder Page", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, ReminderActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_home -> {
                    Toast.makeText(applicationContext,"Home Page",Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

            }
            true
        }



        transaction = arrayListOf()


        transactionAdapter = TransactionAdapter(transaction)
        linearLayoutManager = LinearLayoutManager(this)
        db = Room.databaseBuilder(this, AppDB::class.java, "transition").build()

        rcy_budget.apply {
            adapter = transactionAdapter
            layoutManager = linearLayoutManager

        }

        //swipe to delete
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transaction[viewHolder.adapterPosition])
                rcy_budget.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }

        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(rcy_budget)


        fab_btn.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
       // handleRecyclerViewState(rcy_budget)


    }


    private fun fetchAll() {
        GlobalScope.launch {
            //  db.transactionDao().insertAll(Transaction(0,"Ice cream",-10.0,""))
            transaction = db.transactionDao().getAll()

            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transaction)

            }
        }
    }

    private fun updateDashboard() {
        val totalAmount: Double = transaction.map { it.amount }.sum().absoluteValue


        val budgetAmount: Double = transaction.filter {
            it.amount > 0 }.map { it.amount }.sum()

        val expensive: Double = budgetAmount - totalAmount

        budget.text = "%.2f".format(totalAmount)
        balance.text = "%.2f".format(budgetAmount)
        expense.text = "%.2f".format(expensive)
    }

    private fun deleteTransaction(transactions: Transaction) {
        deletedTransaction= transactions
        oldTransaction= transaction

        GlobalScope.launch {
            db.transactionDao().delete(transactions)
            transaction =transaction.filter{ it.id!= transactions.id }
            runOnUiThread{
                updateDashboard()
                transactionAdapter.setData(transaction)
                showSnackBar()
            }
        }
    }
    
    private fun undoDelete(){
        GlobalScope.launch {
            db.transactionDao().insertAll(deletedTransaction)

            transaction = oldTransaction

            runOnUiThread {
                transactionAdapter.setData(transaction)
                updateDashboard()
            }
        }
    }
 /* private fun handleRecyclerViewState(view: View) {
        if (transactionAdapter.itemCount == 0) {
            rcy_budget.visibility = View.GONE
            empty_image.visibility = View.VISIBLE
        }else{
            rcy_budget.visibility = View.VISIBLE
            empty_image.visibility = View.GONE

        }

    }*/
    private fun showSnackBar(){
        val view = findViewById<View>(R.id.coordinator)
        val snackBar = Snackbar.make(view, "Transaction deleted!",Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo"){
            undoDelete()
        }
                .setActionTextColor(ContextCompat.getColor(this, R.color.red))
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .show()
    }
    override fun onResume() {
        super.onResume()
        fetchAll()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}