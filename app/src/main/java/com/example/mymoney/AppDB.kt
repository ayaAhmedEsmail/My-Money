package com.example.mymoney

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Transaction::class), version = 1)
abstract class AppDB: RoomDatabase() {
    abstract fun transactionDao():TransactionDao
}