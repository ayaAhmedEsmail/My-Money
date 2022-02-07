package com.example.mymoney.Room

import androidx.room.*
import com.example.mymoney.Room.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * from transition")
    fun getAll(): List<Transaction>

    @Insert
    fun insertAll(vararg transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Update
    fun update(vararg transaction: Transaction)
}