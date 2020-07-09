package com.example.task4.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_table")
data class Car(
    @PrimaryKey(autoGenerate = true)
    var carId: Long = 0L,

    @ColumnInfo(name = "brand")
    var brand: String = "",

    @ColumnInfo(name = "model")
    var model: String = "",

    @ColumnInfo(name = "year")
    var year: String = ""
)