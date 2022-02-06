package com.example.mymoney

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transition")
data class Transaction(@PrimaryKey(autoGenerate = true)  val id:Int,
                       val label:String,
                       val amount:Double,
                       val description:String):Serializable {

}