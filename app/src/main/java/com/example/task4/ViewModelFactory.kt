package com.example.task4

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.task4.car_details.CarDetailsViewModel
import com.example.task4.cars_list.CarListViewModel
import com.example.task4.database.CarDatabaseDao

class ViewModelFactory(
    private val dataSource: CarDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CarListViewModel::class.java) -> CarListViewModel(
                dataSource,
                application
            ) as T
            modelClass.isAssignableFrom(CarDetailsViewModel::class.java) -> CarDetailsViewModel(
                dataSource,
                application
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}