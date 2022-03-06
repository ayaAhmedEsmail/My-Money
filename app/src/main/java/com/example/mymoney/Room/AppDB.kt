package com.example.mymoney.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymoney.Room.Transaction
import com.example.mymoney.Room.TransactionDao

@Database(entities = [Transaction::class], version = 1)
abstract class AppDB: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}