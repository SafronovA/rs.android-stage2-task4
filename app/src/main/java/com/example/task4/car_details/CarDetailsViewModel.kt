package com.example.task4.car_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.task4.database.Car
import com.example.task4.database.CarDatabaseDao
import kotlinx.coroutines.*

/**
 * ViewModel for CarDetailsFragment.
 */
class CarDetailsViewModel(
    val database: CarDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var brand = ""
    var model = ""
    var year = ""

    private val _insertNewCar = MutableLiveData<Boolean>()
    val insertNewCar
        get() = _insertNewCar

    fun onSubmitClicked() {
        _insertNewCar.value = true
    }

    private fun newCarInserted() {
        _insertNewCar.value = false
    }

    fun insertNewCarCard() {
        uiScope.launch {
            insert(Car(brand = brand, model = model, year = year))
        }
        newCarInserted()
    }

    private suspend fun insert(car: Car) {
        withContext(Dispatchers.IO) {
            database.insert(car)
        }
    }

    private suspend fun update(car: Car) {
        withContext(Dispatchers.IO) {
            database.update(car)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}